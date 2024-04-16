package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

// 和什么接口的大模型进行交流无需message来管理，交给session即可
@Entity
public class Message {
    @Id(autoincrement = true)
    Long id;
    /**
     * 0 robot
     * 1 person
     */
    @NotNull
    int type;
    /**
     * 按照messageId排序，不能使用UUID
     */
    @NotNull
    String messageId;

    /**
     * 可以直接使用UUID
     */
    @NotNull
    String sessionId;
    @NotNull
    String message;
    @NotNull
    long sendTime;
    @Generated(hash = 691201656)
    public Message(Long id, int type, @NotNull String messageId,
            @NotNull String sessionId, @NotNull String message, long sendTime) {
        this.id = id;
        this.type = type;
        this.messageId = messageId;
        this.sessionId = sessionId;
        this.message = message;
        this.sendTime = sendTime;
    }
    @Generated(hash = 637306882)
    public Message() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getMessageId() {
        return this.messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getSessionId() {
        return this.sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getSendTime() {
        return this.sendTime;
    }
    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }
}
