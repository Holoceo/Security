package com.redmadintern.mikhalevich.security.controller.network;

import com.redmadintern.mikhalevich.security.model.server.Envelope;
import com.redmadintern.mikhalevich.security.model.server.media.MediaData;
import com.redmadintern.mikhalevich.security.model.server.user.UserData;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Alexander on 15.07.2015.
 */
public interface InstagramApi {

    @GET("/v1/users/self/media/recent/")
    void listSelfMedia(@Query("access_token") String token,
                       @Query("count") int count,
                       @Query("max_id") String nextMaxId,
                       Callback<Envelope<MediaData>> cb);

    @GET("/v1/users/{user-id}/media/recent/")
    void listUserMedia(@Query("client_id") String clientId,
                       @Query("count") int count,
                       @Query("max_id") String nextMaxId,
                       @Path("user-id") String userId,
                       Callback<Envelope<MediaData>> cb);

    @GET("/v1/users/search")
    void searchUser(@Query("client_id") String clientId,
                    @Query("q") String userName,
                    Callback<Envelope<UserData>> cb);
}
