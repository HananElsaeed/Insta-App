package com.hananelsaid.hp.instaapp.signuppackage.model;

public class UserData {
    String userID;
    String userEmail;
    String usrPassword;
    String userConPassword;
    //String userName;

    public UserData() {
    }

    public UserData(String userID, String userEmail, String usrPassword, String userConPassword) {
        this.userID = userID;
        this.userEmail = userEmail;
        this.usrPassword = usrPassword;
        this.userConPassword = userConPassword;
        //this.userName = userName;
    }
}
