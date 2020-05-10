package com.hananelsaid.hp.instaapp.profilepackage.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hananelsaid.hp.instaapp.profilepackage.viewmodel.ProfileViewModel;

import java.util.Objects;

public class UploadUserRepo {
    private StorageReference storageReference;
    private DatabaseReference databaseUsers;
    private FirebaseAuth mAuth;
    private String userId;
    // private FirebaseDatabase database;
    private ProfileViewModel profileViewModel;


    public UploadUserRepo(ProfileViewModel profileViewModel) {
        this.profileViewModel = profileViewModel;
        mAuth=FirebaseAuth.getInstance();

        userId= mAuth.getCurrentUser().getUid();

    }

    public void loadDataToFirebase(final Uri uri, final String name, final String phone) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                storageReference = FirebaseStorage.getInstance().getReference().child("profile_image");
                databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
                final StorageReference imagePath = storageReference.child(uri.getLastPathSegment());
                Task<Uri> urlTask = imagePath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continue with the task to get the download URL
                        Task<Uri> downloadUrl = imagePath.getDownloadUrl();

                        return downloadUrl;
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            String downloadURL = downloadUri.toString();
                            // String url2 = downloadUrl.toString();
                            Log.i("TAG", downloadURL);
                            DatabaseReference newUser = databaseUsers;
                            newUser.child(userId).child("image").setValue(downloadURL);
                            newUser.child(userId).child("name").setValue(name);
                            newUser.child(userId).child("phone").setValue(phone);


                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            }
        }.start();

    }

}
