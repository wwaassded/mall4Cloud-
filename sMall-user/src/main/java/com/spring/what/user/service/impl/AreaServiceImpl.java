package com.spring.what.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.api.user.vo.AreaVO;
import com.spring.what.cache.constant.CacheNames;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.user.model.Area;
import com.spring.what.user.service.AreaService;
import com.spring.what.user.mapper.AreaMapper;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author whatyi
 * @description 针对表【area(省市区地区信息)】的数据库操作Service实现
 * @createDate 2026-02-12 17:30:33
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area>
        implements AreaService {

    @Resource
    private AreaMapper areaMapper;

    //FIXME 应该将所有的
    @Override
    @Cacheable(cacheNames = CacheNames.AREA_INFO_KEY, key = "'areaList'", sync = true)
    public List<AreaVO> listAreaVO() {
        List<Area> areas = super.list();
        List<AreaVO> areaVOS = BeanUtil.mapAsList(areas, AreaVO.class);
        HashMap<Long, AreaVO> map = new HashMap<>();
        areaVOS.forEach(areaVO -> map.put(areaVO.getAreaId(), areaVO));
        for (AreaVO areaVO : areaVOS) {
            if (!Objects.isNull(areaVO.getParentId())) {
                AreaVO tmp = map.get(areaVO.getParentId());
                tmp.getAreas().add(areaVO);
                tmp.getAreaIds().add(areaVO.getAreaId());
            }
        }
        map.values().removeIf(areaVO -> areaVO.getLevel() != 0);
        return map.values().stream().toList();
    }

    @Override
    @Cacheable(cacheNames = CacheNames.AREA_KEY, key = "'list:' + #pid")
    public List<AreaVO> listAreaVO(Long pid) {
        LambdaQueryWrapper<Area> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Area::getParentId, pid);
        List<Area> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.mapAsList(areas, AreaVO.class);
    }
}




