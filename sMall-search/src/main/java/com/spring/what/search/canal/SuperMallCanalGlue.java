package com.spring.what.search.canal;

import cn.hutool.core.collection.CollUtil;
import cn.throwx.canal.gule.CanalGlue;
import cn.throwx.canal.gule.model.CanalBinLogEvent;
import cn.throwx.canal.gule.model.ModelTable;
import cn.throwx.canal.gule.support.adapter.SourceAdapterFacade;
import cn.throwx.canal.gule.support.processor.BaseCanalBinlogEventProcessor;
import cn.throwx.canal.gule.support.processor.CanalBinlogEventProcessorFactory;

import java.util.List;

public class SuperMallCanalGlue implements CanalGlue {

    private final CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory;

    @Override
    public void process(String s) {
        CanalBinLogEvent canalBinLogEvent = SourceAdapterFacade.X.adapt(CanalBinLogEvent.class, s);
        ModelTable modelTable = ModelTable.of(canalBinLogEvent.getDatabase(), canalBinLogEvent.getTable());
        List<BaseCanalBinlogEventProcessor<?>> eventProcessors = canalBinlogEventProcessorFactory.get(modelTable);
        if (CollUtil.isEmpty(eventProcessors)) {
            return;
        }
        eventProcessors.forEach(processer -> processer.process(canalBinLogEvent));
    }

    private SuperMallCanalGlue(CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory) {
        this.canalBinlogEventProcessorFactory = canalBinlogEventProcessorFactory;
    }

    public static SuperMallCanalGlue of(CanalBinlogEventProcessorFactory canalBinlogEventProcessorFactory) {
        return new SuperMallCanalGlue(canalBinlogEventProcessorFactory);
    }
}
