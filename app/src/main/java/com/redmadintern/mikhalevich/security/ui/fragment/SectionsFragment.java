package com.redmadintern.mikhalevich.security.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.redmadintern.mikhalevich.security.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by android on 20/07/15.
 */
public class SectionsFragment extends Fragment {
    @Bind(R.id.tabLayout) TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections, container, false);
        ButterKnife.bind(this, view);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2 fgfgd fgdhfgd"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
