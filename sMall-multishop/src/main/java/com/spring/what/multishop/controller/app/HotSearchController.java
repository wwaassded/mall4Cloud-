package com.spring.what.multishop.controller.app;

import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.multishop.service.HotSearchService;
import com.spring.what.multishop.vo.HotSearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("appHotSearchController")
@RequestMapping("/ua/app/hot_search")
@Tag(name = "app-热搜")
public class HotSearchController {

    @Resource
    private HotSearchService hotSearchService;

    @GetMapping("/list")
    @Operation(summary = "获取热搜列表", description = "获取热搜列表")
    @Parameter(name = "shopId", description = "店铺id")
    public ServerResponseEntity<List<HotSearchVO>> listByShopId(@RequestParam("shopId") Long shopId) {
        List<HotSearchVO> res = hotSearchService.listByShopId(shopId);
        return ServerResponseEntity.success(res);
    }
}
