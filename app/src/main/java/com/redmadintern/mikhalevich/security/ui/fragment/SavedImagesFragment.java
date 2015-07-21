package com.redmadintern.mikhalevich.security.ui.fragment;

import android.os.Bundle;

import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;
import com.redmadintern.mikhalevich.security.model.server.events.ImageSavedEvent;

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
        List<String> locations = new ArrayList<>(list.length);
        for (String image : list) {
            String filePath = "file://"+absPath+"/"+image;
            locations.add(filePath);
        }

        cb.success(locations);
    }

    public void onEvent(ImageSavedEvent event){
        String filePath = "file://"+absPath+"/"+event.getImageName();
        addImage(filePath);
    }
}
