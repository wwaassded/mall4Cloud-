package com.spring.what.user.controller.app;

import com.spring.what.api.user.vo.AreaVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.user.service.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("appAreaController")
@RequestMapping("/area")
@Tag(name = "app-地区信息")
public class AreaController {

    @Resource
    private AreaService areaService;

    @GetMapping("/list")
    @Operation(summary = "获取省市区地区信息列表", description = "获取省市区地区信息列表")
    public ServerResponseEntity<List<AreaVO>> list() {
        List<AreaVO> areaVOS = areaService.listAreaVO();
        return ServerResponseEntity.success(areaVOS);
    }

    @GetMapping("/list_by_pid")
    @Operation(summary = "通过父级id获取区域列表", description = "通过父级id获取区域列表")
    public ServerResponseEntity<List<AreaVO>> listByPid(Long pid) {
        List<AreaVO> areaVOS = areaService.listAreaVO(pid);
        return ServerResponseEntity.success(areaVOS);
    }
}