package com.spring.what.biz.config;

import com.spring.what.common.exception.Mall4cloudException;
import com.spring.what.common.response.ResponseEnum;
import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;


@Component
public class MinioTemplate implements InitializingBean {

    static private final Logger logger = LoggerFactory.getLogger(MinioTemplate.class);

    @Resource
    private OssConfig ossConfig;

    private MinioClient minioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        minioClient = MinioClient.builder()
                .endpoint(ossConfig.getEndpoint())
                .credentials(ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret())
                .build();
    }

    /**
     * 删除文件
     *
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     */
    public void removeObject(String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(ossConfig.getBucket())
                .object(objectName)
                .build());
    }

    /**
     * 获得上传的URL
     *
     * @param objectName
     */
    public String getPresignedObjectUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(ossConfig.getBucket())
                    .object(objectName)
                    .expiry(10, TimeUnit.MINUTES)
                    .method(Method.POST)
                    .build());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Mall4cloudException(ResponseEnum.EXCEPTION);
        }
    }

    public void uploadMinio(InputStream inputStream, String filePath, String contentType) throws IOException {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(ossConfig.getBucket())
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .object(filePath)
                    .build());
        } catch (Exception e) {
            logger.error("上传文件出现错误", e);
        }
    }
}
