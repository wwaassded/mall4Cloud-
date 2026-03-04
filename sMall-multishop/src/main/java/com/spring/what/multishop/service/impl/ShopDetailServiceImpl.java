package com.spring.what.multishop.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.dto.AuthAccountDTO;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.api.multishop.vo.ShopDetailVO;
import com.spring.what.api.rbac.dto.UserRoleDTO;
import com.spring.what.api.rbac.feign.UserRoleFeignClient;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.constant.StatusEnum;
import com.spring.what.common.constant.UserAdminType;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.common.util.IpHelper;
import com.spring.what.common.util.PrincipalUtil;
import com.spring.what.multishop.constant.ShopStatus;
import com.spring.what.multishop.dto.ShopDetailDTO;
import com.spring.what.multishop.dto.UpdateShopPasswordDTO;
import com.spring.what.multishop.mapper.ShopUserMapper;
import com.spring.what.multishop.model.ShopDetail;
import com.spring.what.multishop.model.ShopUser;
import com.spring.what.multishop.service.ShopDetailService;
import com.spring.what.multishop.mapper.ShopDetailMapper;
import com.spring.what.multishop.vo.ShopHeadInfoVO;
import com.spring.what.multishop.vo.ShopUserVO;
import com.spring.what.security.AuthContext;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author whatyi
 * @description 针对表【shop_detail(店铺详情)】的数据库操作Service实现
 * @createDate 2026-02-26 13:46:07
 */
@Service
public class ShopDetailServiceImpl extends ServiceImpl<ShopDetailMapper, ShopDetail>
        implements ShopDetailService {

    @Resource
    private ShopDetailMapper shopDetailMapper;

    @Resource
    private ShopUserMapper shopUserMapper;

    @Resource
    private ShopDetailService shopDetailService;

    @Resource
    private UserRoleFeignClient userRoleFeignClient;

    @Resource
    private AccountFeignClient accountFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(rollbackFor = Exception.class)
    public void createShop(ShopDetailDTO shopDetailDTO) {
        checkShopDetailDTO(shopDetailDTO);
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        if (Objects.nonNull(userInfoInTokenBO.getTenantId())) {
            throw new Mall4cloudException("用户已经创建商铺，无法重复创建");
        }
        ShopDetail shopDetail = BeanUtil.map(shopDetailDTO, ShopDetail.class);
        shopDetail.setShopStatus(ShopStatus.OPEN.value());
        shopDetail.setShopId(null);
        shopDetailMapper.insert(shopDetail);
        ShopUser shopUser = new ShopUser();
        shopUser.setShopId(shopDetail.getShopId());
        shopUser.setHasAccount(1);
        shopUser.setNickName(shopDetailDTO.getShopName());
        save(shopUser, null);
        AuthAccountDTO authAccountDTO = getAuthAccountDTO(shopDetailDTO, shopDetail, shopUser);
        accountFeignClient.update(authAccountDTO);
        userInfoInTokenBO.setTenantId(shopDetail.getShopId());
        ServerResponseEntity<Void> voidServerResponseEntity = accountFeignClient.updateUserInfoByUserIdAndSysType(userInfoInTokenBO, userInfoInTokenBO.getUserId(), SysTypeEnum.ORDINARY.value());
        if (!voidServerResponseEntity.isSuccess()) {
            throw new Mall4cloudException(voidServerResponseEntity.getMsg());
        }
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SHOP_DETAIL_ID_KEY, key = "#shopId")
    public ShopDetailVO getMyShopDetailByShopId(Long shopId) {
        ServerResponseEntity<AuthAccountVO> merchantInfoByTenantId = accountFeignClient.getMerchantInfoByTenantId(shopId);
        if (!merchantInfoByTenantId.isSuccess()) {
            throw new Mall4cloudException("无法获取商户的用户信息");
        }
        AuthAccountVO authAccountVO = merchantInfoByTenantId.getData();
        ShopDetailVO shopDetail = shopDetailMapper.getByShopId(shopId);
        if (Objects.nonNull(authAccountVO)) {
            shopDetail.setUsername(authAccountVO.getUsername());
        }
        return shopDetail;
    }

    @Override
    public Boolean checkShopName(String shopName) {
        Integer shopNumber = shopDetailMapper.countShopNumber(shopName, null);
        return shopNumber <= 0;
    }

    @Override
    public ShopHeadInfoVO getShopHeadInfo(Long shopId) {
        ShopHeadInfoVO shopHeadInfoVO = new ShopHeadInfoVO();
        ShopDetailVO shopDetailVO = shopDetailService.getMyShopDetailByShopId(shopId);
        if (Objects.isNull(shopDetailVO)) {
            throw new Mall4cloudException("店铺不存在");
        }
        shopHeadInfoVO.setShopStatus(shopDetailVO.getShopStatus());
        if (!Objects.equals(shopDetailVO.getShopStatus(), 1)) {
            return shopHeadInfoVO;
        }
        shopHeadInfoVO.setShopId(shopId);
        shopHeadInfoVO.setType(shopDetailVO.getType());
        shopHeadInfoVO.setIntro(shopDetailVO.getIntro());
        shopHeadInfoVO.setShopLogo(shopDetailVO.getShopLogo());
        shopHeadInfoVO.setShopName(shopDetailVO.getShopName());
        shopHeadInfoVO.setMobileBackgroundPic(shopDetailVO.getMobileBackgroundPic());
        return shopHeadInfoVO;
    }

    @Override
    public void updateShopPassword(UpdateShopPasswordDTO updateShopPasswordDTO) {
        if (!Objects.equals(updateShopPasswordDTO.getPassword(), updateShopPasswordDTO.getConfirmPsw())) {
            throw new Mall4cloudException("确认的密码一输入的密码不一致，请重新输入");
        }
        ShopUserVO shopUserVO = shopUserMapper.getAdminUser(updateShopPasswordDTO.getShopId());
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setPassword(updateShopPasswordDTO.getPassword());
        authAccountDTO.setUserId(shopUserVO.getShopUserId());
        authAccountDTO.setSysType(SysTypeEnum.MULTISHOP.value());
        accountFeignClient.updateShopPassword(authAccountDTO);
    }

    @Override
    public List<ShopDetail> listByShopIds(List<Long> shopIds) {
        if (shopIds == null || shopIds.isEmpty()) {
            return new ArrayList<>();
        }
        return shopDetailMapper.selectByIds(shopIds);
    }

    @Override
    public List<ShopDetail> listByShopIdsAndShopName(List<Long> shopIds, String shopName) {
        return shopDetailMapper.selectByIds(shopIds)
                .stream()
                .filter(shopDetail ->
                        Objects.equals(shopName, shopDetail.getShopName()))
                .toList();
    }

    private static @NonNull AuthAccountDTO getAuthAccountDTO(ShopDetailDTO shopDetailDTO, ShopDetail shopDetail, ShopUser shopUser) {
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setTenantId(shopDetail.getShopId());
        authAccountDTO.setUsername(shopDetailDTO.getUsername());
        authAccountDTO.setPassword(shopDetailDTO.getPassword());
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        authAccountDTO.setStatus(StatusEnum.ENABLE.value());
        authAccountDTO.setSysType(SysTypeEnum.MULTISHOP.value());
        authAccountDTO.setIsAdmin(UserAdminType.ADMIN.value());
        authAccountDTO.setUserId(shopUser.getShopUserId());
        return authAccountDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(ShopUser shopUser, List<Long> roleIds) {
        shopUserMapper.insert(shopUser);
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setRoleIds(roleIds);
        userRoleDTO.setUserId(shopUser.getShopUserId());
        userRoleFeignClient.saveByUserIdAndSysType(userRoleDTO);
    }

    private void checkShopDetailDTO(ShopDetailDTO shopDetailDTO) {
        if (!shopDetailDTO.getShopName().isEmpty()) {
            shopDetailDTO.setShopName(shopDetailDTO.getShopName().trim());
        }
        if (shopDetailMapper.countShopNumber(shopDetailDTO.getShopName(), null) > 0) {
            throw new Mall4cloudException("商店名称已经被使用过了");
        }
        String userName = shopDetailDTO.getUsername();
        if (!PrincipalUtil.isUserName(userName)) {
            throw new Mall4cloudException("用户名称的格式非法请重新输入");
        }
        ServerResponseEntity<AuthAccountVO> byUsernameAndSysType = accountFeignClient.getByUsernameAndSysType(userName, SysTypeEnum.MULTISHOP);
        if (!byUsernameAndSysType.isSuccess()) {
            throw new Mall4cloudException(byUsernameAndSysType.getMsg());
        }
        AuthAccountVO authAccountVO = byUsernameAndSysType.getData();
        if (Objects.nonNull(authAccountVO)) {
            throw new Mall4cloudException("用户已经创建无需重复创建");
        }
    }
}




