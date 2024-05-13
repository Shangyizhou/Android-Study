package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Advice {
    @Id(autoincrement = true)
    Long id;
    private String question;
    private String advice;
    private String userId;
    private long bookId;
    @Generated(hash = 988294051)
    public Advice(Long id, String question, String advice, String userId,
            long bookId) {
        this.id = id;
        this.question = question;
        this.advice = advice;
        this.userId = userId;
        this.bookId = bookId;
    }
    @Generated(hash = 1718511272)
    public Advice() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getQuestion() {
        return this.question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public String getAdvice() {
        return this.advice;
    }
    public void setAdvice(String advice) {
        this.advice = advice;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public long getBookId() {
        return this.bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

}
