/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.training.tiennguyen.flicksproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MovieViewHolder}
 *
 * @author TienVNguyen
 */
public class MovieViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.listItemIvImage)
    public ImageView ivImage;
    @BindView(R.id.listItemTvTitle)
    public TextView tvTitle;
    @BindView(R.id.listItemTvOverview)
    public TextView tvOverview;

    /**
     * Constructor
     *
     * @param view {@link View}
     */
    public MovieViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
