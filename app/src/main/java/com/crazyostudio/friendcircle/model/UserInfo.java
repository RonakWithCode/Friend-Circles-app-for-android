package com.crazyostudio.friendcircle.model;


public class UserInfo {
    private String name,bio,userImage,userid,LastStatus,mail,password,ImageName;

    public UserInfo(String name, String bio, String userImage, String mail, String password) {
        this.name = name;
        this.bio = bio;
        this.userImage = userImage;
        this.mail = mail;
        this.password = password;
    }

    public UserInfo(String name, String bio, String userImage,String mail, String password, String imageName) {
        this.name = name;
        this.bio = bio;
        this.userImage = userImage;
        this.mail = mail;
        this.password = password;
        ImageName = imageName;
    }

    public UserInfo(){}


    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getLastStatus() {
        return LastStatus;
    }

    public void setLastStatus(String lastStatus) {
        LastStatus = lastStatus;
    }
}


