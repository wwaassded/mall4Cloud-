package com.spring.what.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.biz.model.AttachFile;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.what.biz.vo.AttachFileVO;

import java.util.List;

/**
 * @author whatyi
 * @description 针对表【attach_file(上传文件记录表)】的数据库操作Service
 * @createDate 2026-02-11 20:41:54
 */
public interface AttachFileService extends IService<AttachFile> {

    IPage<AttachFileVO> pageForAttachFileVO(Page<AttachFileVO> page, String fileName, Long fileGroupId);

    void save(List<AttachFile> attachFiles);

    Boolean updateFileName(AttachFile attachFile);

    void delete(Long fileId) throws Exception;
}
