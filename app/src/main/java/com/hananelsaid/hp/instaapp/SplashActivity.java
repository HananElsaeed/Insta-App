package com.hananelsaid.hp.instaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hananelsaid.hp.instaapp.loginpackage.view.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser != null&&currentUser.isEmailVerified()) {
                    openPostsActivity();
                }
                else openLoginActivity();
            }

        },1000);

    }
    public void openPostsActivity(){
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        this.finish();
    }
    public void openLoginActivity(){
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        this.finish();
    }
}
