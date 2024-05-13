package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Book {
    @Id(autoincrement = true)
    Long id;
    private String name;
    String userId;
    @Generated(hash = 640755002)
    public Book(Long id, String name, String userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }
    @Generated(hash = 1839243756)
    public Book() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

}