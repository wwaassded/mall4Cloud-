package com.spring.what.search.canal;

import cn.throwx.canal.gule.common.BinLogEventType;
import cn.throwx.canal.gule.common.OperationType;
import cn.throwx.canal.gule.model.CanalBinLogEvent;
import cn.throwx.canal.gule.model.CanalBinLogResult;
import cn.throwx.canal.gule.support.parser.BaseCommonEntryFunction;
import cn.throwx.canal.gule.support.parser.BasePrimaryKeyTupleFunction;
import cn.throwx.canal.gule.support.parser.CanalBinLogEventParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SuperMallCanalBinLogEventParser implements CanalBinLogEventParser {
    private static final Logger log = LoggerFactory.getLogger(SuperMallCanalBinLogEventParser.class);

    @Override
    public <T> List<CanalBinLogResult<T>> parse(CanalBinLogEvent canalBinLogEvent,
                                                Class<T> aClass,
                                                BasePrimaryKeyTupleFunction basePrimaryKeyTupleFunction,
                                                BaseCommonEntryFunction<T> baseCommonEntryFunction) {
        BinLogEventType eventType = BinLogEventType.fromType(canalBinLogEvent.getType());
        if (BinLogEventType.CREATE.equals(eventType) || BinLogEventType.ALTER.equals(eventType)) {
            if (log.isDebugEnabled()) {
                log.error("监听到了不需要进行处理的sql操作");
            }
            return Collections.emptyList();
        }
        if (!Objects.equals(BinLogEventType.UNKNOWN, eventType) && !Objects.equals(BinLogEventType.QUERY, eventType)) {
            if (canalBinLogEvent.getIsDdl()) {
                CanalBinLogResult<T> binLogResult = new CanalBinLogResult<>();
                binLogResult.setBinLogEventType(eventType);
                binLogResult.setSql(canalBinLogEvent.getSql());
                binLogResult.setDatabaseName(canalBinLogEvent.getDatabase());
                binLogResult.setOperationType(OperationType.DDL);
                binLogResult.setTableName(canalBinLogEvent.getTable());
                return Collections.singletonList(binLogResult);
            } else {
                List<CanalBinLogResult<T>> canalBinLogResults = new ArrayList<>();
                List<Map<String, String>> afterData = canalBinLogEvent.getData();
                List<Map<String, String>> beforeData = canalBinLogEvent.getOld();
                List<String> pkNames = canalBinLogEvent.getPkNames();
                Optional.ofNullable(pkNames).filter(list -> list.size() == 1).orElseThrow(() -> new IllegalArgumentException("DML的主键有且仅有一个"));
                String pkName = pkNames.get(0);
                int afterSize = afterData == null ? 0 : afterData.size();
                int beforeSize = beforeData == null ? 0 : beforeData.size();
                if (afterSize > 0) {
                    for (int i = 0; i < afterSize; ++i) {
                        CanalBinLogResult<T> canalBinLogResult = new CanalBinLogResult<>();
                        canalBinLogResults.add(canalBinLogResult);
                        canalBinLogResult.setTableName(canalBinLogEvent.getTable());
                        canalBinLogResult.setBinLogEventType(eventType);
                        canalBinLogResult.setOperationType(OperationType.DML);
                        canalBinLogResult.setSql(canalBinLogEvent.getSql());
                        canalBinLogResult.setDatabaseName(canalBinLogEvent.getDatabase());
                        Map<String, String> afterDataMap = afterData.get(i);
                        canalBinLogResult.setAfterData(baseCommonEntryFunction.apply(afterDataMap));
                        Map<String, String> old = null;
                        if (beforeSize > 0 && i <= beforeSize) {
                            old = beforeData.get(i);
                            canalBinLogResult.setBeforeData(baseCommonEntryFunction.apply(old));
                        }
                        canalBinLogResult.setPrimaryKey(basePrimaryKeyTupleFunction.apply(afterDataMap, old, pkName));
                    }
                }
                return canalBinLogResults;
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("监听到了不需要进行处理的sql操作");
            }
            return Collections.emptyList();
        }
    }

    private SuperMallCanalBinLogEventParser() {
    }

    public static SuperMallCanalBinLogEventParser of() {
        return new SuperMallCanalBinLogEventParser();
    }
}
