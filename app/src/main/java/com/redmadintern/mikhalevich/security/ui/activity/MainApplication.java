package com.redmadintern.mikhalevich.security.ui.activity;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Rect;

import com.redmadintern.mikhalevich.security.controller.operations.Service;
import com.redmadintern.mikhalevich.security.model.local.Image;

/**
 * Created by Alexander on 29.07.2015.
 */
public class MainApplication extends Application {
    private Bitmap bitmap;
    private Image[] photoArray;
    private Rect[] rectArray;

    @Override
    public void onCreate() {
        super.onCreate();
        Service.initService(this);
    }

    public void putBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Image[] getPhotoArray() {
        return photoArray;
    }

    public void setPhotoArray(Image[] photoArray) {
        this.photoArray = photoArray;
    }

    public Rect[] getRectArray() {
        return rectArray;
    }

    public void setRectArray(Rect[] rectArray) {
        this.rectArray = rectArray;
    }
}
