package com.redmadintern.mikhalevich.security.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.adapter.GalleryAdapter;
import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;
import com.redmadintern.mikhalevich.security.controller.operations.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Alexander on 15.07.2015.
 */
public abstract class ImagesBaseFragment extends Fragment implements ImagesLoadedCallback, AdapterView.OnItemClickListener{
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.gridView) GridView gridView;

    private GalleryAdapter galleryAdapter;
    private Service service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        service = Service.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images_base, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            galleryAdapter = new GalleryAdapter(getActivity(), R.layout.item_stream, new ArrayList<String>());
            loadImages();
        } else {
            gridView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        gridView.setAdapter(galleryAdapter);
        gridView.setOnItemClickListener(this);
    }

    protected void loadImages() {
        setTransitionShown(gridView, progressBar);
        onLoadImages(this);
    }

    protected abstract void onLoadImages(ImagesLoadedCallback cb);

    public Service getService() {
        return service;
    }

    @Override
    public void success(List<String> uris) {
        galleryAdapter.clear();
        galleryAdapter.addAll(uris);
        galleryAdapter.notifyDataSetChanged();
        setTransitionShown(progressBar, gridView);
    }

    @Override
    public void failure(String message) {
        setTransitionShown(progressBar, gridView);
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = galleryAdapter.getItem(position);
        Fragment fragment = ImageDetailFragment.newInstance(url);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setTransitionShown(View from, View to) {
        if (from != null){
            from.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_out));
            from.setVisibility(View.GONE);
        }

        if (to != null){
            to.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_in));
            to.setVisibility(View.VISIBLE);
        }
    }
}
