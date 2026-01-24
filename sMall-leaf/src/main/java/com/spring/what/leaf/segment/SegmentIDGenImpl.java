package com.spring.what.leaf.segment;

import com.spring.what.leaf.IDGen;
import com.spring.what.leaf.common.Result;
import com.spring.what.leaf.common.Status;
import com.spring.what.leaf.segment.model.LeafAlloc;
import com.spring.what.leaf.segment.model.Segment;
import com.spring.what.leaf.segment.model.SegmentBuffer;
import com.spring.what.leaf.service.LeafAllocService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.bv.number.sign.NegativeOrZeroValidatorForBigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;


@Slf4j
@Component
public class SegmentIDGenImpl implements IDGen {

    @Resource
    private LeafAllocService leafAllocService;

    private static final long EXCEPTION_ID_IDCACHE_INIT_FALSE = -1;

    /**
     * key不存在时的异常码
     */
    private static final long EXCEPTION_ID_KEY_NOT_EXISTS = -2;

    /**
     * SegmentBuffer中的两个Segment均未从DB中装载时的异常码
     */
    private static final long EXCEPTION_ID_TWO_SEGMENTS_ARE_NULL = -3;

    /**
     * 最大步长不超过100,0000
     */
    private static final int MAX_STEP = 1000000;

    /**
     * 一个Segment维持时间为15分钟
     */
    private static final long SEGMENT_DURATION = 15 * 60 * 1000L;

    private final ConcurrentHashMap<String, SegmentBuffer> cache = new ConcurrentHashMap<>();

    private volatile boolean initOk = false;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final int DEFAULT_LOAD_FACTOR = 2;

    private ExecutorService service = new ThreadPoolExecutor(5,
            Integer.MAX_VALUE,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new UpdateThreadFactory());
    @Autowired
    private NegativeOrZeroValidatorForBigDecimal negativeOrZeroValidatorForBigDecimal;

    public static class UpdateThreadFactory implements ThreadFactory {

        private static int threadNumber = 0;

        private static synchronized int getNextThreadNumber() {
            return threadNumber++;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Thread-Segment-Update-" + getNextThreadNumber());
        }
    }

    @Override
    public Result getId(String key) {
        if (!initOk) {
            return new Result(EXCEPTION_ID_IDCACHE_INIT_FALSE, Status.EXCEPTION);
        }
        SegmentBuffer segmentBuffer = cache.get(key);
        if (segmentBuffer != null) {
            if (segmentBuffer.isInitOk()) { //先访问加锁后再次判断 而不是直接加锁 提高代码效率
                synchronized (segmentBuffer) {
                    if (segmentBuffer.isInitOk()) {
                        try {
                            updateSegmentFromDb(key, segmentBuffer.getCurrentSegment());
                            log.info("update leafkey {} {} from db", key, segmentBuffer.getCurrentSegment());
                            segmentBuffer.setInitOk(true);
                        } catch (Exception e) {
                            log.warn("init segment from db exception", e);
                        }
                    }
                }
            }
            return getIdFromSegmentBuffer(segmentBuffer);
        }
        return new Result(EXCEPTION_ID_KEY_NOT_EXISTS, Status.EXCEPTION);
    }

    @Override
    public boolean init() {
        log.info("leafSegment开始进行初始化...");
        updateCacheFromDb();
        initOk = true;
        updateCacheFromDbAtEveryMinute();
        return initOk;
    }

    private void updateCacheFromDb() {
        log.info("从db中更新cache");
        try {
            List<String> dbTags = leafAllocService.getAllTags();
            if (dbTags == null || dbTags.isEmpty()) {
                return;
            }
            List<String> cacheTags = new ArrayList<>(cache.keySet());
            Set<String> insertSet = new HashSet<>(dbTags);
            Set<String> removeSet = new HashSet<>(cacheTags);
            for (String tag : cacheTags) {
                insertSet.remove(tag);
            }
            for (String tag : insertSet) {
                SegmentBuffer segmentBuffer = new SegmentBuffer();
                segmentBuffer.setKey(tag);
                Segment currentSegment = segmentBuffer.getCurrentSegment();
                currentSegment.setValue(new AtomicLong(0));
                currentSegment.setMax(0);
                currentSegment.setStep(0);
                cache.put(tag, segmentBuffer);
                log.info("{} 从数据库中初始化到cache中的buffer{}", tag, segmentBuffer);
            }
            for (String tag : dbTags) {
                removeSet.remove(tag);
            }
            for (String tag : removeSet) {
                SegmentBuffer removedBuffer = cache.remove(tag);
                log.info("{} 从cache中删除掉的buffer{}", tag, removedBuffer);
            }
        } catch (Exception e) {
            log.warn("update cache from db exception", e);
        }
    }

    private void updateCacheFromDbAtEveryMinute() {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName("check-idCache-thread");
                        t.setDaemon(true);
                        return t;
                    }
                }
        );
        scheduledExecutorService.scheduleWithFixedDelay(this::updateCacheFromDb, 60, 60, TimeUnit.SECONDS);
    }

    public Result getIdFromSegmentBuffer(final SegmentBuffer buffer) {
        while (true) {
            buffer.rLock().lock();
            try {
                final Segment segment = buffer.getCurrentSegment();
                if (!buffer.isNextReady() && (segment.getIdle() < 0.9 * segment.getStep())
                        && buffer.getThreadRunning().compareAndSet(false, true)) {
                    service.execute(() -> {
                        Segment nextSegment = buffer.getSegments()[buffer.nextPos()];
                        boolean updateReady = false;
                        try {
                            updateSegmentFromDb(buffer.getKey(), nextSegment);
                            updateReady = true;
                        } catch (Exception e) {
                            log.warn("update {} next segment from db exception", buffer.getKey(), e);
                        } finally {
                            if (updateReady) {
                                buffer.wLock().lock();
                                buffer.setNextReady(true);
                                buffer.getThreadRunning().set(false);
                                buffer.wLock().unlock();
                            } else {
                                buffer.getThreadRunning().set(false);
                            }
                        }
                    });
                }
                long value;
                if (segment.getRandomStep() > 1) {
                    value = segment.getValue().getAndAdd(randomAdd(segment.getRandomStep()));
                } else {
                    value = segment.getValue().getAndIncrement();
                }
                if (value < segment.getMax()) {
                    return new Result(value, Status.OK);
                }
            } finally {
                buffer.rLock().unlock();
            }
            waitAndSleep(buffer);
            buffer.wLock().lock();
            try {
                long value;
                Segment currentSegment = buffer.getCurrentSegment();
                value = currentSegment.getValue().getAndIncrement();
                if (value < currentSegment.getMax()) {
                    return new Result(value, Status.OK);
                }
                if (buffer.isNextReady()) {
                    buffer.switchPos();
                    buffer.setNextReady(false);
                } else {
                    log.warn("{} both segments is not ready", buffer);
                    return new Result(EXCEPTION_ID_TWO_SEGMENTS_ARE_NULL, Status.EXCEPTION);
                }
            } finally {
                buffer.wLock().unlock();
            }
        }
    }

    private void waitAndSleep(SegmentBuffer buffer) {
        int roll = 0;
        while (buffer.getThreadRunning().get()) {
            roll += 1;
            if (roll > 10000) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    break;
                } catch (InterruptedException e) {
                    log.warn("Thread {} Interrupted, Exception:", Thread.currentThread().getName(), e);
                    break;
                }
            }
        }
    }

    private long randomAdd(int random) {
        return RANDOM.nextInt(random - 1) + 1;
    }

    public void updateSegmentFromDb(String key, Segment segment) {
        SegmentBuffer buffer = segment.getBuffer();
        LeafAlloc leafAlloc;
        if (buffer.isInitOk()) {
            leafAlloc = leafAllocService.updateMaxIdAndGetLeafAlloc(key);
            buffer.setStep(leafAlloc.getStep());
            buffer.setMinStep(leafAlloc.getStep());
        } else if (buffer.getUpdateTimestamp() == 0) {
            leafAlloc = leafAllocService.updateMaxIdAndGetLeafAlloc(key);
            buffer.setStep(leafAlloc.getStep());
            buffer.setMinStep(leafAlloc.getStep());
            buffer.setUpdateTimestamp(System.currentTimeMillis());
        } else {
            long duration = System.currentTimeMillis() - buffer.getUpdateTimestamp();
            int nextStep = getNextStep(buffer, duration);
            leafAlloc = new LeafAlloc();
            leafAlloc.setBizTag(key);
            leafAlloc.setStep(nextStep);
            leafAlloc = leafAllocService.updateMaxIdByCustomStepAndGetLeafAlloc(leafAlloc);
            buffer.setUpdateTimestamp(System.currentTimeMillis());
            buffer.setStep(nextStep);
            buffer.setMinStep(leafAlloc.getStep());
        }
        long value = leafAlloc.getMaxId() - buffer.getStep();
        segment.getValue().set(value);
        segment.setMax(leafAlloc.getMaxId());
        segment.setStep(buffer.getStep());
        segment.setRandomStep(leafAlloc.getRandomStep());
    }

    /// 根据距离上一次消费的时间动态生成新号段的step 适应不同的消费速度
    private static int getNextStep(SegmentBuffer buffer, long duration) {
        int nextStep = buffer.getStep();
        if (duration < SEGMENT_DURATION) {
            if (nextStep * DEFAULT_LOAD_FACTOR > MAX_STEP) {
                //Do nothing
            } else {
                nextStep = nextStep * DEFAULT_LOAD_FACTOR;
            }
        } else if (duration < DEFAULT_LOAD_FACTOR * SEGMENT_DURATION) {
            //Do nothing
        } else {
            nextStep = nextStep / DEFAULT_LOAD_FACTOR;
            nextStep = Math.max(nextStep, buffer.getMinStep());
        }
        return nextStep;
    }

    public List<LeafAlloc> getAllLeafAllocs() {
        return leafAllocService.getAllLeafAllocs();
    }

    public Map<String, SegmentBuffer> getCache() {
        return cache;
    }
}
