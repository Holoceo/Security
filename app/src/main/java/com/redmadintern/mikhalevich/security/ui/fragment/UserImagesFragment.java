package com.redmadintern.mikhalevich.security.ui.fragment;

import android.os.Bundle;

import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;
import com.redmadintern.mikhalevich.security.controller.operations.Service;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by android on 20/07/15.
 */
public class UserImagesFragment extends ImagesBaseFragment {
    private String userName = "sky";

    @Override
    protected void onLoadImages(final ImagesLoadedCallback cb) {
        final Service service = getService();
        service.fetchUserId(userName, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                service.fetchUserMedia(s, new Callback<List<String>>() {
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

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error.getLocalizedMessage());
            }
        });
    }
}
