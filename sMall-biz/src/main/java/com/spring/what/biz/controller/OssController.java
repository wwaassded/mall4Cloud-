package com.spring.what.biz.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.spring.what.biz.config.MinioTemplate;
import com.spring.what.biz.config.OssConfig;
import com.spring.what.biz.constant.OssType;
import com.spring.what.biz.vo.OssVO;
import com.spring.what.common.response.ServerResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@RequestMapping(value = "/oss")
@RestController
@Tag(name = "文件管理")
public class OssController {

    private static final Logger log = LoggerFactory.getLogger(OssController.class);

    /**
     * 上传的文件夹(根据时间确定)
     */
    public static final String NORM_DAY_PATTERN = "yyyy/MM/dd";

    @Resource
    private OssConfig ossConfig;

    @Resource
    private MinioTemplate minioTemplate;


    @GetMapping(value = "/info")
    @Operation(summary = "token", description = "获取文件上传需要的token")
    @Parameter(name = "fileNum", description = "需要获取token的文件数量")
    public ServerResponseEntity<OssVO> info(@RequestParam("fileNum") Integer fileNum) {
        OssVO ossVO = new OssVO();
        if (Objects.equals(ossConfig.getOssType(), OssType.MINIO.value())) {
            fillMinIoInfo(ossVO, fileNum);
        }
        return ServerResponseEntity.success(ossVO);
    }

    private void fillMinIoInfo(OssVO ossVo, Integer fileNum) {
        List<OssVO> ossVOS = new ArrayList<>(fileNum);
        for (int i = 0; i < fileNum; ++i) {
            OssVO urlOss = loadOssVO(new OssVO());
            String presignedObjectUrl = minioTemplate.getPresignedObjectUrl(urlOss.getFileName());
            urlOss.setActionUrl(presignedObjectUrl);
            ossVOS.add(urlOss);
        }
        ossVo.setOssList(ossVOS);
    }

    private OssVO loadOssVO(OssVO ossVo) {
        String dir = DateUtil.format(new Date(), NORM_DAY_PATTERN) + "/";
        String fileName = IdUtil.fastSimpleUUID();
        ossVo.setDir(dir);
        ossVo.setFileName(fileName);
        return ossVo;
    }


    @PostMapping("/upload_minio")
    @Operation(summary = "文件上传接口", description = "上传文件，返回文件路径与域名")
    public ServerResponseEntity<OssVO> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ServerResponseEntity.success();
        }
        OssVO ossVO = loadOssVO(new OssVO());
        try (InputStream inputStream = file.getInputStream();) {
            minioTemplate.uploadMinio(inputStream, ossVO.getDir() + ossVO.getFileName(), file.getContentType());
        }
        return ServerResponseEntity.success(ossVO);
    }


}