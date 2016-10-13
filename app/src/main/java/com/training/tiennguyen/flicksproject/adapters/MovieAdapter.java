/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.training.tiennguyen.flicksproject.R;
import com.training.tiennguyen.flicksproject.models.MovieModel;
import com.training.tiennguyen.flicksproject.utils.ConfigurationUtils;
import com.training.tiennguyen.flicksproject.viewHolders.MovieViewHolder1;
import com.training.tiennguyen.flicksproject.viewHolders.MovieViewHolder2;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * {@link MovieAdapter}
 *
 * @author TienVNguyen
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final List<MovieModel> mMovies;
    private final int POPULAR = 0;

    /**
     * Constructor
     *
     * @param context {@link Context}
     * @param movies  {@link List<MovieModel>}
     */
    public MovieAdapter(final Context context, final List<MovieModel> movies) {
        this.mContext = context;
        this.mMovies = movies;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public int getItemViewType(int position) {
        double voteAverage = Double.parseDouble(mMovies.get(position).getmVoteAverage());
        if (voteAverage > 5) {
            return POPULAR;
        } else {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case POPULAR:
                View v2 = inflater.inflate(R.layout.item_movie_2, parent, false);
                viewHolder = new MovieViewHolder2(mContext, v2, mMovies);
                break;
            default:
                View v1 = inflater.inflate(R.layout.item_movie_1, parent, false);
                viewHolder = new MovieViewHolder1(mContext, v1, mMovies);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MovieModel model = mMovies.get(position);
        if (model != null) {
            switch (holder.getItemViewType()) {
                case POPULAR:
                    MovieViewHolder2 vh2 = (MovieViewHolder2) holder;
                    configureViewHolder2(model, vh2);
                    break;
                default:
                    MovieViewHolder1 vh1 = (MovieViewHolder1) holder;
                    configureViewHolder1(model, vh1);
                    break;
            }
        }
    }

    /**
     * Configure View Holder 1
     *
     * @param model            {@link MovieModel}
     * @param movieViewHolder1 {@link MovieViewHolder1}
     */
    private void configureViewHolder1(MovieModel model, MovieViewHolder1 movieViewHolder1) {
        movieViewHolder1.tvTitle.setText(model.getmTitle());
        movieViewHolder1.tvOverview.setText(model.getmOverview());
        loadingImage(movieViewHolder1.ivImage, getImagePath(model));
    }

    /**
     * Configure View Holder 2
     *
     * @param model            {@link MovieModel}
     * @param movieViewHolder2 {@link MovieViewHolder2}
     */
    private void configureViewHolder2(MovieModel model, MovieViewHolder2 movieViewHolder2) {
        loadingImage(movieViewHolder2.ivImageOnly, getImagePath(model));
    }

    /**
     * Get Image Path base on Configuration
     *
     * @param model {@link MovieModel}
     */
    private String getImagePath(final MovieModel model) {
        String imagePath;
        if (ConfigurationUtils.isLandscape(mContext)) {
            imagePath = model.getmBackdropPath();
        } else {
            imagePath = model.getmPosterPath();
        }
        return imagePath;
    }

    /**
     * Using Glide to load image
     *
     * @param imageView {@link ImageView}
     * @param imagePath {@link String}
     */
    private void loadingImage(final ImageView imageView, final String imagePath) {
        RoundedCornersTransformation transformation = new RoundedCornersTransformation(mContext,
                35, 0, RoundedCornersTransformation.CornerType.ALL);

        Glide.with(mContext)
                .load(imagePath)
                .listener(getRequestListenerForImage(imageView))
                .placeholder(R.drawable.image_placeholder)
                .bitmapTransform(transformation)
                .into(imageView);
    }

    /**
     * Request Listener For Image
     *
     * @param imageView {@link ImageView}
     * @return {@link RequestListener}
     */
    @NonNull
    private RequestListener<String, GlideDrawable> getRequestListenerForImage(final ImageView imageView) {
        return new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                imageView.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                imageView.setVisibility(View.VISIBLE);
                return false;
            }
        };
    }
}
