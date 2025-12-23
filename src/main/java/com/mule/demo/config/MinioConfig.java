package com.mule.demo.config;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "minio")//读取配置文件
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    @Bean
    public MinioClient minioClient(){
        try {
            // 创建 MinioClient 对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
                    // 检查桶是否存在，如果不存在则创建
         boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if(!found) {
                log.info("Bucket '{}' does not exist. Creating...", bucketName);
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                //设置桶的访问策略为公共读取
                 String policy = """
                           {
                           "Version": "2012-10-17",
                             "Statement": [
                             {
                                 "Effect": "Allow",
                                 "Principal": {"AWS": ["*"]},
                                "Action": ["s3:GetObject"],
                               "Resource": ["arn:aws:s3:::%s/*"]
                            }
                           ]
                       }
                        """.formatted(bucketName);
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
                log.info("Bucket '{}' created successfully with public read access.", bucketName);
            } else {
                log.info("Bucket '{}' already exists.", bucketName);

            }
            return minioClient;
    }
         catch (Exception e) {
            log.error("Error initializing MinioClient: ", e);
            throw new RuntimeException(e);
}
}
}