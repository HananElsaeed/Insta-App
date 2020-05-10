package com.hananelsaid.hp.instaapp.profilepackage.model;

public class User {
    String name ,profileImageUrl,phone;

    public User() {
    }

    public User(String userName, String profileImage) {
        this.name=userName;
        this.profileImageUrl=profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(String name, String profileImageUrl, String phone) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.phone = phone;
    }
}
