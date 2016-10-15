/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
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
import com.training.tiennguyen.flicksproject.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * {@link VideoActivity}
 *
 * @author TienVNguyen
 */
public class VideoActivity extends YouTubeBaseActivity {
    @BindView(R.id.yTPVPlayFull)
    protected YouTubePlayerView yTPVPlayFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
    }

    /**
     * Init Views
     */
    private void initViews() {
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        if (intent == null)
            return;

        final MovieModel movieModel = intent.getParcelableExtra(IntentConstants.VIDEO_DETAILS);
        if (movieModel == null)
            return;

        yTPVPlayFull.initialize(getString(R.string.api_key_youtube),
                getOnListenerForYoutubePlayer(getVideoSources(movieModel.getmId())));
    }

    /**
     * On Initialized Listener For Youtube Player
     *
     * @param videoSources {@link List<String>}
     * @return {@link YouTubePlayer.OnInitializedListener}
     */
    @NonNull
    private YouTubePlayer.OnInitializedListener getOnListenerForYoutubePlayer(final List<String> videoSources) {
        return new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                youTubePlayer.setPlaybackEventListener(playbackEventListener);
                if (!b) {
                    youTubePlayer.loadVideos(videoSources);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(VideoActivity.this, "Youtube Failed!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    /**
     * Get Video Source
     *
     * @param id {@link String}
     * @return {@link List<String>}
     */
    private List<String> getVideoSources(final int id) {
        final List<String> videoSources = new ArrayList<>();

        VideoApi videoApi = RetrofitUtils.get(getString(R.string.api_key_themoviedb)).create(VideoApi.class);
        videoApi.getVideo(id)
                .enqueue(new Callback<TrailersResponseModel>() {
                    @Override
                    public void onResponse(Call<TrailersResponseModel> call, Response<TrailersResponseModel> response) {
                        videoSources.addAll(apiQuerySuccess(response));
                    }

                    @Override
                    public void onFailure(Call<TrailersResponseModel> call, Throwable t) {
                        apiQueryFailed(t);
                    }
                });

        return videoSources;
    }

    /**
     * Api Query Failed
     *
     * @param throwable {@link Throwable}
     */
    private void apiQueryFailed(final Throwable throwable) {
        Log.e("ERROR_VIDEOS_FULL", throwable.getMessage());

        yTPVPlayFull.setVisibility(View.GONE);
    }

    /**
     * Api Query Success
     *
     * @param response {@link Response<TrailersResponseModel>}
     */
    private List<String> apiQuerySuccess(final Response<TrailersResponseModel> response) {
        Log.d("RESPONSE_VIDEOS_FULL", String.valueOf(response.isSuccessful()));

        List<TrailerModel> list = response.body().getmVideos();
        List<String> sources = new ArrayList<>();
        if (list.size() > 0) {
            for (TrailerModel model : list) {
                sources.add(model.getmSource());
            }
        }

        if (sources.size() > 0)
            yTPVPlayFull.setVisibility(View.VISIBLE);
        else
            yTPVPlayFull.setVisibility(View.GONE);

        return sources;
    }

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
        }

        @Override
        public void onError(ErrorReason arg0) {
            Log.d("VIDEO_YOUTUBE_ERROR", arg0.toString());
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };
}
