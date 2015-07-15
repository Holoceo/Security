package com.redmadintern.mikhalevich.security.model.server.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("low_resolution")
    @Expose
    private LowResolution lowResolution;

    /**
     *
     * @return
     * The lowResolution
     */
    public LowResolution getLowResolution() {
        return lowResolution;
    }

}