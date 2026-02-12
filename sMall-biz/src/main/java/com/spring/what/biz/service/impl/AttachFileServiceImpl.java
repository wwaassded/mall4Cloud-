package com.spring.what.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spring.what.api.auth.bo.UserInfoInTokenBO;
import com.spring.what.biz.config.MinioTemplate;
import com.spring.what.biz.model.AttachFile;
import com.spring.what.biz.service.AttachFileService;
import com.spring.what.biz.mapper.AttachFileMapper;
import com.spring.what.biz.vo.AttachFileVO;
import com.spring.what.security.AuthContext;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author whatyi
 * @description 针对表【attach_file(上传文件记录表)】的数据库操作Service实现
 * @createDate 2026-02-11 20:41:54
 */
@Service
public class AttachFileServiceImpl extends ServiceImpl<AttachFileMapper, AttachFile>
        implements AttachFileService {

    @Resource
    private AttachFileMapper attachFileMapper;

    @Resource
    private MinioTemplate minioTemplate;

    @Override
    public IPage<AttachFileVO> pageForAttachFileVO(Page<AttachFileVO> page, String fileName, Long fileGroupId) {
        return attachFileMapper.pageForAttachFileVO(page, fileName, AuthContext.get().getTenantId(), fileGroupId);
    }

    @Override
    public void save(List<AttachFile> attachFiles) {
        UserInfoInTokenBO userInfoInTokenBO = AuthContext.get();
        attachFiles.forEach(attachFile -> attachFile.setShopId(userInfoInTokenBO.getTenantId()));
        attachFileMapper.insert(attachFiles);
    }

    @Override
    public Boolean updateFileName(AttachFile attachFile) {
        if (Objects.isNull(attachFile.getAttachFileGroupId()) && Objects.isNull(attachFile.getFileName())) {
            return Boolean.TRUE;
        }
        LambdaUpdateWrapper<AttachFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(AttachFile::getFileId, attachFile.getFileId())
                .eq(attachFile.getAttachFileGroupId() != null, AttachFile::getAttachFileGroupId, attachFile.getAttachFileGroupId())
                .eq(attachFile.getFileName() != null, AttachFile::getFileName, attachFile.getFileName());
        attachFileMapper.update(lambdaUpdateWrapper);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long fileId) throws Exception {
        AttachFile attachFile = attachFileMapper.selectById(fileId);
        String filePath = attachFile.getFilePath();
        if (StrUtil.isNotBlank(filePath)) {
            filePath = filePath.substring(1);
        }
        minioTemplate.removeObject(filePath);
        attachFileMapper.deleteById(fileId);
    }
}




