package com.redmadintern.mikhalevich.security.model.server.events;

/**
 * Created by android on 21/07/15.
 */
public class ImageSavedEvent {
    private String imageName;

    public ImageSavedEvent(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
