package com.example.rsservice.service;

import com.example.rsservice.model.AuthorizedStatement;
import com.example.rsservice.model.StatementDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import software.amazon.kinesis.exceptions.InvalidStateException;
import software.amazon.kinesis.exceptions.ShutdownException;
import software.amazon.kinesis.lifecycle.events.*;
import software.amazon.kinesis.processor.ShardRecordProcessor;

/**
 * Created by tnguyen on 11/20/18.
 */
public class StatementDetailRecordProcessor implements ShardRecordProcessor {

    private static final Logger log = LoggerFactory.getLogger(StatementDetailRecordProcessor.class);

    private static final String SHARD_ID_MDC_KEY = "ShardId";

    private String shardId;

    @Override
    public void initialize(InitializationInput initializationInput) {
        this.shardId = initializationInput.shardId();
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Initializing @ Sequence: {}", initializationInput.extendedSequenceNumber());
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }

    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput) {
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Processing {} record(s)", processRecordsInput.records().size());
            processRecordsInput.records().forEach(r -> {
                log.info("Processing record pk: {} -- Seq: {}", r.partitionKey(), r.sequenceNumber());

                final byte[] bytes = new byte[r.data().remaining()];
                r.data().get(bytes);

                StatementDetail statementDetail = StatementDetail.fromJsonAsBytes(bytes);
                log.info("************* RECEIVED FROM STATEMENT SERVICE **********");
                log.info("StatementId: " + statementDetail.getStatementId() + " PUN: " + statementDetail.getStatementId() + "Amount: " + statementDetail.getAmount());

            });
        } catch (Throwable t) {
            log.error("Caught throwable while processing records.  Aborting");
            //Runtime.getRuntime().halt(1);
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }

    @Override
    public void leaseLost(LeaseLostInput leaseLostInput) {
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Lost lease, so terminating.");
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }

    @Override
    public void shardEnded(ShardEndedInput shardEndedInput) {
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Reached shard end checkpointing.");
            shardEndedInput.checkpointer().checkpoint();
        } catch (ShutdownException | InvalidStateException e) {
            log.error("Exception while checkpointing at shard end.  Giving up", e);
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }

    @Override
    public void shutdownRequested(ShutdownRequestedInput shutdownRequestedInput) {
        MDC.put(SHARD_ID_MDC_KEY, shardId);
        try {
            log.info("Scheduler is shutting down, checkpointing.");
            shutdownRequestedInput.checkpointer().checkpoint();
        } catch (ShutdownException | InvalidStateException e) {
            log.error("Exception while checkpointing at requested shutdown.  Giving up", e);
        } finally {
            MDC.remove(SHARD_ID_MDC_KEY);
        }
    }
}
