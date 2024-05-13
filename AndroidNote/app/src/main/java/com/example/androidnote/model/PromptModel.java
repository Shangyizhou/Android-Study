package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity

public class PromptModel {
    @Id(autoincrement = true)
    Long id;
    String robotId;
    String name;
    String content;
    long createTime;
    long updateTime;
    @Generated(hash = 830030553)
    public PromptModel(Long id, String robotId, String name, String content,
            long createTime, long updateTime) {
        this.id = id;
        this.robotId = robotId;
        this.name = name;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    @Generated(hash = 384470688)
    public PromptModel() {
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
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
