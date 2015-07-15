package com.redmadintern.mikhalevich.security.ui.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.network.HttpClient;
import com.redmadintern.mikhalevich.security.utils.FileUtils;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by android on 15/07/15.
 */
public class ImageDetailFragment extends Fragment{
    @Bind(R.id.image) ImageView imageView;
    @Bind(R.id.progressBar) ProgressBar progressBar;

    private String path;
    private Bitmap bitmap;
    private Picasso picasso;

    public static ImageDetailFragment newInstance(String path) {
        ImageDetailFragment detailFragment = new ImageDetailFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getArguments().getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        picasso = new Picasso.Builder(getActivity())
                .downloader(new OkHttpDownloader(HttpClient.getOkHttpInstance()))
                .build();

        picasso.with(getActivity()).load(path).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                ImageDetailFragment.this.bitmap = bitmap;
                imageView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!path.startsWith("file"))
            inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getFragmentManager().popBackStack();
                return true;

            case R.id.action_save:
                SaveImageTask saveImageTask = new SaveImageTask();
                saveImageTask.execute();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class SaveImageTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            if (bitmap != null) {
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    String path = getActivity().getFilesDir() + "/" + FileUtils.writeImage(getActivity(), byteArray);
                    return "OK";
                } catch (Exception e) {

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            progressBar.setVisibility(View.GONE);
            if (res == null) {
                Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
