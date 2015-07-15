package com.redmadintern.mikhalevich.security.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 15/07/15.
 */
public class FileUtils {
    public static final String IMAGE_FILE_NAME = "image";

    public static String writeImage(Context context, byte[] byteArray) {
        try {
            File imagesDir = context.getFilesDir();
            int num = imagesDir.list().length;

            String fileName = IMAGE_FILE_NAME + num + ".jpeg";
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(byteArray);
            fos.close();
            return fileName;
        } catch (Exception e) {
            return null;
        }
    }
}
