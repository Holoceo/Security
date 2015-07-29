package com.redmadintern.mikhalevich.security.ui.fragment.images;

import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;
import com.redmadintern.mikhalevich.security.model.local.Image;
import com.redmadintern.mikhalevich.security.model.server.Envelope;
import com.redmadintern.mikhalevich.security.model.server.media.MediaData;
import com.redmadintern.mikhalevich.security.utils.ImagesHelper;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Alexander on 21.07.2015.
 */
public class MyImagesFragment extends ImagesBaseFragment {

    @Override
    protected void onLoadImages(final ImagesLoadedCallback cb) {
        getService().fetchSelfMedia(getNextMaxId(), new Callback<Envelope<MediaData>>() {
            @Override
            public void success(Envelope<MediaData> mediaDataEnvelope, Response response) {
                List<Image> images = ImagesHelper.obtainImages(mediaDataEnvelope);
                String nextMaxId = mediaDataEnvelope.getPagination().getNextMaxId();
                cb.success(images, nextMaxId);
            }

            @Override
            public void failure(RetrofitError error) {
                cb.failure(error.getLocalizedMessage());
            }
        });
    }
}
