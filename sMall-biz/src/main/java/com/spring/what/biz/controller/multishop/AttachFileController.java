package com.spring.what.biz.controller.multishop;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.biz.dto.AttachFileDTO;
import com.spring.what.biz.model.AttachFile;
import com.spring.what.biz.service.AttachFileService;
import com.spring.what.biz.vo.AttachFileVO;
import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ServerResponseEntity;
import com.spring.what.common.util.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("multishopAttachFileController")
@RequestMapping("/m/attach_file")
@Tag(name = "上传文件记录表")
public class AttachFileController {

    @Resource
    private AttachFileService attachFileService;

    @GetMapping("/page/{limit}/{size}")
    @Operation(summary = "获取上传文件记录表列表", description = "分页获取上传文件记录表列表")
    public ServerResponseEntity<IPage<AttachFileVO>> page(@PathVariable Integer limit, @PathVariable Integer size, String fileName, Long fileGroupId) {
        Page<AttachFileVO> page = new Page<>(limit, size);
        if (fileGroupId == 0) {
            fileGroupId = null;
        }
        IPage<AttachFileVO> result = attachFileService.pageForAttachFileVO(page, fileName, fileGroupId);
        return ServerResponseEntity.success(result);
    }

    @PostMapping
    @Operation(summary = "保存上传文件记录", description = "保存上传文件记录")
    public ServerResponseEntity<Void> save(@RequestBody List<AttachFileDTO> attachFileDtos) {
        List<AttachFile> attachFiles = BeanUtil.mapAsList(attachFileDtos, AttachFile.class);
        attachFileService.save(attachFiles);
        return ServerResponseEntity.success();
    }

    /**
     * 更改文件名或分组
     */
    @PutMapping("/update_file")
    @Operation(summary = "更新文件记录", description = "更新文件记录")
    public ServerResponseEntity<Boolean> updateFileName(@RequestBody AttachFileDTO attachFileDto) {
        if (StrUtil.isBlank(attachFileDto.getFileName())) {
            ServerResponseEntity.showFail("上传的图片名称不能为空");
        }
        AttachFile attachFile = BeanUtil.map(attachFileDto, AttachFile.class);
        return ServerResponseEntity.success(attachFileService.updateFileName(attachFile));
    }

    @DeleteMapping
    @Operation(summary = "删除上传文件记录", description = "根据上传文件记录表id删除上传文件记录")
    public ServerResponseEntity<Void> delete(@RequestParam Long fileId) throws Exception {
        attachFileService.delete(fileId);
        return ServerResponseEntity.success();
    }
}