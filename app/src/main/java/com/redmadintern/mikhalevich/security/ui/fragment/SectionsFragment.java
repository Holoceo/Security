package com.redmadintern.mikhalevich.security.ui.fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.adapter.SectionsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by android on 20/07/15.
 */
public class SectionsFragment extends Fragment {
    @Bind(R.id.tabLayout) TabLayout tabLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private int colorTabIconSelected;
    private int colorTabIconUnselected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        colorTabIconSelected = getResources().getColor(R.color.tab_icon_selected);
        colorTabIconUnselected = getResources().getColor(R.color.tab_icon_unselected);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sections, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setupViewPager();
        setupTabs();
    }

    private void setupTabs() {
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_action_save));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_action_save));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_action_save));

        TabLayout.TabLayoutOnPageChangeListener onPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            viewPager.setCurrentItem(position);
            setTabIconColor(tab, colorTabIconSelected);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            setTabIconColor(tab, colorTabIconUnselected);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void setupViewPager() {
        FragmentPagerAdapter pagerAdapter = new SectionsAdapter(getActivity().getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
    }

    private void setTabIconColor(TabLayout.Tab tab, int color) {
        tab.getIcon().mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

}
