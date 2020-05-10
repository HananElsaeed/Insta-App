package com.hananelsaid.hp.instaapp.signuppackage.viewmodel;

import androidx.lifecycle.ViewModel;

import com.hananelsaid.hp.instaapp.signuppackage.model.SignUpRepository;
import com.hananelsaid.hp.instaapp.signuppackage.view.SignUpActivity;

public class SignUpViewModel extends ViewModel {
    SignUpActivity viewRef;
    SignUpRepository repoRef = null;

    public SignUpViewModel(SignUpActivity viewRef) {
        this.viewRef = viewRef;
        repoRef =new SignUpRepository(this);
    }


    public void signUp(String email, String password) {
        repoRef.signUp(email, password);
    }


    public void display(String messages) {
        viewRef.display(messages);
    }

    public void openLoginActivity() {
        viewRef.openLoginActivity();
    }
}
