package com.spring.what.leaf.service;

import com.spring.what.leaf.IDGen;
import com.spring.what.leaf.common.Result;
import com.spring.what.leaf.exception.InitException;
import com.spring.what.leaf.segment.SegmentIDGenImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("SegmentService")
public class SegmentService {

    @Resource
    private IDGen idGen;

    public SegmentService() throws InitException {
        if (idGen.init()) {
            log.info("Segment Service Init Successfully");
        } else {
            throw new InitException("Segment Service Init Fail");
        }
    }

    public Result getId(String key) {
        return idGen.getId(key);
    }

    public SegmentIDGenImpl getIdGen() {
        if (idGen instanceof SegmentIDGenImpl segmentIDGen) {
            return segmentIDGen;
        }
        return null;
    }

}
