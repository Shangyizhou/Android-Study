package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @NotNull 不能为空
 */
@Entity
public class User {

    /**
     * 自动增长
     */
    @Id(autoincrement = true)
    private Long _id;
    private int dr;
    private String headUrl;
    private String obj;

    @NotNull
    private String name;
    private String auth_token;
    @NotNull
    private String account;
    @NotNull
    private String password;
    @Generated(hash = 1303641330)
    public User(Long _id, int dr, String headUrl, String obj, @NotNull String name,
            String auth_token, @NotNull String account, @NotNull String password) {
        this._id = _id;
        this.dr = dr;
        this.headUrl = headUrl;
        this.obj = obj;
        this.name = name;
        this.auth_token = auth_token;
        this.account = account;
        this.password = password;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long get_id() {
        return this._id;
    }
    public void set_id(Long _id) {
        this._id = _id;
    }
    public int getDr() {
        return this.dr;
    }
    public void setDr(int dr) {
        this.dr = dr;
    }
    public String getHeadUrl() {
        return this.headUrl;
    }
    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
    public String getObj() {
        return this.obj;
    }
    public void setObj(String obj) {
        this.obj = obj;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuth_token() {
        return this.auth_token;
    }
    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id=" + _id +
                ", dr=" + dr +
                ", headUrl='" + headUrl + '\'' +
                ", obj='" + obj + '\'' +
                ", name='" + name + '\'' +
                ", auth_token='" + auth_token + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}


