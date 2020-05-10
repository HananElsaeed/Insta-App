package com.hananelsaid.hp.instaapp.profilepackage.viewmodel;

import android.net.Uri;

import androidx.lifecycle.ViewModel;

import com.hananelsaid.hp.instaapp.profilepackage.model.UploadUserRepo;

public class ProfileViewModel extends ViewModel {
    UploadUserRepo uploadUserRepo;

    public ProfileViewModel() {
        uploadUserRepo = new UploadUserRepo(this);
    }

    public void loaddataToFirebase(Uri imageUri, String name, String phone) {
        uploadUserRepo.loadDataToFirebase(imageUri, name, phone);
    }
}
