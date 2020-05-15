package com.hananelsaid.hp.instaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.UUID;

public class ShowPostImage extends AppCompatActivity {
    ImageView ivPostImage;
    String postImageUrl, profileImageUrl;
    private final int WRITEIMAGE = 100;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post_image);
        ivPostImage = findViewById(R.id.ivPostImage);
        Intent intent = getIntent();
        postImageUrl = intent.getStringExtra("postimageUrl");
        profileImageUrl = intent.getStringExtra("profileimageUrl");
        if (postImageUrl != null)
            Picasso.get().load(postImageUrl).into(ivPostImage);
        else if (profileImageUrl != null)
            Picasso.get().load(profileImageUrl).into(ivPostImage);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_showimage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String fileName = UUID.randomUUID().toString() + ".jpg";
        if (id == R.id.action_save) {
            if (postImageUrl != null) {
                checkPermission();
            } else if (profileImageUrl != null)
                checkPermission();

        }
        return super.onOptionsItemSelected(item);
    }

    void checkPermission(/*String url, Context context, ContentResolver contentResolver, String name, String desc*/) {

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(
                    ShowPostImage.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !=
                    PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITEIMAGE);
                return;
            } else {
                Log.i("TAG", "onOptionsItemSelected: ");
                // Picasso.get().load(url).into(new SaveImageHelper(context, contentResolver, name, desc));
                saveImage();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WRITEIMAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ShowPostImage.this, "Granted", Toast.LENGTH_LONG).show();
                    saveImage();
                    break;
                } else
                    Toast.makeText(ShowPostImage.this, "Cannot access your Album", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void saveImage() {
        //
        imageBitmap = ((BitmapDrawable) ivPostImage.getDrawable()).getBitmap();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).toLocalizedPattern()
                .format(String.valueOf(System.currentTimeMillis()));


        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String imageName = time + ".JPEG";
        File file = new File(dir, imageName);
        OutputStream ostream;

        try {
            ostream = new FileOutputStream(file);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.flush();
            ostream.close();
            Toast.makeText(getApplicationContext(), "Saved to the Pictures", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
