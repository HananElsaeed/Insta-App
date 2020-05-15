package com.hananelsaid.hp.instaapp.loginpackage.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.hananelsaid.hp.instaapp.CheckConnection;
import com.hananelsaid.hp.instaapp.MainActivity;
import com.hananelsaid.hp.instaapp.R;
import com.hananelsaid.hp.instaapp.loginpackage.viewmodel.LoginViewModel;
import com.hananelsaid.hp.instaapp.signuppackage.view.SignUpActivity;
import com.hananelsaid.hp.instaapp.signuppackage.viewmodel.SignUpViewModel;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "TAGLoginActivity";
    //views refrences
    TextInputEditText edLoginEmail, edLoginPassword;
    Button btnLogin;
    TextView tvSignUp;
    ImageView ivGoogle;

    //viewModel class refrence
    LoginViewModel viewModel;

    //
    String email, password;
    //firebase
    private GoogleSignInClient client;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edLoginEmail = findViewById(R.id.etloginemail);
        edLoginPassword = findViewById(R.id.etloginpassword);
        ivGoogle = findViewById(R.id.ivGoogle);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        viewModel = ViewModelProviders.of(this, new LoginViewModelFactory(this)).get(LoginViewModel.class);

        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, gso);


    }

    @Override
    protected void onStart() {
        super.onStart();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = edLoginEmail.getText().toString().trim();
                password = edLoginPassword.getText().toString().trim();
                loginUser();
                if (CheckConnection.isNetworkConnected(LoginActivity.this)) {
                    if (!email.isEmpty() && !password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(edLoginEmail.getText().toString()).matches()) {
                        viewModel.login(email, password);
                    }

                } else display("Pleas check your internet Connection ");

            }
        });

        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckConnection.isNetworkConnected(LoginActivity.this)) {
                    signUpWithGoogle();
                } else display("Pleas check your internet Connection ");

            }
        });

    }

    private void signUpWithGoogle() {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Sign In Faild" + e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            openPostsActivity();
        } else {
            Toast.makeText(LoginActivity.this, "Faild", Toast.LENGTH_SHORT).show();

        }
    }

    void loginUser() {
        if (email.isEmpty()) {
            edLoginEmail.setError("Please enter the email");
            edLoginEmail.requestFocus();
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edLoginEmail.getText().toString()).matches()) {
            edLoginEmail.setError("Please enter valid email");
            edLoginEmail.requestFocus();
        }
        if (password.isEmpty()) {
            edLoginPassword.setError("Please enter the password");
            edLoginPassword.requestFocus();
        }


    }

    public void display(String message) {
        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
    }

    public void openPostsActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        this.finish();
    }

    private void register() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        this.finish();
    }

    class LoginViewModelFactory implements ViewModelProvider.Factory {

        LoginActivity loginActivity;

        LoginViewModelFactory(LoginActivity loginActivity) {
            this.loginActivity = loginActivity;

        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

            return (T) new LoginViewModel(loginActivity);
        }
    }
}
