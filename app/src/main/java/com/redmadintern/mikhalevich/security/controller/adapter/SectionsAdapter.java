package com.redmadintern.mikhalevich.security.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.redmadintern.mikhalevich.security.ui.fragment.images.MyImagesFragment;
import com.redmadintern.mikhalevich.security.ui.fragment.images.SavedImagesFragment;
import com.redmadintern.mikhalevich.security.ui.fragment.images.UserImagesFragment;

/**
 * Created by Alexander on 28.07.2015.
 */
public class SectionsAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 3;

    public SectionsAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MyImagesFragment();

            case 1:
                return new SavedImagesFragment();

            case 2:
                return new UserImagesFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    public static String getTitle(int pagePosition) {
        switch (pagePosition) {
            case 0:
                return "Профиль";

            case 1:
                return "Сохраненные";

            case 2:
                return "Поиск";

            default:
                return "";
        }
    }
}
