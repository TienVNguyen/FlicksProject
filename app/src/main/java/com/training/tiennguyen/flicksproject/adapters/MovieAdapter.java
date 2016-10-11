/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.training.tiennguyen.flicksproject.R;
import com.training.tiennguyen.flicksproject.models.MovieModel;
import com.training.tiennguyen.flicksproject.utils.ConfigurationUtils;
import com.training.tiennguyen.flicksproject.viewHolders.MovieViewHolder;

import java.util.List;

/**
 * {@link MovieAdapter}
 *
 * @author TienVNguyen
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private final Context mContext;
    private final int mResource;
    private final List<MovieModel> mMovies;

    /**
     * Constructor
     *
     * @param context  {@link Context}
     * @param resource {@link Integer}
     * @param movies   {@link List<MovieModel>}
     */
    public MovieAdapter(final Context context, final int resource, final List<MovieModel> movies) {
        this.mContext = context;
        this.mResource = resource;
        this.mMovies = movies;
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(mResource, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final MovieModel model = mMovies.get(position);
        if (model != null) {
            holder.tvTitle.setText(model.getmTitle());
            holder.tvOverview.setText(model.getmOverview());
            loadingImage(holder, getImagePath(model));
        }
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
     * @param holder    {@link MovieViewHolder}
     * @param imagePath {@link String}
     */
    private void loadingImage(final MovieViewHolder holder, final String imagePath) {
        Glide.with(mContext)
                .load(imagePath)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.ivImage);
    }
}
