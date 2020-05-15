package com.hananelsaid.hp.instaapp.postspackage.model;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hananelsaid.hp.instaapp.postspackage.viewmodel.PostViewModel;
import com.hananelsaid.hp.instaapp.profilepackage.model.User;

import java.util.ArrayList;

public class PostRepo {
    PostViewModel postViewModel;


    public PostRepo(PostViewModel postViewModel) {
        this.postViewModel = postViewModel;

    }


    public MutableLiveData<ArrayList<Post>> getPostsDataFromFirebase() {
        Log.i("TAG", "Hi I am here: ");
        return FirebasGetPosts.viewPosts();
    }

    public MutableLiveData<ArrayList<User>> getUserDataFromFirebase() {
        return FirebasGetPosts.viewuser();
    }


    static class FirebasGetPosts {

        static MutableLiveData<ArrayList<Post>> liveData = new MutableLiveData<ArrayList<Post>>();
        static MutableLiveData<ArrayList<User>> liveData2 = new MutableLiveData<ArrayList<User>>();

        static final ArrayList<Post> postsList = new ArrayList<Post>();
        static final ArrayList<User> usersList = new ArrayList<>();
        private static DatabaseReference databaseReference;
        private static DatabaseReference databaseUser;


        public static MutableLiveData<ArrayList<Post>> viewPosts() {
            databaseReference = FirebaseDatabase.getInstance().getReference("InstaApp")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    // Read from the database

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (dataSnapshot != null) {
                                postsList.clear();
                                Post ps;
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    Object title1 = dataSnapshot1.child("Title").getValue();
                                    Object description1 = dataSnapshot1.child("Description").getValue();
                                    Object image1 = dataSnapshot1.child("Image").getValue();
                                    if (title1 != null && description1 != null && image1 != null) {

                                        String title = title1.toString();
                                        String description = description1.toString();
                                        String image = image1.toString();

                                        Log.i("TAG", "Value is: " + title);
                                        ps = new Post(title, description, image);
                                        postsList.add(ps);
                                        Log.i("TAG2", "posts number " + postsList.size());

                                    }
                                }
                                Log.i("TAG2", "posts number " + postsList.size());
                                liveData.setValue(postsList);
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

        public static MutableLiveData<ArrayList<User>> viewuser() {
            databaseUser = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            new Thread() {
                @Override
                public void run() {
                    super.run();
                    // Read from the database

                    databaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot != null) {
                                usersList.clear();
                                User user;
                                Object image = dataSnapshot.child("image").getValue();
                                Object name = dataSnapshot.child("name").getValue();
                                if (image != null && name != null) {
                                    String profileImage = image.toString();
                                    String userName = name.toString();
                                    //Log.i("TAG", "Value is: " + profileImage);
                                    user = new User(userName, profileImage);
                                    usersList.add(user);
                                    Log.i("TAG2", "users number " + usersList.size());


                                    liveData2.setValue(usersList);
                                }
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

            return liveData2;
        }
    }
}
