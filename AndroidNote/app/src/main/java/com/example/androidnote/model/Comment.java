package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Comment {
    @Id(autoincrement = true)
    Long id;
    private String robotId;
    private String content;
    private String userId;
    @Generated(hash = 502692624)
    public Comment(Long id, String robotId, String content, String userId) {
        this.id = id;
        this.robotId = robotId;
        this.content = content;
        this.userId = userId;
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
}
