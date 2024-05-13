package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class Comment {
    @Id(autoincrement = true)
    Long id;
    private String robotId;
    private String content;
    private String userId;
    @NotNull
    long createTime;
    @NotNull
    long updateTime;
    @Generated(hash = 6705288)
    public Comment(Long id, String robotId, String content, String userId,
            long createTime, long updateTime) {
        this.id = id;
        this.robotId = robotId;
        this.content = content;
        this.userId = userId;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    @Generated(hash = 1669165771)
    public Comment() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRobotId() {
        return this.robotId;
    }
    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
