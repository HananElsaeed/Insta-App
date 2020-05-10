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
            databaseReference = FirebaseDatabase.getInstance().getReference("InstaApp").child(FirebaseAuth.getInstance().getUid());


            new Thread() {
                @Override
                public void run() {
                    super.run();
                    // Read from the database

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            postsList.clear();
                            Post ps;
                            //Post value1 = dataSnapshot.getValue(Post.class);
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if (dataSnapshot1.child("UserId").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                    String title = dataSnapshot1.child("Title").getValue().toString();
                                    String description = dataSnapshot1.child("Description").getValue().toString();
                                    String image = dataSnapshot1.child("Image").getValue().toString();
                                    //String profileImage = dataSnapshot1.child("ProfileImage").getValue().toString();
                                    // String userName = dataSnapshot1.child("name").getValue().toString();

                                    //Log.i("TAG", "Value is: " + profileImage);
                                    ps = new Post(title, description, image);
                                    // ps=new Post(title,description,userName,profileImage,image);

                                    postsList.add(ps);
                                    Log.i("TAG2", "posts number " + postsList.size());
                                }
                            }

                            liveData.setValue(postsList);
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

                            usersList.clear();
                            User user;

                            String profileImage = dataSnapshot.child("image").getValue().toString();
                            String userName = dataSnapshot.child("name").getValue().toString();

                            //Log.i("TAG", "Value is: " + profileImage);
                            user = new User(userName, profileImage);
                            usersList.add(user);
                            Log.i("TAG2", "posts number " + postsList.size());


                            liveData2.setValue(usersList);
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
