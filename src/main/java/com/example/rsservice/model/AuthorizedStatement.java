package com.example.rsservice.model;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by tnguyen on 11/20/18.
 */
public class AuthorizedStatement {
    private final static ObjectMapper JSON = new ObjectMapper();
    static {
        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String statementId;
    private String pun;

    public AuthorizedStatement() {

    }

    public AuthorizedStatement(String statementId, String pun) {
        this.statementId = statementId;
        this.pun = pun;
    }

    public byte[] toJsonAsBytes() {
        try {
            return JSON.writeValueAsBytes(this);
        } catch (IOException e) {
            return null;
        }
    }

    public static AuthorizedStatement fromJsonAsBytes(byte[] bytes) {
        try {
            return JSON.readValue(bytes, AuthorizedStatement.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("StatementId: %s pun: %s ",
                statementId, pun);
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

}
