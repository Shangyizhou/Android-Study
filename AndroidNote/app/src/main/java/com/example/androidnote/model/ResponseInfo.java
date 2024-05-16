package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ResponseInfo {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String requestId;
    @NotNull
    private long requestTime;
    @NotNull
    private long responseTime;
    @NotNull
    private String userId;
    @NotNull
    private long costToken;

    @Generated(hash = 118974817)
    public ResponseInfo(Long id, @NotNull String requestId, long requestTime,
            long responseTime, @NotNull String userId, long costToken) {
        this.id = id;
        this.requestId = requestId;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.userId = userId;
        this.costToken = costToken;
    }

    @Generated(hash = 1558055219)
    public ResponseInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }


    public long getCostToken() {
        return this.costToken;
    }

    public void setCostToken(long costToken) {
        this.costToken = costToken;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
