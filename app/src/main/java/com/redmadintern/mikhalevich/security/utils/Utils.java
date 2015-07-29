package com.redmadintern.mikhalevich.security.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.redmadintern.mikhalevich.security.model.local.Image;
import com.redmadintern.mikhalevich.security.ui.activity.GalleryDetailActivity;
import com.redmadintern.mikhalevich.security.ui.activity.MainApplication;

import org.json.JSONException;

/**
 * Created by Alexander on 13.12.2014.
 */
public class Utils {

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable == null)
            return null;

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        if (width <= 0 | height <= 0)
            return null;


        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static void showGallery(Activity context, Bitmap bitmap, int left, int top, Image[] photoArray,
                                    Rect[] rectArray, int position, int count){
        //if ( position < 0 || position >= photos.length)
        //    return;

        Intent intent = new Intent(context, GalleryDetailActivity.class);

        MainApplication application = (MainApplication)context.getApplication();
        application.putBitmap(bitmap);


        int orientation = context.getResources().getConfiguration().orientation;

        application.setPhotoArray(photoArray);
        application.setRectArray(rectArray);

        intent.putExtra(GalleryDetailActivity.EXTRA_ORIENTATION, orientation);
        intent.putExtra(GalleryDetailActivity.EXTRA_TOP, top);
        intent.putExtra(GalleryDetailActivity.EXTRA_LEFT, left);
        intent.putExtra(GalleryDetailActivity.POSITION, position);
        intent.putExtra(GalleryDetailActivity.EXTRA_COUNT, count);

        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }
}
