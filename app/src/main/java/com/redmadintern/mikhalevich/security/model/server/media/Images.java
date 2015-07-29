package com.redmadintern.mikhalevich.security.model.server.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Images {

    @SerializedName("thumbnail")
    @Expose
    private Image thumbnail;

    @SerializedName("standard_resolution")
    @Expose
    private Image standardResolution;

    public Image getThumbnail() {
        return thumbnail;
    }

    public Image getStandardResolution() {
        return standardResolution;
    }
}