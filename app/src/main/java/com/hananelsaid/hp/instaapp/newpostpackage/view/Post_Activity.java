package com.hananelsaid.hp.instaapp.newpostpackage.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hananelsaid.hp.instaapp.MainActivity;
import com.hananelsaid.hp.instaapp.R;
import com.hananelsaid.hp.instaapp.loginpackage.viewmodel.LoginViewModel;
import com.hananelsaid.hp.instaapp.newpostpackage.viewmodel.NewPostViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Post_Activity extends AppCompatActivity {
    //Ui views refrences
    ImageView loadImage;
    EditText ettitle, etdescription;
    Button btnSubmit;

    //
    private Uri selectedImage = null;
    public static final int GALLERY_REQUEST = 2;
    private final int READIMAGE = 253;
    //
    String title = null;
    String description = null;
    // viewModel refrence
    NewPostViewModel newPostViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_);
        loadImage = findViewById(R.id.ivloadimage);
        ettitle = findViewById(R.id.ettitle);
        etdescription = findViewById(R.id.etdescription);
        btnSubmit = findViewById(R.id.btnsubmit);
        newPostViewModel = ViewModelProviders.of(this, new PostViewModelFactory(this)).get(NewPostViewModel.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = ettitle.getText().toString().trim();
                description = etdescription.getText().toString().trim();
                checkValidation();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(description) && selectedImage != null) {
                    newPostViewModel.loadImageToFirebase(selectedImage, title, description, System.currentTimeMillis()
                            + "." + getFileExtension(selectedImage));
                    openHome();
                }

            }
        });

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    public void openHome() {
        Intent openPostsActivity = new Intent(Post_Activity.this, MainActivity.class);
        // openPostsActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(openPostsActivity);
        Post_Activity.this.finish();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void checkValidation() {
        if (title.isEmpty()) {
            ettitle.setError("Please enter a Title");
            ettitle.requestFocus();
        }
        if (description.isEmpty()) {
            etdescription.setError("Please enter the description");
            etdescription.requestFocus();
        }

    }

    void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    Post_Activity.this,
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

    public void loadImage() {
        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryintent, GALLERY_REQUEST);

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
                    Toast.makeText(Post_Activity.this, "Cannot access your images", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            selectedImage = data.getData();
            CropImage.activity(selectedImage).setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(10, 10).start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selectedImage = result.getUri();
                loadImage.setImageURI(selectedImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        /*    String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    loadImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                    cursor.close();
                }
            }*/
    }

    class PostViewModelFactory implements ViewModelProvider.Factory {

        Post_Activity post_activity;

        PostViewModelFactory(Post_Activity post_activity) {
            this.post_activity = post_activity;

        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

            return (T) new NewPostViewModel(post_activity);
        }
    }
}
