package com.hananelsaid.hp.instaapp.profilepackage.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.hananelsaid.hp.instaapp.MainActivity;
import com.hananelsaid.hp.instaapp.R;
import com.hananelsaid.hp.instaapp.profilepackage.viewmodel.ProfileViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static com.hananelsaid.hp.instaapp.newpostpackage.view.Post_Activity.GALLERY_REQUEST;

public class Profile extends AppCompatActivity {

    ImageView ivPerson;
    TextInputEditText etName, etPhone;
    Button btnDone;

    //
    String name, phone;
    //
    private Uri imageUri = null;
   // private static final int GALLERY_REQUEST2 = 5;
    private final int READIMAGE = 203;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ivPerson = findViewById(R.id.ivUserProfile);
        etName = findViewById(R.id.etUserName);
        etPhone = findViewById(R.id.etPhone);
        btnDone = findViewById(R.id.btnDone);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        ivPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                checkValidation();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone)&&imageUri!=null) {
                    profileViewModel.loaddataToFirebase(imageUri, name, phone);
                    Intent openPostsActivity = new Intent(Profile.this, MainActivity.class);
                    startActivity(openPostsActivity);
                    Profile.this.finish();
                }


            }
        });

    }

    void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    Profile.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
                    PackageManager.PERMISSION_GRANTED
            ) {

                // requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READIMAGE);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READIMAGE);
                return;
            }
        }

        loadImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READIMAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage();
                    break;
                } else
                    Toast.makeText(Profile.this, "Cannot access your images", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void loadImage() {
        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryintent, GALLERY_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                ivPerson.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void checkValidation() {
        if (name.isEmpty()) {
            etName.setError("Please enter your Name");
            etName.requestFocus();
        }
        if (phone.isEmpty()) {
            etPhone.setError("Please enter your Phone");
            etPhone.requestFocus();
        }

    }
}
