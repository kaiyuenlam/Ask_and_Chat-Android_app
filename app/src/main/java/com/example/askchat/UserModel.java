package com.example.askchat;

public class UserModel {

    public String userName, email, education, college, uid, encodedUserIcon;

    public UserModel() {

    }

    public UserModel(String userName, String email, String uid) {
        this.userName = userName;
        this.email = email;
        this.uid = uid;
        this.education = "";
        this.college = "";
        this.encodedUserIcon = "";
    }

    public UserModel(String userName, String email, String uid, String education, String college, String encodedUserIcon) {
        this.userName = userName;
        this.email = email;
        this.uid = uid;
        this.education = education;
        this.college = college;
        this.encodedUserIcon = encodedUserIcon;
    }

    public String getUserName() {
        return userName;
    }

    public String getEncodedUserIcon() {
        return encodedUserIcon;
    }

    public void setEncodedUserIcon(String encodedUserIcon) {
        this.encodedUserIcon = encodedUserIcon;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getuid() {
        return uid;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }
}
