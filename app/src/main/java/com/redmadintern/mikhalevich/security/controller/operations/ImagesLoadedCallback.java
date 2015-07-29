package com.redmadintern.mikhalevich.security.controller.operations;

import com.redmadintern.mikhalevich.security.model.local.Image;

import java.util.List;

/**
 * Created by android on 20/07/15.
 */
public interface ImagesLoadedCallback {
    void success(List<Image> images, String nextMaxId);
    void failure(String message);
}
