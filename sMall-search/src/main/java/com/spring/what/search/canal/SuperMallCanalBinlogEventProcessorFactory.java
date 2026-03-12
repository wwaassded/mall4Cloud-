package com.spring.what.search.canal;

import cn.throwx.canal.gule.model.ModelTable;
import cn.throwx.canal.gule.support.processor.BaseCanalBinlogEventProcessor;
import cn.throwx.canal.gule.support.processor.CanalBinlogEventProcessorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SuperMallCanalBinlogEventProcessorFactory implements CanalBinlogEventProcessorFactory {

    private final ConcurrentHashMap<ModelTable, List<BaseCanalBinlogEventProcessor<?>>> cache = new ConcurrentHashMap<>(16);

    @Override
    public void register(ModelTable modelTable, BaseCanalBinlogEventProcessor<?> baseCanalBinlogEventProcessor) {
        synchronized (cache) {
            cache.putIfAbsent(modelTable, new ArrayList<>());
            cache.get(modelTable).add(baseCanalBinlogEventProcessor);
        }
    }

    @Override
    public List<BaseCanalBinlogEventProcessor<?>> get(ModelTable modelTable) {
        return cache.get(modelTable);
    }

    private SuperMallCanalBinlogEventProcessorFactory() {
    }

    public static SuperMallCanalBinlogEventProcessorFactory of() {
        return new SuperMallCanalBinlogEventProcessorFactory();
    }
}
