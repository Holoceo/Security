package com.redmadintern.mikhalevich.security.utils;

import com.redmadintern.mikhalevich.security.model.local.Image;
import com.redmadintern.mikhalevich.security.model.server.Envelope;
import com.redmadintern.mikhalevich.security.model.server.media.MediaData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 29.07.2015.
 */
public class ImagesHelper {
    public static List<Image> obtainImages(Envelope<MediaData> mediaDataEnvelope) {
        List<MediaData> dataList = mediaDataEnvelope.getData();
        int dataSize = dataList.size();

        List<Image> images = new ArrayList<Image>(dataSize);
        for (int i = 0; i < dataSize; i++) {
            MediaData data = dataList.get(i);
            String thumbnail = data.getImages().getThumbnail().getUrl();
            String standardResolution = data.getImages().getStandardResolution().getUrl();
            Image image = new Image(thumbnail, standardResolution);
            images.add(image);
        }
        return images;
    }
}
