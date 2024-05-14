package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Star {
    @Id(autoincrement = true)
    Long id;
    String userId;
    String robotId;
    boolean isStar;
    @Generated(hash = 810798114)
    public Star(Long id, String userId, String robotId, boolean isStar) {
        this.id = id;
        this.userId = userId;
        this.robotId = robotId;
        this.isStar = isStar;
    }
    @Generated(hash = 249476133)
    public Star() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getRobotId() {
        return this.robotId;
    }
    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }
    public boolean getIsStar() {
        return this.isStar;
    }
    public void setIsStar(boolean isStar) {
        this.isStar = isStar;
    }
}
