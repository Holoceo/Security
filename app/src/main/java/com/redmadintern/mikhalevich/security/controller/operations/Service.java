package com.redmadintern.mikhalevich.security.controller.operations;

import android.content.Context;

import com.redmadintern.mikhalevich.security.controller.network.Credentials;
import com.redmadintern.mikhalevich.security.controller.network.HttpClient;
import com.redmadintern.mikhalevich.security.controller.network.InstagramApi;
import com.redmadintern.mikhalevich.security.model.local.Image;
import com.redmadintern.mikhalevich.security.model.server.Envelope;
import com.redmadintern.mikhalevich.security.model.server.media.MediaData;
import com.redmadintern.mikhalevich.security.model.server.user.UserData;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Response;

/**
 * Created by Alexander on 15.07.2015.
 */
public class Service {
    public static final int IMAGES_PER_PAGE = 33;
    private static Service ourInstance;
    private InstagramApi instagramService;
    private Picasso picasso;
    private String token;
    private byte[] encryptKey;

    public static void initService(Context context) {
        if (ourInstance == null)
            ourInstance = new Service(context);
    }

    public static Service getInstance() {
        return ourInstance;
    }

    private Service(Context context) {
        Client client = new OkClient(HttpClient.getOkHttpInstance());

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Credentials.ENDPOINT)
                .setClient(client)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        instagramService = restAdapter.create(InstagramApi.class);

        picasso = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(HttpClient.getOkHttpInstance()))
                .build();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void fetchSelfMedia(String nextMaxId, final Callback<Envelope<MediaData>> cb) {
        instagramService.listSelfMedia(token, IMAGES_PER_PAGE, nextMaxId, cb);
    }

    public void fetchUserMedia(String userId, String nextMaxId, final Callback<Envelope<MediaData>> cb) {
        instagramService.listUserMedia(Credentials.CLIENT_ID, IMAGES_PER_PAGE, nextMaxId, userId, cb);
    }

    public void fetchUserId(String userName, final Callback<String> cb) {
        instagramService.searchUser(Credentials.CLIENT_ID, userName, new Callback<Envelope<UserData>>() {
            @Override
            public void success(Envelope<UserData> userDataEnvelope, Response response) {
                String id = userDataEnvelope.getData().get(0).getId();
                cb.success(id, response);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }



    public byte[] getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(byte[] encryptKey) {
        this.encryptKey = encryptKey;
    }

    public Picasso getPicassoInstance() {
        return picasso;
    }
}
