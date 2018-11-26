package com.example.rsservice.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by tnguyen on 11/19/18.
 */
public class StatementDetail {

    private final static ObjectMapper JSON = new ObjectMapper();
    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String statementId;
    private String pun;
    private String amount;

    public StatementDetail() {

    }

    public StatementDetail(String statementId, String pun, String amount) {
        this.statementId = statementId;
        this.pun = pun;
        this.amount = amount;
    }

    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            return null;
        }
    }

    public static StatementDetail fromJsonAsBytes(byte[] bytes) {
        try {
            return JSON.readValue(bytes, StatementDetail.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("StatementId: %s pun: %s amount: %s ",
                statementId, pun, amount);
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getPun() {
        return pun;
    }

    public void setPun(String pun) {
        this.pun = pun;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
