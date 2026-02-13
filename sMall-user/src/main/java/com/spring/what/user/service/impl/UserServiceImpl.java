package com.spring.what.user.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.api.auth.constant.SysTypeEnum;
import com.spring.what.api.auth.dto.AuthAccountDTO;
import com.spring.what.api.auth.feign.AccountFeignClient;
import com.spring.what.api.auth.vo.AuthAccountVO;
import com.spring.what.api.leaf.feign.LeafFeignClient;
import com.spring.what.api.user.vo.UserApiVO;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ResponseEnum;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.IpHelper;
import com.spring.what.security.AuthContext;
import com.spring.what.user.dto.UserRegisterDTO;
import com.spring.what.user.model.User;
import com.spring.what.user.service.UserService;
import com.spring.what.user.mapper.UserMapper;
import com.spring.what.user.vo.UserSimpleInfoVO;
import io.seata.spring.annotation.GlobalTransactional;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author whatyi
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2026-02-12 17:30:33
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private LeafFeignClient leafFeignClient;
    @Autowired
    private AccountFeignClient accountFeignClient;

    @Override
    public IPage<UserApiVO> page(Page<UserApiVO> page) {
        return userMapper.getPage(page);
    }

    @Override
    public UserSimpleInfoVO getByAddrId() {
        return userMapper.getByAddrId(AuthContext.get().getUserId());
    }

    @Override
    @Cacheable(cacheNames = CacheNames.USER_INFO, key = "#userId")
    public UserApiVO getUserApiVOById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    @CacheEvict(cacheNames = CacheNames.USER_INFO, key = "#user.userId")
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Long saveFromUSerREgisterDTO(UserRegisterDTO param) {
        this.verifyRegisterDTO(param);
        ServerResponseEntity<Long> segmentId = leafFeignClient.getSegmentId(User.DISTRIBUTED_ID_KEY);
        if (!segmentId.isSuccess()) {
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
        Long userId = segmentId.getData();
        AuthAccountDTO authAccountDTO = new AuthAccountDTO();
        authAccountDTO.setCreateIp(IpHelper.getIpAddr());
        authAccountDTO.setPassword(param.getPassword());
        authAccountDTO.setIsAdmin(0);
        authAccountDTO.setSysType(SysTypeEnum.ORDINARY.value());
        authAccountDTO.setUsername(param.getUserName());
        authAccountDTO.setStatus(1);
        authAccountDTO.setUserId(userId);
        ServerResponseEntity<Long> saved = accountFeignClient.save(authAccountDTO);
        if (!saved.isSuccess()) {
            throw new Mall4cloudException(saved.getMsg());
        }
        User user = new User();
        user.setUserId(userId);
        user.setPic(param.getImg());
        user.setNickName(param.getNickName());
        user.setStatus(1);
        userMapper.insert(user);
        return saved.getData();
    }

    private void verifyRegisterDTO(UserRegisterDTO param) {
        ServerResponseEntity<AuthAccountVO> byUsernameAndSysType = accountFeignClient.getByUsernameAndSysType(param.getUserName(), SysTypeEnum.ORDINARY);
        if (!byUsernameAndSysType.isSuccess()) {
            throw new Mall4cloudException(byUsernameAndSysType.getMsg());
        }
        if (byUsernameAndSysType.getData() != null) {
            throw new Mall4cloudException("用户名已经存在");
        }
    }
}




