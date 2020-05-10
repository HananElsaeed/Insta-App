package com.hananelsaid.hp.instaapp.postspackage.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.hananelsaid.hp.instaapp.newpostpackage.model.UploadImageRepo;
import com.hananelsaid.hp.instaapp.postspackage.model.Post;
import com.hananelsaid.hp.instaapp.postspackage.model.PostRepo;
import com.hananelsaid.hp.instaapp.profilepackage.model.User;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ViewModel {
    public LiveData<List<Post>> postsLiveMutableData = new MutableLiveData<>();

    PostRepo postRepo;

   public PostViewModel(){
       postRepo = new PostRepo(this);
   }

    public MutableLiveData<ArrayList<Post>> getDataFromFirebase() {

        return postRepo.getPostsDataFromFirebase();

    }
    public MutableLiveData<ArrayList<User>> getUserDataFromFirebase() {

        return postRepo.getUserDataFromFirebase();

    }

}
