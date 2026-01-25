package com.spring.what.common.database.interceptor;

import com.spring.what.api.leaf.feign.LeafFeignClient;
import com.spring.what.common.database.annotation.DistributedId;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.model.BaseModel;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;


@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class})})
public class MybatisInterceptor implements Interceptor {

    /**
     * 单个插入名称
     */
    private static final String INSERT = "insert";

    /**
     * 单个插入名称
     */
    private static final String SAVE = "save";

    /**
     * 批量插入名称
     */
    private static final String BATCH_INSERT = "insertBatch";

    /**
     * 批量插入名称
     */
    private static final String BATCH_SAVE = "saveBatch";

    @Resource
    private LeafFeignClient leafFeignClient;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        if (SqlCommandType.INSERT != sqlCommandType) {
            return invocation.proceed();
        }
        Object parameter = invocation.getArgs()[1];
        Object dbObject = findDbObject(parameter);
        if (dbObject == null) {
            return invocation.proceed();
        }
        if (mappedStatement.getId().contains(INSERT) || mappedStatement.getId().contains(SAVE)) {
            generateKeyForObject(dbObject);
        } else if (mappedStatement.getId().contains(BATCH_INSERT) || mappedStatement.getId().contains(BATCH_SAVE)) {
            if (dbObject instanceof ArrayList<?> arrayList && !arrayList.isEmpty()) {
                Object firstItem = arrayList.get(0);
                if (findDbObject(firstItem) != null) {
                    for (Object o : arrayList) {
                        generateKeyForObject(o);
                    }
                }
            }
        }
        return invocation.proceed();
    }

    private Object findDbObject(Object o) {
        if (o instanceof BaseModel baseModel) {
            return baseModel;
        } else if (o instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                if (value instanceof BaseModel || value instanceof ArrayList<?>) {
                    return value;
                }
            }
        }
        return null;
    }

    private void generateKeyForObject(Object model) throws IllegalAccessException {
        Field[] fields = model.getClass().getFields();
        for (Field field : fields) {
            DistributedId distributedIdAnnotation = field.getAnnotation(DistributedId.class);
            if (distributedIdAnnotation == null)
                continue;
            field.setAccessible(true);
            String bizTag = distributedIdAnnotation.value();
            ServerResponseEntity<Long> responseEntity = leafFeignClient.getSegmentId(bizTag);
            if (responseEntity.isSuccess()) {
                field.set(model, responseEntity.getData());
            } else {
                log.error("can't get distributed id !!!! ");
                throw new Mall4cloudException(ResponseEnum.EXCEPTION);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor executor) {
            return Plugin.wrap(executor, this);
        } else {
            return target;
        }
    }
}
