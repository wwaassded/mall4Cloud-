package com.spring.what.leaf.segment.model;

import lombok.Data;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class SegmentBuffer {

    private String key;

    private final Segment[] segments; // 双segment

    private volatile int currentPos; // 现在使用的是哪个segment

    private volatile boolean initOk;

    private volatile boolean nextReady;

    private volatile int step;
    private volatile int minStep;
    private volatile long updateTimestamp;

    private AtomicBoolean threadRunning;

    private ReadWriteLock lock;

    public SegmentBuffer() {
        segments = new Segment[]{new Segment(this), new Segment(this)};
        currentPos = 0;
        initOk = false;
        nextReady = false;
        threadRunning = new AtomicBoolean(false);
        lock = new ReentrantReadWriteLock();
    }

    public Segment getCurrentSegment() {
        return segments[currentPos];
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    public int nextPos() {
        return (currentPos + 1) % 2;
    }

    public void switchPos() {
        currentPos = nextPos();
    }

    public boolean isInitOk() {
        return !initOk;
    }

    @Override
    public String toString() {
        return "SegmentBuffer{" + "key='" + key + '\'' +
                ", segments=" + Arrays.toString(segments) +
                ", currentPos=" + currentPos +
                ", nextReady=" + nextReady +
                ", initOk=" + initOk +
                ", threadRunning=" + threadRunning +
                ", step=" + step +
                ", minStep=" + minStep +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }

}

