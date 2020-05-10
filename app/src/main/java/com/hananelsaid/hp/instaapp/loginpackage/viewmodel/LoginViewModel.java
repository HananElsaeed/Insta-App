package com.hananelsaid.hp.instaapp.loginpackage.viewmodel;

import androidx.lifecycle.ViewModel;

import com.hananelsaid.hp.instaapp.loginpackage.model.LoginRepository;
import com.hananelsaid.hp.instaapp.loginpackage.view.LoginActivity;

public class LoginViewModel extends ViewModel {
    LoginActivity loginActivity;
    LoginRepository repository;

    public LoginViewModel(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        repository = new LoginRepository(this);

    }

    public void login(String email, String password) {
        repository.login(email, password);
    }

    public void display(String message) {
        loginActivity.display(message);
    }

    public void loginSuccessfully() {
        loginActivity.openPostsActivity();
    }
}
