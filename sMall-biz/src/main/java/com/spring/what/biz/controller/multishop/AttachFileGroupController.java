package com.spring.what.biz.controller.multishop;

import com.spring.what.biz.dto.AttachFileGroupDTO;
import com.spring.what.biz.vo.AttachFileGroupVO;
import com.spring.what.common.response.ServerResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("multishopAttachFileGroupController")
@RequestMapping("/m/attach_file_group")
@Tag(name = "店铺-文件分组")
public class AttachFileGroupController {

    @GetMapping("/list")
    @Operation(summary = "获取列表", description = "分页获取列表")
    public ServerResponseEntity<List<AttachFileGroupVO>> list() {
    }

    @GetMapping
    @Operation(summary = "获取", description = "根据attachFileGroupId获取")
    public ServerResponseEntity<AttachFileGroupVO> getByAttachFileGroupId(@RequestParam Long attachFileGroupId) {
    }

    @PostMapping
    @Operation(summary = "保存", description = "保存")
    public ServerResponseEntity<Void> save(@Valid @RequestBody AttachFileGroupDTO attachFileGroupDTO) {
    }

    @PutMapping
    @Operation(summary = "更新", description = "更新")
    public ServerResponseEntity<Void> update(@Valid @RequestBody AttachFileGroupDTO attachFileGroupDTO) {
    }

    @DeleteMapping
    @Operation(summary = "删除", description = "根据id删除")
    public ServerResponseEntity<Void> delete(@RequestParam Long attachFileGroupId) {
    }
}
