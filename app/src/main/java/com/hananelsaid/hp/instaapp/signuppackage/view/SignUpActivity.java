package com.hananelsaid.hp.instaapp.signuppackage.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.hananelsaid.hp.instaapp.CheckConnection;
import com.hananelsaid.hp.instaapp.MainActivity;
import com.hananelsaid.hp.instaapp.R;
import com.hananelsaid.hp.instaapp.loginpackage.view.LoginActivity;
import com.hananelsaid.hp.instaapp.signuppackage.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    String email, password, confirmPassword;
    //views refrences
    TextInputEditText etEmail, etPassword, etConfirmPass;
    Button btnSignUp;
    TextView tvlogin;

    //firebase
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuth;
    //view model
    SignUpViewModel viewModelRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
     /*   mAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                openPostsActivity();
            }
        };*/

        //views objects
        etEmail = findViewById(R.id.etemail);
        etPassword = findViewById(R.id.etpassword);
        etConfirmPass = findViewById(R.id.etconfirmpassword);
        tvlogin = findViewById(R.id.tvopenLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        //viewModel
        viewModelRef = ViewModelProviders.of(this, new SignViewModelFactory(this)).get(SignUpViewModel.class);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                confirmPassword = etConfirmPass.getText().toString().trim();
                singnUpUser();
                if (CheckConnection.isNetworkConnected(SignUpActivity.this)) {
                    if (!email.isEmpty() && !password.isEmpty() &&
                            !confirmPassword.isEmpty() && password.equals(confirmPassword) &&
                            Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                        viewModelRef.signUp(email, password);
                    }

                } else display("Pleas check your internet Connection ");
            }
        });

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openLoginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(openLoginIntent);
                SignUpActivity.this.finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
       // auth.addAuthStateListener(mAuth);
    }

    private void singnUpUser() {


        if (email.isEmpty()) {
            etEmail.setError("Please enter the email");
            etEmail.requestFocus();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Please enter valid email");
            etEmail.requestFocus();
        }
        if (password.isEmpty()) {

            etPassword.setError("Please enter the password");
            etPassword.requestFocus();
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPass.setError("Not the same password");
            etConfirmPass.requestFocus();

        }


    }

    public void display(String message) {
        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
    }

    public void openLoginActivity() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        this.finish();
    }

    public void openPostsActivity() {
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        this.finish();
    }

    class SignViewModelFactory implements ViewModelProvider.Factory {

        SignUpActivity signUpctivity;

        SignViewModelFactory(SignUpActivity signUpctivity) {
            this.signUpctivity = signUpctivity;

        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

            return (T) new SignUpViewModel(signUpctivity);
        }
    }
}
