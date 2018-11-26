package com.example.rsservice.config;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by tnguyen on 11/19/18.
 */
@Configuration
public class AWSConfiguration {

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.streamname}")
    private String streamName;

    @Value("${cloud.aws.statementStream}")
    private String stmStreamName;

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public AmazonKinesis getAmazonKinesis() {
        AmazonKinesis kinesisClient = null;
        try {
            AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();

            clientBuilder.setRegion(this.region);
            clientBuilder.setCredentials(CredentialUtils.getCredentials(this.accessKey, this.secretKey));
            clientBuilder.setClientConfiguration(ConfigurationUtils.getClientConfigWithUserAgent());

            kinesisClient = clientBuilder.build();

        } catch (Exception e) {

        }
        return kinesisClient;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getRegion() {
        return region;
    }

    public String getStreamName() {
        return streamName;
    }

    public String getStmStreamName() {
        return stmStreamName;
    }

    public String getAppName() {
        return appName;
    }
}


