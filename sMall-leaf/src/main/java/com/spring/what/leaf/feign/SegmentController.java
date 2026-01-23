package com.spring.what.leaf.feign;

import cn.hutool.core.util.StrUtil;
import com.spring.what.api.leaf.feign.LeafFeignClient;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.leaf.common.Result;
import com.spring.what.leaf.common.Status;
import com.spring.what.leaf.exception.LeafServerException;
import com.spring.what.leaf.exception.NoKeyException;
import com.spring.what.leaf.service.SegmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class SegmentController implements LeafFeignClient {

    @Resource
    private SegmentService segmentService;

    @Override
    public ServerResponseEntity<Long> getSegmentId(String key) {
        return ServerResponseEntity.success(get(key, segmentService.getId(key)));
    }

    private Long get(String key, Result result) {
        Result tmp;
        if (StrUtil.isBlank(key)) {
            throw new NoKeyException();
        }
        tmp = result;
        if (Objects.equals(tmp.getStatus(), Status.EXCEPTION)) {
            throw new LeafServerException(tmp.toString());
        }
        return result.getId();
    }
}
