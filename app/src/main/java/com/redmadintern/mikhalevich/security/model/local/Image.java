package com.redmadintern.mikhalevich.security.model.local;

/**
 * Created by Alexander on 29.07.2015.
 */
public class Image {
    private String thumbnail;
    private String standardResolution;

    public Image(String thumbnail, String standardResolution) {
        this.thumbnail = thumbnail;
        this.standardResolution = standardResolution;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getStandardResolution() {
        return standardResolution;
    }
}
