package com.mule.demo.common;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import io.minio.MinioClient;
import com.mule.demo.config.MinioConfig;

import cn.hutool.core.util.IdUtil;
import io.minio.PutObjectArgs;
import java.io.InputStream;
import com.mule.demo.exception.ServiceException;

@Slf4j
@Component
public class MinioUtils {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioConfig minioConfig;
    //上传文件
    public String upload(MultipartFile file) {
        try {
            // 获取原始文件名和后缀
          String originalFilename=file.getOriginalFilename();
          String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));
          //生成新的文件名
          String objectName=IdUtil.simpleUUID()+suffix;
         // 上传文件
         try(InputStream InputStream=file.getInputStream()){
             minioClient.putObject(
                     PutObjectArgs.builder()
                             .bucket(minioConfig.getBucketName())//存储桶名称
                             .object(objectName)//对象名称
                             .stream(InputStream, file.getSize(), -1)//文件流和文件大小
                             .contentType(file.getContentType())//文件类型
                             .build()
             );
        }
        // 返回文件访问路径
        return minioConfig.getEndpoint()+"/"+minioConfig.getBucketName()+"/"+objectName;
    } catch (Exception e) {
        log.error("file upload error: ", e);
        throw new ServiceException("file upload error");
        
    }

    }
}
