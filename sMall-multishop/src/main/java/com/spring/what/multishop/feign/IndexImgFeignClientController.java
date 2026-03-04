package com.spring.what.multishop.feign;

import com.spring.what.api.multishop.feign.IndexImgFeignClient;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.service.IndexImgService;
import com.spring.what.security.AuthContext;
import jakarta.annotation.Resource;

import java.util.Objects;

public class IndexImgFeignClientController implements IndexImgFeignClient {

    @Resource
    private IndexImgService indexImgService;

    @Override
    public ServerResponseEntity<Void> deleteBySpuId(Long spuId, Long shopId) {
        if (!Objects.equals(shopId, AuthContext.get().getTenantId())) {
            return ServerResponseEntity.fail(ResponseEnum.UNAUTHORIZED);
        }
        indexImgService.deleteBySpuId(spuId, shopId);
        return ServerResponseEntity.success();
    }
}
