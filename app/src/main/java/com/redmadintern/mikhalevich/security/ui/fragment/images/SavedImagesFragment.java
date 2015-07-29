package com.redmadintern.mikhalevich.security.ui.fragment.images;

import android.os.Bundle;

import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;
import com.redmadintern.mikhalevich.security.model.local.Image;
import com.redmadintern.mikhalevich.security.model.local.events.ImageSavedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Alexander on 21.07.2015.
 */
public class SavedImagesFragment extends ImagesBaseFragment{
    private File imagesDir;
    private String absPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagesDir = getActivity().getFilesDir();
        absPath = imagesDir.getAbsolutePath();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onLoadImages(ImagesLoadedCallback cb) {


        String[] list = imagesDir.list();
        List<Image> images = new ArrayList<>(list.length);
        for (String image : list) {
            String filePath = "file://"+absPath+"/"+image;
            Image img = new Image(filePath, filePath);
            images.add(img);
        }

        cb.success(images, null);
    }

    public void onEvent(ImageSavedEvent event){
        String filePath = "file://"+absPath+"/"+event.getImageName();
        Image img = new Image(filePath, filePath);
        addImage(img);
    }
}
