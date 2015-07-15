package com.redmadintern.mikhalevich.security.controller.operations;

import com.redmadintern.mikhalevich.security.controller.network.Credentials;
import com.redmadintern.mikhalevich.security.controller.network.HttpClient;
import com.redmadintern.mikhalevich.security.controller.network.InstagramApi;
import com.redmadintern.mikhalevich.security.model.server.Envelope;
import com.redmadintern.mikhalevich.security.model.server.media.MediaData;
import com.redmadintern.mikhalevich.security.model.server.user.UserData;
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
    private static Service ourInstance = new Service();
    private InstagramApi instagramService;
    private Picasso picasso;
    private String token;
    private byte[] encryptKey;

    public static Service getInstance() {
        return ourInstance;
    }

    private Service() {
        Client client = new OkClient(HttpClient.getOkHttpInstance());

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Credentials.ENDPOINT)
                .setClient(client)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        instagramService = restAdapter.create(InstagramApi.class);
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     *
     * @param cb
     */
    public void fetchSelfMedia(final Callback<List<String>> cb) {
        instagramService.listSelfMedia(token, new Callback<Envelope<MediaData>>() {
            @Override
            public void success(Envelope<MediaData> mediaDataEnvelope, Response response) {
                List<String> urls = obtainUrls(mediaDataEnvelope);
                cb.success(urls, response);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
    }

    public void fetchUserMedia(String userId, final Callback<List<String>> cb) {
        instagramService.listUserMedia(Credentials.CLIENT_ID, userId, new Callback<Envelope<MediaData>>() {
            @Override
            public void success(Envelope<MediaData> mediaDataEnvelope, Response response) {
                List<String> urls = obtainUrls(mediaDataEnvelope);
                cb.success(urls, response);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error);
            }
        });
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

    private List<String> obtainUrls(Envelope<MediaData> mediaDataEnvelope) {
        List<MediaData> dataList = mediaDataEnvelope.getData();
        int dataSize = dataList.size();
        List<String> urls = new ArrayList<String>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            MediaData data = dataList.get(i);
            String url = data.getImages().getLowResolution().getUrl();
            urls.add(url);
        }
        return urls;
    }

    public byte[] getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(byte[] encryptKey) {
        this.encryptKey = encryptKey;
    }
}
