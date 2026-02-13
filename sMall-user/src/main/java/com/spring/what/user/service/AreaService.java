package com.spring.what.user.service;

import com.spring.what.api.user.vo.AreaVO;
import com.spring.what.user.model.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【area(省市区地区信息)】的数据库操作Service
 * @createDate 2026-02-12 17:30:33
 */
public interface AreaService extends IService<Area> {

    List<AreaVO> listAreaVO();

    List<AreaVO> listAreaVO(Long pid);
}
