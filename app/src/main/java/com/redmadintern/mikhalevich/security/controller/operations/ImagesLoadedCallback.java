package com.redmadintern.mikhalevich.security.controller.operations;

import java.util.List;

/**
 * Created by android on 20/07/15.
 */
public interface ImagesLoadedCallback {
    void success(List<String> uris);
    void failure(String message);
}
