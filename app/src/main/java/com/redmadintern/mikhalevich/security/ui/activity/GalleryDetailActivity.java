package com.redmadintern.mikhalevich.security.ui.activity;

import android.animation.TimeInterpolator;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.operations.Service;
import com.redmadintern.mikhalevich.security.model.local.Image;
import com.redmadintern.mikhalevich.security.model.local.events.ImageSavedEvent;
import com.redmadintern.mikhalevich.security.ui.view.ScaleView;
import com.redmadintern.mikhalevich.security.utils.FileUtils;
import com.redmadintern.mikhalevich.security.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.holoware.pagergallery.GalleryPagerAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Alexander on 13.12.2014.
 */
public class GalleryDetailActivity extends ActionBarActivity implements GalleryPagerAdapter.OnEndReachedListener {
    private static final int ANIM_DURATION = 250;
    public static final String EXTRA_ORIENTATION = "orientation";
    public static final String EXTRA_TOP = "top";
    public static final String EXTRA_LEFT = "left";
    public static final String EXTRA_COUNT = "count";
    public static final String POSITION = "position";
    public static final String URLS = "urls";
    public static final String IS_TOOLBAR_VISIBLE = "toolbar";

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();

    protected GalleryPagerAdapter mPagerAdapter;
    //private RelativeLayout baseLayout;
    private ViewPager viewPager;
    protected ScaleView scaleView;
    private View btnBack;
    private View btnDownload;
    private View toolbar;
    private View decorView;

    private Bitmap thumbnailBitmap;
    protected List<String> urls;
    private Rect[] rects;
    private int left;
    private int top;
    private int position;
    private boolean isToolbarVisible = true;
    private int count;

    int startOrientation;
    boolean firstLaunch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);
        decorView = getWindow().getDecorView();
        //baseLayout = (RelativeLayout) findViewById(R.id.layout);
        scaleView = (ScaleView) findViewById(R.id.thumbnail);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.view_pager_margin));
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        toolbar = findViewById(R.id.toolbar_layout);
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnDownload = findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(onImageDownloadClickListener);

        MainApplication application = (MainApplication)getApplication();

        Bundle extras = getIntent().getExtras();
        left = extras.getInt(EXTRA_LEFT);
        top = extras.getInt(EXTRA_TOP) - getStatusBarHeight();
        startOrientation = extras.getInt(EXTRA_ORIENTATION);
        rects = application.getRectArray();

        firstLaunch = (savedInstanceState == null);

        if (savedInstanceState == null) {
            position = extras.getInt(POSITION);
            count = extras.getInt(EXTRA_COUNT);

            Image[] photos = application.getPhotoArray();
            urls = getUrlsForPhotos(photos);

            thumbnailBitmap = ((MainApplication) getApplicationContext()).getBitmap();
            scaleView.setSrc(thumbnailBitmap);

            Rect currentRect = rects[position];
            if (currentRect != null) {
                Rect rect = new Rect(left + currentRect.left, top + currentRect.top,
                        left + currentRect.right, top + currentRect.bottom);
                scaleView.setRect(rect);
                scaleView.startShowAnimation();
            } else {
                viewPager.setVisibility(View.VISIBLE);
            }


            ViewTreeObserver observer = toolbar.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    toolbar.getViewTreeObserver().removeOnPreDrawListener(this);
                    showToolbar(250);
                    return true;
                }
            });
        } else {
            position = savedInstanceState.getInt(POSITION);
            urls = savedInstanceState.getStringArrayList(URLS);
            count = savedInstanceState.getInt(EXTRA_COUNT);
            isToolbarVisible = savedInstanceState.getBoolean(IS_TOOLBAR_VISIBLE);
            if (isToolbarVisible) {
                toolbar.setVisibility(View.VISIBLE);
            } else {
                toolbar.setVisibility(View.INVISIBLE);
            }

            viewPager.setVisibility(View.VISIBLE);
            scaleView.setVisibility(View.GONE);
        }

        scaleView.setAnimatorUpdateListener(animationListener);

        mPagerAdapter = new GalleryPagerAdapter(urls, this, count, thumbnailBitmap, position);
        mPagerAdapter.setImageLoadingListener(imageLoadingListener);
        mPagerAdapter.setOnClickListener(onPageClickListener);
        mPagerAdapter.setOnEndReachedListener(this);
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(position);

        if (toolbar.getVisibility() == View.INVISIBLE)
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private List<String> getUrlsForPhotos(Image[] photos) {
        urls = new ArrayList<>(photos.length);

        for (Image photo : photos) {
            urls.add(photo.getStandardResolution());
        }
        return urls;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    boolean onBackPressed = false;
    @Override
    public void onBackPressed() {
        if (onBackPressed)
            return;

        onBackPressed = true;
        int orientation = getResources().getConfiguration().orientation;
        if (firstLaunch/*orientation == startOrientation*/) {
            int currentPosition = viewPager.getCurrentItem();
            if (currentPosition >= rects.length) {
                finish();
                return;
            }

            View v = mPagerAdapter.getViewForPosition(currentPosition);
            if (v == null) {
                finish();
                return;
            }
            ImageView iv = (ImageView)v.findViewById(R.id.photo_view);

            Bitmap bitmap = Utils.drawableToBitmap(iv.getDrawable());
            if (bitmap == null)
                finish();
            scaleView.setSrc(bitmap);

            Rect currentRect = rects[currentPosition];
            if (currentRect != null) {
                Rect rect = new Rect(left + currentRect.left, top + currentRect.top,
                        left + currentRect.right, top + currentRect.bottom);
                scaleView.setRect(rect);
                scaleView.setVisibility(View.VISIBLE);
                scaleView.startHideAnimation();
                hideToolbar(false);
                viewPager.setVisibility(View.GONE);
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    ScaleView.AnimationListener animationListener = new ScaleView.AnimationListener() {
        @Override
        public void onShowAnimationEnd() {
            //viewPager.setBackgroundColor(Color.BLACK);
            viewPager.setVisibility(View.VISIBLE);
            scaleView.setVisibility(View.GONE);
        }

        @Override
        public void onHideAnimationEnd() {
            finish();
            overridePendingTransition(0, 0);
        }
    };

    private final GalleryPagerAdapter.ImageLoadingListener imageLoadingListener = new GalleryPagerAdapter.ImageLoadingListener() {
        @Override
        public void onImageLoaded(int pos) {
        }

        @Override
        public void onLoadingFailed(int pos) {
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION, position);
        outState.putStringArrayList(URLS, (ArrayList)urls);
        outState.putBoolean(IS_TOOLBAR_VISIBLE, isToolbarVisible);
        outState.putInt(EXTRA_COUNT, count);
        super.onSaveInstanceState(outState);
    }

    private final View.OnClickListener onPageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            isToolbarVisible = !isToolbarVisible;
            if (isToolbarVisible)
                showToolbar(0);
            else
                hideToolbar(true);
        }
    };

    private void showToolbar(int delay) {
        decorView.setSystemUiVisibility(0);

        toolbar.setTranslationY(-toolbar.getHeight());
        toolbar.setVisibility(View.VISIBLE);
        toolbar.animate().setDuration(ANIM_DURATION).translationY(0).setInterpolator(sDecelerator).setStartDelay(delay);
    }

    private void hideToolbar(boolean hideStatusBar) {
        if (hideStatusBar)
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

        toolbar.animate().setDuration(ANIM_DURATION).translationY(-toolbar.getHeight()).setInterpolator(sDecelerator).setStartDelay(0);
    }

    @Override
    public void onEndReached() {

    }

    ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
        @Override
        public void onPageSelected(int position) {
            GalleryDetailActivity.this.position = position;
        }
    };

    View.OnClickListener onImageDownloadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int currentItem = viewPager.getCurrentItem();
            if (urls.size() > currentItem) {
                String url = urls.get(currentItem);
                if (url == null)
                    return;

                Service.getInstance().getPicassoInstance().load(url).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        SaveImageTask saveImageTask = new SaveImageTask();
                        saveImageTask.execute(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        }
    };

    class SaveImageTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPagerAdapter.showProgress();
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            if (bitmap != null) {
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String path = FileUtils.writeImage(GalleryDetailActivity.this, byteArray);
                    return path;
                } catch (Exception e) {
                    String exception = e.getLocalizedMessage();
                    int abc = 3;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            mPagerAdapter.hideProgress();

            if (res == null) {
                Toast.makeText(GalleryDetailActivity.this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
            } else {
                EventBus.getDefault().post(new ImageSavedEvent(res));
                Toast.makeText(GalleryDetailActivity.this, "Изображение сохранено", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
