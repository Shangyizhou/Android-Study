package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserInfo {

    @Id(autoincrement = true)
    private Long id;
    private String imageFilePath = "";
    @NotNull
    private String userName = "";

    //性别 true = 男 false = 女
    private boolean sex = true;
    //简介
    private String desc = "";
    //年龄
    private int age = 0;
    //生日
    private String birthday = "";
    //星座
    private String constellation = "";
    //爱好
    private String hobby = "";

    @Generated(hash = 1426943254)
    public UserInfo(Long id, String imageFilePath, @NotNull String userName,
            boolean sex, String desc, int age, String birthday,
            String constellation, String hobby) {
        this.id = id;
        this.imageFilePath = imageFilePath;
        this.userName = userName;
        this.sex = sex;
        this.desc = desc;
        this.age = age;
        this.birthday = birthday;
        this.constellation = constellation;
        this.hobby = hobby;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", imageFilePath='" + imageFilePath + '\'' +
                ", userName='" + userName + '\'' +
                ", sex=" + sex +
                ", desc='" + desc + '\'' +
                ", age=" + age +
                ", birthday='" + birthday + '\'' +
                ", constellation='" + constellation + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }

    public boolean getSex() {
        return this.sex;
    }
}
