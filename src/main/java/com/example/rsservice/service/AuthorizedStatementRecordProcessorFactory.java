package com.example.rsservice.service;

import com.example.rsservice.service.AuthorizedStatementRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

/**
 * Created by tnguyen on 11/20/18.
 */
public class AuthorizedStatementRecordProcessorFactory implements ShardRecordProcessorFactory {
    @Override
    public ShardRecordProcessor shardRecordProcessor() {
        return new AuthorizedStatementRecordProcessor();
    }
}
