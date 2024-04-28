package com.example.androidnote.model;

import com.example.androidnote.manager.BmobManager;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import java.util.List;

@Entity
public class RobotModel implements Serializable {
    private static final long serialVersionUID = 1L; // 或者你可以选择一个特定的值

    @Id(autoincrement = true)
    Long id;
    private String robotId;
    private String ownerId;
    private String title;
    private String desc;
    private String imageUrl;
    private String beginSay;
    private boolean isDel = false;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> questions;
    @NotNull
    long sendTime;
    @NotNull
    long createTime;
    @NotNull
    long updateTime;
    @Generated(hash = 543954103)
    public RobotModel(Long id, String robotId, String ownerId, String title,
            String desc, String imageUrl, String beginSay, boolean isDel,
            List<String> questions, long sendTime, long createTime,
            long updateTime) {
        this.id = id;
        this.robotId = robotId;
        this.ownerId = ownerId;
        this.title = title;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.beginSay = beginSay;
        this.isDel = isDel;
        this.questions = questions;
        this.sendTime = sendTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    @Generated(hash = 928486130)
    public RobotModel() {
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
    public String getOwnerId() {
        return this.ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getBeginSay() {
        return this.beginSay;
    }
    public void setBeginSay(String beginSay) {
        this.beginSay = beginSay;
    }
    public boolean getIsDel() {
        return this.isDel;
    }
    public void setIsDel(boolean isDel) {
        this.isDel = isDel;
    }
    public List<String> getQuestions() {
        return this.questions;
    }
    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
    public long getSendTime() {
        return this.sendTime;
    }
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
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
        return "RobotModel{" +
                "id=" + id +
                ", robotId='" + robotId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", beginSay='" + beginSay + '\'' +
                ", isDel=" + isDel +
                ", questions=" + questions +
                ", sendTime=" + sendTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Session createSession() {
        Session session = new Session();
        session.setUserId(BmobManager.getInstance().getObjectId());
        session.setName(this.title);
        session.setDesc(this.desc);
        session.setRobotId(this.robotId);
        session.setUrl(this.imageUrl);
        session.setIsDel(false);
        session.setCreateTime(System.currentTimeMillis());
        session.setUpdateTime(System.currentTimeMillis());
        return session;
    }
}
