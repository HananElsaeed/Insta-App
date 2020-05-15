package com.hananelsaid.hp.instaapp;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;
    Context context;

    public SaveImageHelper(Context context, ContentResolver contentResolver, String name, String desc) {
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
        this.context = context;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Toast.makeText(context, "onBitmapLoaded", Toast.LENGTH_SHORT).show();

        ContentResolver r = contentResolverWeakReference.get();
        if (r != null) {
            MediaStore.Images.Media.insertImage(r, bitmap, name, desc);
            //open Gallary after saving
            Intent openGallary = new Intent();
            openGallary.setAction(Intent.ACTION_GET_CONTENT);
            openGallary.setType("image /*");
            context.startActivity(Intent.createChooser(openGallary, "VIEW PICTURE"));


        }
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
