package com.redmadintern.mikhalevich.security.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.network.HttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Alexander on 15.07.2015.
 */
public class GalleryAdapter extends ArrayAdapter<String> {
    private Picasso picasso;

    public GalleryAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        picasso = new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(HttpClient.getOkHttpInstance()))
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            image = (ImageView)inflater.inflate(R.layout.item_stream, null);
        } else {
            image = (ImageView)convertView;
        }

        String url = getItem(position);
        picasso.with(getContext()).load(url).into(image);

        return image;
    }
}
