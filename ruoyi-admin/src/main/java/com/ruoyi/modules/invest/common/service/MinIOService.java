package com.ruoyi.modules.invest.common.service;

import io.minio.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * MinIO文件存储服务
 *
 * @author investvf
 */
@Service
public class MinIOService {

    private static final Logger log = LoggerFactory.getLogger(MinIOService.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket:investvf}")
    private String defaultBucket;

    /**
     * 创建bucket
     */
    public void createBucket(String bucketName) throws Exception {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 判断bucket是否存在
     */
    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param objectName 对象名称
     * @return 文件访问路径
     */
    public String uploadFile(MultipartFile file, String objectName) throws Exception {
        return uploadFile(file, defaultBucket, objectName);
    }

    /**
     * 上传文件到指定bucket
     */
    public String uploadFile(MultipartFile file, String bucketName, String objectName) throws Exception {
        createBucket(bucketName);
        InputStream inputStream = file.getInputStream();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build());
        return getFileUrl(bucketName, objectName);
    }

    /**
     * 删除文件
     */
    public void deleteFile(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    /**
     * 获取文件访问URL
     */
    public String getFileUrl(String bucketName, String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(7, TimeUnit.DAYS)
                    .build());
        } catch (Exception e) {
            log.error("获取文件URL失败", e);
            return null;
        }
    }

    /**
     * 获取文件输入流
     */
    public InputStream getFile(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }
}
