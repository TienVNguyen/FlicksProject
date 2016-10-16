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
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayerView;
import com.training.tiennguyen.flicksproject.R;
import com.training.tiennguyen.flicksproject.api.VideoApi;
import com.training.tiennguyen.flicksproject.constants.IntentConstants;
import com.training.tiennguyen.flicksproject.models.MovieModel;
import com.training.tiennguyen.flicksproject.models.TrailerModel;
import com.training.tiennguyen.flicksproject.models.TrailersResponseModel;
import com.training.tiennguyen.flicksproject.utils.ConfigurationUtils;
import com.training.tiennguyen.flicksproject.utils.RetrofitUtils;

import java.util.ArrayList;
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
    private static final int RECOVERY_REQUEST = 1;

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

    /**
     * PlaybackEventListener
     */
    private PlaybackEventListener playbackEventListener = new PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };
    /**
     * YouTubePlayer.PlayerStateChangeListener
     */
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
            Log.d("DETAILS_YOUTUBE", "onAdStarted");
        }

        @Override
        public void onError(ErrorReason arg0) {
            Log.d("DETAILS_YOUTUBE_ERROR", arg0.toString());
        }

        @Override
        public void onLoaded(String arg0) {
            Log.d("DETAILS_YOUTUBE", arg0);
        }

        @Override
        public void onLoading() {
            Log.d("DETAILS_YOUTUBE", "onLoading");
        }

        @Override
        public void onVideoEnded() {
            Log.d("DETAILS_YOUTUBE", "onVideoEnded");
        }

        @Override
        public void onVideoStarted() {
            Log.d("DETAILS_YOUTUBE", "onVideoStarted");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        initViews();
    }

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

        loadVideo(movieModel);
    }

    /**
     * Load Video
     *
     * @param movieModel {@link MovieModel}
     */
    private void loadVideo(MovieModel movieModel) {
        if (ConfigurationUtils.isConnectInternet(getApplicationContext())) {
            getVideoSources(movieModel.getmId());
        } else {
            dialogMessageForInternetRequest();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            final MovieModel movieModel = data.getParcelableExtra(IntentConstants.VIDEO_DETAILS);
            if (movieModel == null)
                return;

            getVideoSources(movieModel.getmId());
        }
    }

    /**
     * Dialog Message For Internet Request
     */
    private void dialogMessageForInternetRequest() {
        new AlertDialog.Builder(DetailsActivity.this)
                .setTitle(getString(R.string.connection_error_title))
                .setMessage(getString(R.string.connection_error_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                        startActivityForResult(intent, 0);
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
     * @param videoSources {@link String}
     * @return {@link YouTubePlayer.OnInitializedListener}
     */
    @NonNull
    private YouTubePlayer.OnInitializedListener getOnListenerForYoutubePlayer(final List<String> videoSources) {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                onInitializationSuccessForYouTubePlayerListener(youTubePlayer, b, videoSources);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                onInitializationFailureForYouTubePlayerListener(youTubeInitializationResult);
            }
        };
    }

    /**
     * onInitializationFailureForYouTubePlayerListener
     *
     * @param youTubeInitializationResult {@link YouTubeInitializationResult}
     */
    private void onInitializationFailureForYouTubePlayerListener(
            final YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(DetailsActivity.this, RECOVERY_REQUEST).show();
        } else {
            String error = getString(R.string.player_error) + youTubeInitializationResult.toString();
            Toast.makeText(DetailsActivity.this, error, Toast.LENGTH_LONG).show();
        }
        yTPVPlay.setVisibility(View.GONE);
    }

    /**
     * onInitializationSuccessForYouTubePlayerListener
     *
     * @param youTubePlayer {@link YouTubePlayer}
     * @param b             {@link Boolean}
     * @param videoSources  {@link List<String>}
     */
    private void onInitializationSuccessForYouTubePlayerListener(
            final YouTubePlayer youTubePlayer, final boolean b, final List<String> videoSources) {

        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!b) {
            youTubePlayer.cueVideos(videoSources);
        }
        yTPVPlay.setVisibility(View.VISIBLE);
    }

    /**
     * Get Video Sources
     *
     * @param id {@link String}
     */
    private void getVideoSources(final int id) {
        VideoApi videoApi = RetrofitUtils.get(getString(R.string.api_key_themoviedb)).create(VideoApi.class);
        videoApi.getVideo(id)
                .enqueue(new Callback<TrailersResponseModel>() {
                    @Override
                    public void onResponse(Call<TrailersResponseModel> call, Response<TrailersResponseModel> response) {
                        apiQuerySuccess(response);
                    }

                    @Override
                    public void onFailure(Call<TrailersResponseModel> call, Throwable t) {
                        apiQueryFailed(t);
                    }
                });
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
    private void apiQuerySuccess(final Response<TrailersResponseModel> response) {
        Log.d("RESPONSE_VIDEOS", String.valueOf(response.isSuccessful()));

        List<TrailerModel> list = response.body().getmVideos();
        List<String> sources = new ArrayList<>();
        if (list.size() > 0) {
            for (TrailerModel model : list) {
                sources.add(model.getmSource());
            }
        }

        if (sources.size() > 0) {
            yTPVPlay.setVisibility(View.VISIBLE);
            yTPVPlay.initialize(getString(R.string.api_key_youtube), getOnListenerForYoutubePlayer(sources));
        } else {
            yTPVPlay.setVisibility(View.GONE);
        }
    }
}
