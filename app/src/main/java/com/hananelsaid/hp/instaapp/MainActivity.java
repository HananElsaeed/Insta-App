package com.hananelsaid.hp.instaapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.hananelsaid.hp.instaapp.loginpackage.view.LoginActivity;
import com.hananelsaid.hp.instaapp.newpostpackage.view.Post_Activity;
import com.hananelsaid.hp.instaapp.profilepackage.view.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth=FirebaseAuth.getInstance();
      /*  NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
        navController.navigate(R.id.FirstFragment);*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            startActivity(new Intent(MainActivity.this, Profile.class));
        }
      /*  if (id == R.id.add_button) {
        *//*    NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
            navController.navigate(R.id.SecondFragment);*//*

        }*/
        if (id==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            openLoginActivity();
        }


        return super.onOptionsItemSelected(item);
    }
    public void openLoginActivity(){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        this.finish();
    }
}
