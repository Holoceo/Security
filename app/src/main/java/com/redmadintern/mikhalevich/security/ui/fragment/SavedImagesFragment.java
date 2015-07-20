package com.redmadintern.mikhalevich.security.ui.fragment;

import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 21.07.2015.
 */
public class SavedImagesFragment extends ImagesBaseFragment{

    @Override
    protected void onLoadImages(ImagesLoadedCallback cb) {
        File imagesDir = getActivity().getFilesDir();
        String absPath = imagesDir.getAbsolutePath();

        String[] list = imagesDir.list();
        List<String> locations = new ArrayList<>(list.length);
        for (String image : list) {
            String filePath = "file://"+absPath+"/"+image;
            locations.add(filePath);
        }

        cb.success(locations);
    }
}
