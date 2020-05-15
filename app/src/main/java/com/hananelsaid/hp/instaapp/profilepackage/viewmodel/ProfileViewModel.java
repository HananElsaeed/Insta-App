package com.hananelsaid.hp.instaapp.profilepackage.viewmodel;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hananelsaid.hp.instaapp.profilepackage.model.DownloadUserDataRepo;
import com.hananelsaid.hp.instaapp.profilepackage.model.UploadUserRepo;
import com.hananelsaid.hp.instaapp.profilepackage.model.User;

public class ProfileViewModel extends ViewModel {
    UploadUserRepo uploadUserRepo;
    DownloadUserDataRepo downloadUserDataRepo;

    public ProfileViewModel() {
        uploadUserRepo = new UploadUserRepo(this);
        downloadUserDataRepo=new DownloadUserDataRepo(this);
    }
    public MutableLiveData<User> getUserData() {

        return downloadUserDataRepo.getUserData();

    }

    public void loaddataToFirebase(Uri imageUri, String name, String phone) {
        uploadUserRepo.loadDataToFirebase(imageUri, name, phone);
    }
}
