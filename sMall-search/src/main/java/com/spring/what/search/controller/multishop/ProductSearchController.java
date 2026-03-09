package com.spring.what.search.controller.multishop;

import com.spring.what.api.dto.EsPageDTO;
import com.spring.what.api.dto.ProductSearchDTO;
import com.spring.what.api.vo.EsPageVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.search.constant.SearchTypeEnum;
import com.spring.what.search.manager.ProductSearchManager;
import com.spring.what.search.vo.SpuAdminVO;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("multishopSearchSpuController")
@RequestMapping("/m/search")
@Tag(name = "multishop-spu管理列表接口")
public class ProductSearchController {

    @Resource
    private ProductSearchManager productSearchManager;

    @GetMapping("/page")
    @Operation(summary = "商品信息列表", description = "商品信息列表")
    public ServerResponseEntity<EsPageVO<SpuAdminVO>> page(@Valid EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        Long shopId = AuthContext.get().getTenantId();
        productSearchDTO.setShopId(shopId);
        productSearchDTO.setSearchType(SearchTypeEnum.MULTISHOP.value());
        EsPageVO<SpuAdminVO> searchPage = productSearchManager.adminPage(pageDTO, productSearchDTO);
        return ServerResponseEntity.success(searchPage);
    }

}