package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Fans {
    @Id(autoincrement = true)
    Long id;
    String userId;
    String robotId;
    boolean isFans;
    @Generated(hash = 1854415506)
    public Fans(Long id, String userId, String robotId, boolean isFans) {
        this.id = id;
        this.userId = userId;
        this.robotId = robotId;
        this.isFans = isFans;
    }
    @Generated(hash = 918233287)
    public Fans() {
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
    public boolean getIsFans() {
        return this.isFans;
    }
    public void setIsFans(boolean isFans) {
        this.isFans = isFans;
    }
}
