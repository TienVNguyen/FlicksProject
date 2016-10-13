/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RatingBar;
import android.widget.TextView;

import com.training.tiennguyen.flicksproject.R;
import com.training.tiennguyen.flicksproject.constants.IntentConstants;
import com.training.tiennguyen.flicksproject.models.MovieModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link DetailsActivity}
 *
 * @author TienVNguyen
 */
public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.detailsTvTitle)
    protected TextView tvTitle;
    @BindView(R.id.detailsTvReleaseDate)
    protected TextView tvReleaseDate;
    @BindView(R.id.detailsRbVoteAverage)
    protected RatingBar rbVoteAverage;
    @BindView(R.id.detailsTvOverview)
    protected TextView tvOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    /**
     * Init Views
     */
    private void initViews() {
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        if (intent == null)
            return;

        final MovieModel movieModel = intent.getParcelableExtra(IntentConstants.VIDEO_DETAILS);
        if (movieModel == null)
            return;

        tvTitle.setText(movieModel.getmTitle());
        tvReleaseDate.setText(movieModel.getmReleaseDate());
        rbVoteAverage.setRating(Float.parseFloat(movieModel.getmVoteAverage()));
        tvOverview.setText(movieModel.getmOverview());
    }
}
