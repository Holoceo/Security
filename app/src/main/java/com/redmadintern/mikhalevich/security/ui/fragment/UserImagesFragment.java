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
    private static final String KEY_NAME = "username";
    private String userName = "sky";

    public static UserImagesFragment newInstance(String userName) {
        Bundle args = new Bundle();
        args.putString(KEY_NAME, userName);
        UserImagesFragment fragment = new UserImagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            userName = args.getString(KEY_NAME);
        }
    }

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
