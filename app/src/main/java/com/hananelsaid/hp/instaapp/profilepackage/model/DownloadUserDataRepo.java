package com.hananelsaid.hp.instaapp.profilepackage.model;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hananelsaid.hp.instaapp.postspackage.viewmodel.PostViewModel;
import com.hananelsaid.hp.instaapp.profilepackage.viewmodel.ProfileViewModel;

public class DownloadUserDataRepo {
    private DatabaseReference databaseUser;
    private MutableLiveData<User> liveData;
    private ProfileViewModel viewModel;

    public DownloadUserDataRepo(ProfileViewModel viewModel) {
        this.viewModel = viewModel;
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserData() {
        databaseUser = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        new Thread() {
            @Override
            public void run() {
                super.run();
                // Read from the database

                databaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user;

                        Object image = dataSnapshot.child("image").getValue();
                        Object name = dataSnapshot.child("name").getValue();
                        Object phone = dataSnapshot.child("phone").getValue();
                        if (image != null && name != null && phone != null) {
                            String profileImage = image.toString();
                            String userName = name.toString();
                            String userPhone = phone.toString();

                            //Log.i("TAG", "Value is: " + profileImage);
                            user = new User(userName, profileImage, userPhone);


                            liveData.setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

            }
        }.start();
        return liveData;
    }

}
