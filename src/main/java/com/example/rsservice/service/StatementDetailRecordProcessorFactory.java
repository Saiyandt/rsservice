package com.example.rsservice.service;

import com.example.rsservice.service.StatementDetailRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessor;
import software.amazon.kinesis.processor.ShardRecordProcessorFactory;

/**
 * Created by tnguyen on 11/20/18.
 */
public class StatementDetailRecordProcessorFactory implements ShardRecordProcessorFactory {
    @Override
    public ShardRecordProcessor shardRecordProcessor() {
        return new StatementDetailRecordProcessor();
    }
}
