package com.example.androidnote.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import cn.bmob.v3.BmobUser;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class IMUser extends BmobUser {
    @Id
    Long id;
    //昵称
    private String userName = "";
    //头像
    private String photo = "";
    private String schoolName = "";
    private String className = "";

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

    @Generated(hash = 1272465326)
    public IMUser(Long id, String userName, String photo, String schoolName,
            String className, boolean sex, String desc, int age, String birthday,
            String constellation, String hobby) {
        this.id = id;
        this.userName = userName;
        this.photo = photo;
        this.schoolName = schoolName;
        this.className = className;
        this.sex = sex;
        this.desc = desc;
        this.age = age;
        this.birthday = birthday;
        this.constellation = constellation;
        this.hobby = hobby;
    }

    @Generated(hash = 931211978)
    public IMUser() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
        return "UserBean{" +
                "userName='" + userName + '\'' +
                ", photo='" + photo + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", className='" + className + '\'' +
                ", sex=" + sex +
                ", desc='" + desc + '\'' +
                ", age=" + age +
                ", birthday='" + birthday + '\'' +
                ", constellation='" + constellation + '\'' +
                ", hobby='" + hobby + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getSex() {
        return this.sex;
    }
}
