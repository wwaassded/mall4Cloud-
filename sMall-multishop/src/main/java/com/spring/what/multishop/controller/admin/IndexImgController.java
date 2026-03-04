package com.spring.what.multishop.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.constant.StatusEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.multishop.dto.IndexImgDTO;
import com.spring.what.multishop.model.IndexImg;
import com.spring.what.multishop.service.IndexImgService;
import com.spring.what.multishop.vo.IndexImgVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController("adminIndexImgController")
@RequestMapping("/admin/index_img")
@Tag(name = "admin-轮播图")
public class IndexImgController {

    @Resource
    private IndexImgService indexImgService;

    @GetMapping("/page/{limit}/{size}")
    @Operation(summary = "获取轮播图列表", description = "分页获取轮播图列表")
    public ServerResponseEntity<IPage<IndexImgVO>> page(@PathVariable Integer limit, @PathVariable Integer size, IndexImgDTO indexImgDTO) {
        Page<IndexImgVO> page = new Page<>(limit, size);
        indexImgDTO.setShopId(AuthContext.get().getTenantId());
        IPage<IndexImgVO> res = indexImgService.pageVO(page, indexImgDTO);
        return ServerResponseEntity.success(res);
    }

    @GetMapping
    @Operation(summary = "获取轮播图", description = "根据imgId获取轮播图")
    public ServerResponseEntity<IndexImgVO> getByImgId(@RequestParam Long imgId) {
        IndexImg indexImg = indexImgService.getById(imgId);
        if (Objects.nonNull(indexImg.getSpuId())) {
            //TODO: 完成product模块之后通过spuId获取Spu相关的信息
        }
        IndexImgVO indexImgVO = BeanUtil.map(indexImg, IndexImgVO.class);
        return ServerResponseEntity.success(indexImgVO);
    }

    @PostMapping
    @Operation(summary = "保存轮播图", description = "保存轮播图")
    @CacheEvict(cacheNames = CacheNames.INDEX_IMG_KEY, key = "#indexImgDTO.shopId")
    public ServerResponseEntity<Void> save(@Valid @RequestBody IndexImgDTO indexImgDTO) {
        indexImgDTO.setShopId(AuthContext.get().getTenantId());
        IndexImg indexImg = BeanUtil.map(indexImgDTO, IndexImg.class);
        indexImg.setImgId(null);
        indexImg.setStatus(StatusEnum.ENABLE.value());
        indexImgService.save(indexImg);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @Operation(summary = "更新轮播图", description = "更新轮播图")
    @CacheEvict(cacheNames = CacheNames.INDEX_IMG_KEY, key = "#indexImgDTO.shopId")
    public ServerResponseEntity<Void> update(@Valid @RequestBody IndexImgDTO indexImgDTO) {
        IndexImg indexImg = BeanUtil.map(indexImgDTO, IndexImg.class);
        indexImg.setShopId(AuthContext.get().getTenantId());
        indexImgService.updateById(indexImg);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除轮播图", description = "根据轮播图id删除轮播图")
    public ServerResponseEntity<Void> delete(@RequestParam Long imgId) {
        indexImgService.removeByIdAndShopId(imgId, AuthContext.get().getTenantId());
        return ServerResponseEntity.success();
    }
}

