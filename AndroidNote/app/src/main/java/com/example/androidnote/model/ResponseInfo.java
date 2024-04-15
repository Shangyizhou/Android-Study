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
    private String userName;
    @NotNull
    private String modelName;

    @Generated(hash = 1758000325)
    public ResponseInfo(Long id, @NotNull String requestId, long requestTime,
            long responseTime, @NotNull String userName,
            @NotNull String modelName) {
        this.id = id;
        this.requestId = requestId;
        this.requestTime = requestTime;
        this.responseTime = responseTime;
        this.userName = userName;
        this.modelName = modelName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return "ResponseInfo{" +
                "id=" + id +
                ", requestId='" + requestId + '\'' +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                ", userName='" + userName + '\'' +
                ", modelName='" + modelName + '\'' +
                '}';
    }
}
