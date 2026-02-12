package com.spring.what.biz.controller.multishop;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.spring.what.biz.dto.AttachFileGroupDTO;
import com.spring.what.biz.model.AttachFile;
import com.spring.what.biz.model.AttachFileGroup;
import com.spring.what.biz.service.AttachFileGroupService;
import com.spring.what.biz.service.AttachFileService;
import com.spring.what.biz.vo.AttachFileGroupVO;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import com.spring.what.security.AuthContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("multishopAttachFileGroupController")
@RequestMapping("/m/attach_file_group")
@Tag(name = "店铺-文件分组")
public class AttachFileGroupController {

    @Resource
    private AttachFileGroupService attachFileGroupService;

    @Resource
    private AttachFileService attachFileService;

    @GetMapping("/list")
    @Operation(summary = "获取列表", description = "分页获取列表")
    public ServerResponseEntity<List<AttachFileGroupVO>> list() {
        LambdaQueryWrapper<AttachFileGroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AttachFileGroup::getShopId, AuthContext.get().getTenantId());
        List<AttachFileGroup> attachFileGroups = attachFileGroupService.list(lambdaQueryWrapper);
        List<AttachFileGroupVO> attachFileGroupVOS = BeanUtil.mapAsList(attachFileGroups, AttachFileGroupVO.class);
        return ServerResponseEntity.success(attachFileGroupVOS);
    }

    @GetMapping
    @Operation(summary = "获取", description = "根据attachFileGroupId获取")
    public ServerResponseEntity<AttachFileGroupVO> getByAttachFileGroupId(@RequestParam Long attachFileGroupId) {
        AttachFileGroup attachFileGroup = attachFileGroupService.getById(attachFileGroupId);
        AttachFileGroupVO attachFileGroupVO = BeanUtil.map(attachFileGroup, AttachFileGroupVO.class);
        return ServerResponseEntity.success(attachFileGroupVO);
    }

    @PostMapping
    @Operation(summary = "保存", description = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody AttachFileGroupDTO attachFileGroupDTO) {
        AttachFileGroup attachFile = BeanUtil.map(attachFileGroupDTO, AttachFileGroup.class);
        attachFile.setAttachFileGroupId(null);
        attachFile.setType(1);
        attachFileGroupService.save(attachFile);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @Operation(summary = "更新", description = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody AttachFileGroupDTO attachFileGroupDTO) {
        AttachFileGroup attachFileGroup = BeanUtil.map(attachFileGroupDTO, AttachFileGroup.class);
        attachFileGroup.setShopId(AuthContext.get().getTenantId());
        attachFileGroupService.updateById(attachFileGroup);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @Operation(summary = "删除", description = "根据id删除")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> delete(@RequestParam Long attachFileGroupId) {
        LambdaQueryWrapper<AttachFile> attachFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        attachFileLambdaQueryWrapper.eq(AttachFile::getAttachFileGroupId, attachFileGroupId);
        attachFileService.remove(attachFileLambdaQueryWrapper);
        attachFileGroupService.removeById(attachFileGroupId);
        return ServerResponseEntity.success();
    }
}
