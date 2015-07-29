package com.redmadintern.mikhalevich.security.ui.fragment.images;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.redmadintern.mikhalevich.security.R;
import com.redmadintern.mikhalevich.security.controller.adapter.ImagesRecyclerAdapter;
import com.redmadintern.mikhalevich.security.controller.operations.ImagesLoadedCallback;
import com.redmadintern.mikhalevich.security.controller.operations.Service;
import com.redmadintern.mikhalevich.security.model.local.Image;
import com.redmadintern.mikhalevich.security.model.server.media.Images;
import com.redmadintern.mikhalevich.security.ui.fragment.ImageDetailFragment;
import com.redmadintern.mikhalevich.security.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 15.07.2015.
 */
public abstract class ImagesBaseFragment extends Fragment implements ImagesLoadedCallback,
        ImagesRecyclerAdapter.OnImageClickListener, ImagesRecyclerAdapter.OnEndReachedListener {
    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;
    @Bind(R.id.float_action_button) View actionButton;
    @Bind(R.id.no_connection) View viewNoConnection;
    @Bind(R.id.btnTryAgain) View btnTryAgain;

    private ImagesRecyclerAdapter imagesAdapter;
    private Service service;
    private int numColumns;
    private String nextMaxId;

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
        initNums();
        setupRecyclerView();

        if (savedInstanceState == null) {
            imagesAdapter = new ImagesRecyclerAdapter(getActivity());
            recyclerView.setAdapter(imagesAdapter);
            loadImages();
        } else {
            recyclerView.setAdapter(imagesAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        imagesAdapter.setOnImageClickListener(this);
        imagesAdapter.setOnEndReachedListener(this);
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), numColumns);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (imagesAdapter.getItemViewType(position)) {
                    case ImagesRecyclerAdapter.TYPE_IMAGE:
                        return 1;

                    default:
                        return numColumns;
                }
            }
        };
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.addItemDecoration(new SpacesItemDecoration(R.dimen.grid_spacing));
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initNums() {
        numColumns = getResources().getInteger(R.integer.num_columns);
    }

    protected void loadImages() {
        setTransitionShown(recyclerView, progressBar);
        nextMaxId = null;
        onLoadImages(this);
    }

    protected void loadImagesFromNoConnection() {
        setTransitionShown(viewNoConnection, progressBar);
        nextMaxId = null;
        onLoadImages(this);
    }

    protected abstract void onLoadImages(ImagesLoadedCallback cb);

    public Service getService() {
        return service;
    }

    @Override
    public void success(List<Image> images, String nextMaxId) {
        this.nextMaxId = nextMaxId;
        imagesAdapter.clear();
        imagesAdapter.addAll(images);
        recyclerView.scrollToPosition(0);
        imagesAdapter.notifyDataSetChanged();
        if (images.size() < Service.IMAGES_PER_PAGE)
            imagesAdapter.setFooterEnabled(false);

        setTransitionShown(progressBar, recyclerView);
    }

    @Override
    public void failure(String message) {
        setTransitionShown(progressBar, viewNoConnection);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadImagesFromNoConnection();
            }
        });
        //Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onImageClicked(View v, Image image, final int position) {
        /*Fragment fragment = ImageDetailFragment.newInstance(image.getStandardResolution());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

        Service.getInstance().getPicassoInstance().load(image.getThumbnail()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                //Photos
                Image[] photos = imagesAdapter.getItems();

                //Get location on screen
                int[] screenLocation = new int[2];
                recyclerView.getLocationOnScreen(screenLocation);
                int top = screenLocation[1];

                int count = imagesAdapter.getCount();

                Rect[] rectArray = new Rect[count+1];


                for (int i=0; i < recyclerView.getChildCount(); i++) {
                    View v = recyclerView.getChildAt(i);
                    int position = recyclerView.getChildAdapterPosition(v);
                    Rect rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                    rectArray[position] = rect;
                }



                Utils.showGallery(getActivity(), bitmap, 0, top, photos, rectArray, position, count);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    public void onEndReached() {
        onLoadImages(new ImagesLoadedCallback() {
            @Override
            public void success(List<Image> images, String nextMaxId) {
                ImagesBaseFragment.this.nextMaxId = nextMaxId;
                imagesAdapter.addAll(images);
                imagesAdapter.notifyDataSetChanged();
                if (images.size() < Service.IMAGES_PER_PAGE)
                    imagesAdapter.setFooterEnabled(false);
            }

            @Override
            public void failure(String message) {
                ImagesBaseFragment.this.failure(message);
            }
        });
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

    protected void addImage(Image image) {
        imagesAdapter.add(image);
        imagesAdapter.notifyDataSetChanged();
    }

    protected String getNextMaxId() {
        return nextMaxId;
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int spaceResource) {
            this.space = getResources().getDimensionPixelSize(spaceResource);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.top = space;
            outRect.right = space;
            outRect.bottom = space;

            int childPosition = parent.getChildAdapterPosition(view);

            if ((childPosition % numColumns) == 0)
                outRect.left += space;

            if (((childPosition+1) % numColumns) == 0)
                outRect.right += space;

            if (childPosition < numColumns)
                outRect.top += space;

            // Add top margin only for the first item to avoid double space between items
            /*if(parent.getChildPosition(view) == 0)
                outRect.top = space;*/
        }
    }

    public View getActionButton() {
        return actionButton;
    }
}
