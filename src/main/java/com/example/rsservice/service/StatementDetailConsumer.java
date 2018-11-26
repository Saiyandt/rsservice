package com.example.rsservice.service;

import com.example.rsservice.config.AWSConfiguration;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.kinesis.common.ConfigsBuilder;
import software.amazon.kinesis.common.InitialPositionInStream;
import software.amazon.kinesis.common.InitialPositionInStreamExtended;
import software.amazon.kinesis.coordinator.Scheduler;
import software.amazon.kinesis.retrieval.RetrievalConfig;
import software.amazon.kinesis.retrieval.polling.PollingConfig;

import java.util.UUID;

/**
 * Created by tnguyen on 11/20/18.
 */
public class StatementDetailConsumer {
    private static final Logger log = LoggerFactory.getLogger(StatementDetailConsumer.class);

    @Autowired
    AWSConfiguration awsConfiguration;

    private KinesisAsyncClient kinesisClient;
    private Region region;
    private  String streamName;
    private Scheduler scheduler;
    private String appName;


    public void init() throws Exception {
        log.info("***************** START Statement Detail Consumer *********");
        this.streamName = awsConfiguration.getStmStreamName();
        this.appName = awsConfiguration.getAppName() + "stmConsumer";
        this.region = Region.of(ObjectUtils.firstNonNull(awsConfiguration.getRegion(), "us-east-2"));


        this.kinesisClient = KinesisAsyncClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(this.region)
                .build();

        DynamoDbAsyncClient dynamoClient = DynamoDbAsyncClient.builder().region(region).build();
        CloudWatchAsyncClient cloudWatchClient = CloudWatchAsyncClient.builder().region(region).build();
        ConfigsBuilder configsBuilder = new ConfigsBuilder(streamName, appName, kinesisClient, dynamoClient, cloudWatchClient, UUID.randomUUID().toString(), new StatementDetailRecordProcessorFactory());

        RetrievalConfig tmp = new RetrievalConfig(kinesisClient, streamName, appName);
        tmp.initialPositionInStreamExtended(InitialPositionInStreamExtended.newInitialPosition(InitialPositionInStream.TRIM_HORIZON));


        this.scheduler = new Scheduler(
                configsBuilder.checkpointConfig(),
                configsBuilder.coordinatorConfig(),
                configsBuilder.leaseManagementConfig(),
                configsBuilder.lifecycleConfig(),
                configsBuilder.metricsConfig(),
                configsBuilder.processorConfig(),
                tmp.retrievalSpecificConfig(new PollingConfig(streamName, kinesisClient))
        );

        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.setDaemon(true);
        schedulerThread.start();

    }
}
