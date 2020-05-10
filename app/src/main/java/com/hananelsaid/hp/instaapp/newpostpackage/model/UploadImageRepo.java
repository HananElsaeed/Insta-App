package com.hananelsaid.hp.instaapp.newpostpackage.model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hananelsaid.hp.instaapp.newpostpackage.viewmodel.NewPostViewModel;

import java.util.Objects;

public class UploadImageRepo {

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseUsers;
    private FirebaseUser currentUser;
    // private FirebaseDatabase database;
    private NewPostViewModel newPostViewModel;


    public UploadImageRepo(NewPostViewModel newPostViewModel) {
        this.newPostViewModel = newPostViewModel;
    }

    public void loadImageToFirebase(final Uri uri, final String title, final String description, final String child) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                storageReference = FirebaseStorage.getInstance().getReference("Posts_images");
                databaseReference = FirebaseDatabase.getInstance().getReference("InstaApp");
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseUsers = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
                final StorageReference imagePath = storageReference.child(child);

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
                            final String downloadURL = downloadUri.toString();
                            Log.i("TAG", downloadURL);
                            final DatabaseReference newPost = databaseReference.child(currentUser.getUid()).push();

                            databaseUsers.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    newPost.child("Title").setValue(title);
                                    newPost.child("Description").setValue(description);
                                    newPost.child("Image").setValue(downloadURL);
                                    newPost.child("UserId").setValue(currentUser.getUid());
                                   // for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                                        newPost.child("ProfileImage").setValue(dataSnapshot.child("image").getValue());
                                        newPost.child("name").setValue(dataSnapshot.child("name").getValue());



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
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
