package com.spring.what.multishop.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.multishop.model.HotSearch;
import com.spring.what.security.AuthContext;
import com.spring.what.multishop.dto.HotSearchDTO;
import com.spring.what.multishop.service.HotSearchService;
import com.spring.what.multishop.vo.HotSearchVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController("adminHotSearchController")
@RequestMapping("/admin/hot_search")
@Tag(name = "admin-热搜")
public class HotSearchController {

    @Resource
    private HotSearchService hotSearchService;

    @GetMapping("/page/{limit}/{size}")
    @Operation(summary = "分页获取热搜列表", description = "分页获取热搜列表")
    public ServerResponseEntity<IPage<HotSearchVO>> page(@PathVariable Integer limit, @PathVariable Integer size, HotSearchDTO hotSearchDTO) {
        Page<HotSearchVO> page = new Page<>(limit, size);
        hotSearchDTO.setShopId(AuthContext.get().getTenantId());
        IPage<HotSearchVO> res = hotSearchService.getPage(page, hotSearchDTO);
        return ServerResponseEntity.success(res);
    }

    @GetMapping
    @Operation(summary = "获取热搜", description = "根据hotSearchId获取热搜")
    public ServerResponseEntity<HotSearchVO> getByHotSearchId(@RequestParam Long hotSearchId) {
        return ServerResponseEntity.success(hotSearchService.getByHotSearchId(hotSearchId));
    }

    @PostMapping
    @Operation(summary = "保存热搜", description = "保存热搜")
    public ServerResponseEntity<Void> save(@Valid @RequestBody HotSearchDTO hotSearchDTO) {
        hotSearchDTO.setShopId(AuthContext.get().getTenantId());
        HotSearch hotSearch = BeanUtil.map(hotSearchDTO, HotSearch.class);
        hotSearch.setHotSearchId(null);
        hotSearchService.save(hotSearch);
        hotSearchService.removeHotSearchListCache(hotSearch.getShopId());
        return ServerResponseEntity.success();
    }

    @PutMapping
    @Operation(summary = "更新热搜", description = "更新热搜")
    public ServerResponseEntity<Void> update(@Valid @RequestBody HotSearchDTO hotSearchDTO) {
        hotSearchDTO.setShopId(AuthContext.get().getTenantId());
        hotSearchService.updateById(BeanUtil.map(hotSearchDTO, HotSearch.class));
        hotSearchService.removeHotSearchListCache(hotSearchDTO.getShopId());
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除热搜", description = "根据热搜id删除热搜")
    public ServerResponseEntity<Void> delete(@RequestParam Long hotSearchId) {
        hotSearchService.removeById(hotSearchId);
        hotSearchService.removeHotSearchListCache(hotSearchId);
        return ServerResponseEntity.success();
    }
}
