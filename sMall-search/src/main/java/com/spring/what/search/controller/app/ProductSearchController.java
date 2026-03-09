package com.spring.what.search.controller.app;

import cn.hutool.core.collection.CollUtil;
import com.spring.what.api.dto.EsPageDTO;
import com.spring.what.api.dto.ProductSearchDTO;
import com.spring.what.api.multishop.bo.EsShopDetailBO;
import com.spring.what.api.multishop.feign.ShopDetailFeignClient;
import com.spring.what.api.vo.EsPageVO;
import com.spring.what.api.vo.search.ProductSearchVO;
import com.spring.what.api.vo.search.ShopInfoSearchVO;
import com.spring.what.common.constant.StatusEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.search.manager.ProductSearchManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController("appSearchSpuController")
@RequestMapping("/ua/search")
@Tag(name = "app-spu搜索接口")
public class ProductSearchController {

    @Resource
    private ProductSearchManager productSearchManager;

    @Resource
    private ShopDetailFeignClient shopDetailFeignClient;

    @GetMapping("/page")
    @Operation(summary = "商品信息列表-包含spu、品牌、分类、属性和店铺信息", description = "spu列表-包含spu、品牌、分类、属性和店铺信息")
    public ServerResponseEntity<EsPageVO<ProductSearchVO>> page(@Valid EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        productSearchDTO.setSpuStatus(StatusEnum.ENABLE.value());
        EsPageVO<ProductSearchVO> paged = productSearchManager.page(pageDTO, productSearchDTO);
        loadShopData(paged.getList());
        return ServerResponseEntity.success(paged);
    }

    @GetMapping("/simple_page")
    @Operation(summary = "商品信息列表-包含spu信息", description = "商品信息列表-包含spu信息")
    public ServerResponseEntity<EsPageVO<ProductSearchVO>> simplePage(@Valid EsPageDTO pageDTO, ProductSearchDTO productSearchDTO) {
        productSearchDTO.setSpuStatus(StatusEnum.ENABLE.value());
        EsPageVO<ProductSearchVO> pageVO = productSearchManager.simplePage(pageDTO, productSearchDTO);
        loadShopData(pageVO.getList());
        return ServerResponseEntity.success(pageVO);
    }

    /**
     * 获取店铺数据
     *
     * @param list
     */
    private void loadShopData(List<ProductSearchVO> list) {
        if (CollUtil.isEmpty(list)) {
            return;
        }
        for (ProductSearchVO productSearchVO : list) {
            ShopInfoSearchVO shopInfo = productSearchVO.getShopInfo();
            if (Objects.isNull(shopInfo) || Objects.isNull(shopInfo.getShopId())) {
                continue;
            }
            ServerResponseEntity<EsShopDetailBO> esShopDetailBOServerResponseEntity = shopDetailFeignClient.getShopByShopId(shopInfo.getShopId());
            if (esShopDetailBOServerResponseEntity == null || !esShopDetailBOServerResponseEntity.isSuccess()) {
                continue;
            }
            EsShopDetailBO esShopDetailBO = esShopDetailBOServerResponseEntity.getData();
            shopInfo.setShopLogo(esShopDetailBO.getShopLogo());
            shopInfo.setShopName(esShopDetailBO.getShopName());
            shopInfo.setType(esShopDetailBO.getType());
        }
    }
}