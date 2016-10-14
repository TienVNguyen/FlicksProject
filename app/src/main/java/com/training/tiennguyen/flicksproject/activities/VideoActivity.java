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
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.training.tiennguyen.flicksproject.R;
import com.training.tiennguyen.flicksproject.api.VideoApi;
import com.training.tiennguyen.flicksproject.constants.IntentConstants;
import com.training.tiennguyen.flicksproject.models.MovieModel;
import com.training.tiennguyen.flicksproject.models.TrailersResponseModel;
import com.training.tiennguyen.flicksproject.models.VideoModel;
import com.training.tiennguyen.flicksproject.utils.RetrofitUtils;

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

        yTPVPlayFull.initialize(getString(R.string.api_key), getOnListenerForYoutubePlayer(getVideoSource(movieModel.getmId())));
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
                youTubePlayer.loadVideo(videoSource);
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
     * @return {@link String}
     */
    private String getVideoSource(final String id) {
        final StringBuilder videoSource = new StringBuilder();

        VideoApi videoApi = RetrofitUtils.get(getString(R.string.api_key)).create(VideoApi.class);
        videoApi.getVideo(Integer.parseInt(id))
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
        Log.e("ERROR_VIDEOS_FULL", throwable.getMessage());
    }

    /**
     * Api Query Success
     *
     * @param response {@link Response<TrailersResponseModel>}
     */
    private String apiQuerySuccess(Response<TrailersResponseModel> response) {
        Log.d("RESPONSE_VIDEOS_FULL", String.valueOf(response.isSuccessful()));

        List<VideoModel> list = response.body().getmVideos();
        if (list.size() > 0) {
            return list.get(0).getmSource();
        }
        return "";
    }
}
