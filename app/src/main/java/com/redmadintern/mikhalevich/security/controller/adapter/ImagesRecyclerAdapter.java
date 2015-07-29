package com.redmadintern.mikhalevich.security.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.operations.Service;
import com.redmadintern.mikhalevich.security.model.local.Image;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 29.07.2015.
 */
public class ImagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_FOOTER = 1;

    private Context context;
    private LayoutInflater layoutInflater;
    private Picasso picasso;
    private boolean isFooterEnabled = true;
    private List<Image> images = new ArrayList<>();
    private OnImageClickListener onImageClickListener;
    private OnEndReachedListener onEndReachedListener;

    public ImagesRecyclerAdapter(Context context) {
        this.context = context.getApplicationContext();
        layoutInflater = LayoutInflater.from(context);
        picasso = Service.getInstance().getPicassoInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_IMAGE) {
            View imageView = layoutInflater.inflate(R.layout.item_image, parent, false);
            return new ViewHolderImage(imageView);
        } else {
            View footerView = layoutInflater.inflate(R.layout.item_footer, parent, false);
            return new ViewHolderFooter(footerView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_IMAGE) {
            ImageView imageView = (ImageView)viewHolder.itemView;
            imageView.setTag(position);
            String url = images.get(position).getThumbnail();
            picasso.load(url).placeholder(android.R.color.white).into(imageView);
        } else {
            if (isFooterEnabled) {
                viewHolder.itemView.setVisibility(View.VISIBLE);
                if (onEndReachedListener != null)
                    onEndReachedListener.onEndReached();
            } else {
                viewHolder.itemView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return images.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < images.size())
            return TYPE_IMAGE;
        else
            return TYPE_FOOTER;
    }

    class ViewHolderImage extends RecyclerView.ViewHolder {
        public ViewHolderImage(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer)view.getTag();
                    if (onImageClickListener != null) {
                        onImageClickListener.onImageClicked(view, images.get(position), position);
                    }
                }
            });
        }
    }

    class ViewHolderFooter extends RecyclerView.ViewHolder {
        public ViewHolderFooter(View itemView) {
            super(itemView);
        }
    }

    public void add(Image image) {
        images.add(image);
    }

    public void addAll(@NonNull List<Image> images) {
        this.images.addAll(images);
    }

    public void clear() {
        images.clear();
    }

    public void setFooterEnabled(boolean enabled) {
        isFooterEnabled = enabled;
    }

    @Nullable
    public Image getItem(int position) {
        if (position >= images.size())
            return null;
        else
            return images.get(position);
    }

    public Image[] getItems() {
        return images.toArray(new Image[images.size()]);
    }

    public int getCount() {
        return (images == null)?0:images.size();
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    public void setOnEndReachedListener(OnEndReachedListener onEndReachedListener) {
        this.onEndReachedListener = onEndReachedListener;
    }

    public interface OnImageClickListener {
        public void onImageClicked(View v, Image image, int position);
    }

    public interface OnEndReachedListener {
        public void onEndReached();
    }
}
