package com.redmadintern.mikhalevich.security.ui.fragment;

import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;
import com.squareup.picasso.Downloader;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Alexander on 21.07.2015.
 */
public class MyImagesFragment extends ImagesBaseFragment {

    @Override
    protected void onLoadImages(final ImagesLoadedCallback cb) {
        getService().fetchSelfMedia(new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                cb.success(strings);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error.getLocalizedMessage());
            }
        });
    }
}
