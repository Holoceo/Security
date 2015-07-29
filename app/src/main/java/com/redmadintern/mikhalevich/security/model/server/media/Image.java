package com.redmadintern.mikhalevich.security.model.server.media;

import com.google.gson.annotations.Expose;

public class Image {

    @Expose
    private String url;
    @Expose
    private Integer width;
    @Expose
    private Integer height;

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     * The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     *
     * @return
     * The height
     */
    public Integer getHeight() {
        return height;
    }

}