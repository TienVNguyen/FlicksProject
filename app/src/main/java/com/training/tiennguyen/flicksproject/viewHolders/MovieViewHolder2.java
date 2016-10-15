/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.viewHolders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.training.tiennguyen.flicksproject.R;
import com.training.tiennguyen.flicksproject.activities.VideoActivity;
import com.training.tiennguyen.flicksproject.constants.IntentConstants;
import com.training.tiennguyen.flicksproject.models.MovieModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link MovieViewHolder2}
 *
 * @author TienVNguyen
 */
public class MovieViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.listItemIvImageOnly)
    public ImageView ivImageOnly;
    @BindView(R.id.listItemIvPlayButton)
    ImageView ivPlayButton;

    private Context mContext;
    private List<MovieModel> mMovies;

    /**
     * Constructor
     *
     * @param context {@link Context}
     * @param view    {@link View}
     * @param movies  {@link List<MovieModel>
     */
    public MovieViewHolder2(final Context context, final View view, final List<MovieModel> movies) {
        super(view);
        ButterKnife.bind(this, view);
        this.mContext = context;
        this.mMovies = movies;

        ivPlayButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            final MovieModel movieModel = mMovies.get(position);
            if (movieModel != null) {
                final Intent intent = new Intent(mContext, VideoActivity.class);
                intent.putExtra(IntentConstants.VIDEO_DETAILS, movieModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }
    }
}
