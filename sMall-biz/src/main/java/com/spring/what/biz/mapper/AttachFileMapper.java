package com.spring.what.biz.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.what.biz.model.AttachFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spring.what.biz.vo.AttachFileVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author whatyi
 * @description 针对表【attach_file(上传文件记录表)】的数据库操作Mapper
 * @createDate 2026-02-11 20:41:54
 * @Entity com.spring.what.biz.model.AttachFile
 */
public interface AttachFileMapper extends BaseMapper<AttachFile> {

    IPage<AttachFileVO> pageForAttachFileVO(Page<AttachFileVO> page, @Param("filename") String fileName, @Param("tenantid") Long tenantId, @Param("filegroup") Long fileGroupId);
}




