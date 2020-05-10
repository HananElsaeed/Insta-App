package com.hananelsaid.hp.instaapp.newpostpackage.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.hananelsaid.hp.instaapp.newpostpackage.model.UploadImageRepo;
import com.hananelsaid.hp.instaapp.newpostpackage.view.Post_Activity;

public class NewPostViewModel extends ViewModel {
    UploadImageRepo repo;
    Post_Activity post_activity;

    public NewPostViewModel( Post_Activity post_activity) {
       this.post_activity =post_activity;
        repo = new UploadImageRepo(this);
    }
    public void loadImageToFirebase(Uri uri, String title,  String description,String child) {
        repo.loadImageToFirebase(uri,title,description,child);
    }
    public void openHome(){
        post_activity.openHome();
    }

}
