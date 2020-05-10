package com.hananelsaid.hp.instaapp.loginpackage.model;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.hananelsaid.hp.instaapp.R;
import com.hananelsaid.hp.instaapp.loginpackage.viewmodel.LoginViewModel;

public class LoginRepository {

    LoginViewModel loginViewModel;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    public LoginRepository(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        mAuth = FirebaseAuth.getInstance();

    }

    private void verifyEmail() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null&&currentUser.isEmailVerified()) {
            loginViewModel.loginSuccessfully();
        } else
            loginViewModel.display("you should verify your email");
    }

    public void login(final String email, final String password) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            verifyEmail();
                        }
                        else loginViewModel.display(task.getException().getMessage().toString());
                    }
                });
            }
        }.start();

    }

}
