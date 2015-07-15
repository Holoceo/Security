package com.redmadintern.mikhalevich.security.ui.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.adapter.GalleryAdapter;
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
public class StreamFragment extends Fragment implements View.OnClickListener{
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.grid) GridView gridView;
    @Bind(R.id.all) Button btnAll;
    @Bind(R.id.saved) Button btnSaved;
    @Bind(R.id.search) Button btnSearch;

    @Bind(R.id.layout_search) View laSearch;
    @Bind(R.id.edit) EditText editText;
    @Bind(R.id.search_user) Button btnFind;

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
        View view = inflater.inflate(R.layout.fragment_stream, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Фото");
        btnAll.setOnClickListener(this);
        btnSaved.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnFind.setOnClickListener(this);

        galleryAdapter = new GalleryAdapter(getActivity(), R.layout.item_stream, new ArrayList<String>());
        gridView.setAdapter(galleryAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });

        if (savedInstanceState == null)
            loadMyPhotos();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all:
                loadMyPhotos();
                break;

            case R.id.saved:
                loadSavedPhotos();
                break;

            case R.id.search:
                laSearch.setVisibility(View.VISIBLE);
                galleryAdapter.clear();
                galleryAdapter.notifyDataSetChanged();
                break;

            case R.id.search_user:
                loadUserPhotos(editText.getText().toString());
                break;
        }
    }

    private void loadMyPhotos() {
        laSearch.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        service.fetchSelfMedia(new Callback<List<String>>() {
            @Override
            public void success(List<String> strings, Response response) {
                progressBar.setVisibility(View.GONE);
                galleryAdapter.clear();
                galleryAdapter.addAll(strings);
                galleryAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUserPhotos(String userName) {
        progressBar.setVisibility(View.VISIBLE);
        service.fetchUserId(userName, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                service.fetchUserMedia(s, new Callback<List<String>>() {
                    @Override
                    public void success(List<String> strings, Response response) {
                        progressBar.setVisibility(View.GONE);
                        galleryAdapter.addAll(strings);
                        galleryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadSavedPhotos() {
        laSearch.setVisibility(View.GONE);
        File imagesDir = getActivity().getFilesDir();
        String absPath = imagesDir.getAbsolutePath();

        String[] list = imagesDir.list();
        List<String> locations = new ArrayList<>(list.length);
        for (String image : list) {
            String filePath = "file://"+absPath+"/"+image;
            locations.add(filePath);
        }
        galleryAdapter.clear();
        galleryAdapter.addAll(locations);
        galleryAdapter.notifyDataSetChanged();
    }
}
