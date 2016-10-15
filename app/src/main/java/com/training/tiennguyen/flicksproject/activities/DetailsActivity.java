/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.training.tiennguyen.flicksproject.R;
import com.training.tiennguyen.flicksproject.api.VideoApi;
import com.training.tiennguyen.flicksproject.constants.IntentConstants;
import com.training.tiennguyen.flicksproject.models.MovieModel;
import com.training.tiennguyen.flicksproject.models.TrailerModel;
import com.training.tiennguyen.flicksproject.models.TrailersResponseModel;
import com.training.tiennguyen.flicksproject.utils.ConfigurationUtils;
import com.training.tiennguyen.flicksproject.utils.RetrofitUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * {@link DetailsActivity}
 *
 * @author TienVNguyen
 */
public class DetailsActivity extends YouTubeBaseActivity {
    @BindView(R.id.detailsYtpvPlay)
    protected YouTubePlayerView yTPVPlay;
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
        rbVoteAverage.setRating(movieModel.getmVoteAverage());
        tvOverview.setText(movieModel.getmOverview());

        if (ConfigurationUtils.isConnectInternet(getApplicationContext())) {
            yTPVPlay.initialize(getString(R.string.api_key_youtube),
                    getOnListenerForYoutubePlayer(getVideoSource(movieModel.getmId())));
        } else {
            dialogMessageForInternetRequest();
        }
    }

    /**
     * Dialog Message For Internet Request
     */
    private void dialogMessageForInternetRequest() {
        new AlertDialog.Builder(DetailsActivity.this)
                .setTitle("NO CONNECTION")
                .setMessage("There is no Internet connection. Please go to setting!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yTPVPlay.setVisibility(View.GONE);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * On Initialized Listener For Youtube Player
     *
     * @param videoSource {@link String}
     * @return {@link YouTubePlayer.OnInitializedListener}
     */
    @NonNull
    private YouTubePlayer.OnInitializedListener getOnListenerForYoutubePlayer(final String videoSource) {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(videoSource);
                yTPVPlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(DetailsActivity.this, "Youtube Failed!", Toast.LENGTH_SHORT).show();
                yTPVPlay.setVisibility(View.GONE);
            }
        };
    }

    /**
     * Get Video Source
     *
     * @param id {@link String}
     * @return {@link String}
     */
    private String getVideoSource(final int id) {
        final StringBuilder videoSource = new StringBuilder();

        VideoApi videoApi = RetrofitUtils.get(getString(R.string.api_key_themoviedb)).create(VideoApi.class);
        videoApi.getVideo(id)
                .enqueue(new Callback<TrailersResponseModel>() {
                    @Override
                    public void onResponse(Call<TrailersResponseModel> call, Response<TrailersResponseModel> response) {
                        videoSource.append(apiQuerySuccess(response));
                    }

                    @Override
                    public void onFailure(Call<TrailersResponseModel> call, Throwable t) {
                        apiQueryFailed(t);
                    }
                });

        return videoSource.toString();
    }

    /**
     * Api Query Failed
     *
     * @param throwable {@link Throwable}
     */
    private void apiQueryFailed(final Throwable throwable) {
        Log.e("ERROR_VIDEOS", throwable.getMessage());

        yTPVPlay.setVisibility(View.GONE);
    }

    /**
     * Api Query Success
     *
     * @param response {@link Response<TrailersResponseModel>}
     */
    private String apiQuerySuccess(Response<TrailersResponseModel> response) {
        Log.d("RESPONSE_VIDEOS", String.valueOf(response.isSuccessful()));

        List<TrailerModel> list = response.body().getmVideos();
        if (list.size() > 0) {
            yTPVPlay.setVisibility(View.VISIBLE);
            return list.get(0).getmSource();
        }
        yTPVPlay.setVisibility(View.GONE);
        return "";
    }
}
