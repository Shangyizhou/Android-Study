package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class Session {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String sessionId;
    @NotNull
    private String name; // title
    @NotNull
    private String desc; // 描述
    @NotNull
    private String robotId; // 暂不使用，robotId应该是标识各个机器人应用的ID 类似于 applicationId
    @NotNull
    private String userId; // 标注聊天用户，应该与IMUser打通
    @NotNull
    private String url; // 访问的文心接口
    @NotNull
    private boolean isDel; // 是否不使用此历史会话
    @NotNull
    private long createTime;
    @NotNull
    private long updateTime;
    @Generated(hash = 945606436)
    public Session(Long id, @NotNull String sessionId, @NotNull String name,
            @NotNull String desc, @NotNull String robotId, @NotNull String userId,
            @NotNull String url, boolean isDel, long createTime, long updateTime) {
        this.id = id;
        this.sessionId = sessionId;
        this.name = name;
        this.desc = desc;
        this.robotId = robotId;
        this.userId = userId;
        this.url = url;
        this.isDel = isDel;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    @Generated(hash = 1317889643)
    public Session() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSessionId() {
        return this.sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getRobotId() {
        return this.robotId;
    }
    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public boolean getIsDel() {
        return this.isDel;
    }
    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
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

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", robotId='" + robotId + '\'' +
                ", userId='" + userId + '\'' +
                ", url='" + url + '\'' +
                ", isDel=" + isDel +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
