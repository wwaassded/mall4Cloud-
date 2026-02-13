package com.spring.what.api.user.feign;

import com.spring.what.common.feign.FeignInsideAuthConfig;
import com.spring.what.common.order.vo.UserAddrVO;
import com.spring.what.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户地址feign连接
 *
 * @author FrozenWatermelon
 * @date 2020/12/07
 */
@FeignClient(value = "mall4cloud-user", contextId = "userAddr")
public interface UserAddrFeignClient {




    /**
     * 根据地址id获取用户地址信息
     *
     * @param addrId 地址id
     * @return 用户地址信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/userAddr/getUserAddrByAddrId")
    ServerResponseEntity<UserAddrVO> getUserAddrByAddrId(@RequestParam("addrId") Long addrId);

}
