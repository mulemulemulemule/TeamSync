package com.mule.demo.service.impl;

import com.mule.demo.common.MinioUtils;
import com.mule.demo.exception.ServiceException;
import com.mule.demo.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioServiceImpl implements MinioService {
    @Autowired
    private MinioUtils minioUtils;

    @Override
    public String upload(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new ServiceException("file is empty");
        return minioUtils.upload(file);
    }

    @Override
    public void delete(String url) {
        if (url != null && !url.isEmpty()) minioUtils.removeFile(url);
    }
}
