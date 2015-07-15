package com.redmadintern.mikhalevich.security.model.server;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {

    @SerializedName("next_url")
    @Expose
    private String nextUrl;
    @SerializedName("next_max_id")
    @Expose
    private String nextMaxId;

    /**
     *
     * @return
     * The nextUrl
     */
    public String getNextUrl() {
        return nextUrl;
    }

    /**
     *
     * @return
     * The nextMaxId
     */
    public String getNextMaxId() {
        return nextMaxId;
    }

}