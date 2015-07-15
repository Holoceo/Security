package com.redmadintern.mikhalevich.security.model.server.media;

import com.google.gson.annotations.Expose;
import com.redmadintern.mikhalevich.security.model.server.media.Images;

public class MediaData {

    @Expose
    private Images images;

    /**
     *
     * @return
     * The images
     */
    public Images getImages() {
        return images;
    }
}