package com.spring.what.search.controller.platform;

import com.spring.what.api.dto.EsPageDTO;
import com.spring.what.api.dto.ProductSearchDTO;
import com.spring.what.api.vo.EsPageVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.search.constant.SearchTypeEnum;
import com.spring.what.search.manager.ProductSearchManager;
import com.spring.what.search.vo.SpuAdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("platformSearchSpuController")
@RequestMapping("/p/search")
@Tag(name = "platform-spu列表接口")
public class ProductSearchController {

    @Resource
    private ProductSearchManager productSearchManager;

    @GetMapping("/page")
    @Operation(summary = "商品管理信息列表（平台端）", description = "商品管理信息列表（平台端）")
    public ServerResponseEntity<EsPageVO<SpuAdminVO>> adminPage(@Valid EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        productSearchDTO.setSearchType(SearchTypeEnum.PLATFORM.value());
        EsPageVO<SpuAdminVO> spuAdminVOEsPageVO = productSearchManager.adminPage(pageDTO, productSearchDTO);
        return ServerResponseEntity.success(spuAdminVOEsPageVO);
    }
}