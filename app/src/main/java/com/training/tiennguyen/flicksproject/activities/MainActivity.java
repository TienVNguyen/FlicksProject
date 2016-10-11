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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.training.tiennguyen.flicksproject.R;
import com.training.tiennguyen.flicksproject.adapters.MovieAdapter;
import com.training.tiennguyen.flicksproject.api.MovieApi;
import com.training.tiennguyen.flicksproject.models.MovieModel;
import com.training.tiennguyen.flicksproject.models.NowPlaying;
import com.training.tiennguyen.flicksproject.utils.ConfigurationUtils;
import com.training.tiennguyen.flicksproject.utils.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * {@link MainActivity}
 *
 * @author TienVNguyen
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvEmptyList)
    protected TextView tvEmptyList;
    @BindView(R.id.pbMovies)
    protected ProgressBar pbMovies;
    @BindView(R.id.sRLMovies)
    protected SwipeRefreshLayout sRLMovies;
    @BindView(R.id.rvMovies)
    protected RecyclerView rvMovies;

    private List<MovieModel> mMovies = new ArrayList<>();
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        populateDataForList();
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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViewsForRecyclerView();
        initViewsForSwipeRefreshLayout();
    }

    /**
     * Init Views For Recycler View
     */
    private void initViewsForRecyclerView() {
        mMovieAdapter = new MovieAdapter(getApplicationContext(), R.layout.item_movie, mMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rvMovies.setHasFixedSize(true);
        rvMovies.setAdapter(mMovieAdapter);
        rvMovies.setItemAnimator(new SlideInUpAnimator());
    }

    /**
     * Init Views For Swipe Refresh Layout
     */
    private void initViewsForSwipeRefreshLayout() {
        sRLMovies.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateDataForList();
            }
        });
        sRLMovies.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * Populate Data For List
     */
    private void populateDataForList() {
        if (ConfigurationUtils.isConnectInternet(getApplicationContext())) {
            fetchDataForList();
        } else {
            dialogMessageForInternetRequest();
        }
    }

    /**
     * Dialog Message For Internet Request
     */
    private void dialogMessageForInternetRequest() {
        new AlertDialog.Builder(MainActivity.this)
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
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Fetch Data For List
     */
    private void fetchDataForList() {
        MovieApi mMovieApi = RetrofitUtils.get(getString(R.string.api_key)).create(MovieApi.class);
        mMovieApi.getNowPlaying()
                .enqueue(new Callback<NowPlaying>() {
                    @Override
                    public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                        apiQuerySuccess(response);
                    }

                    @Override
                    public void onFailure(Call<NowPlaying> call, Throwable t) {
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
        Log.e("ERROR", throwable.getMessage());
    }

    /**
     * Api Query Success
     *
     * @param response {@link Response}
     */
    private void apiQuerySuccess(Response<NowPlaying> response) {
        Log.d("RESPONSE", String.valueOf(response.isSuccessful()));

        clearAllElementsForRv();
        addAllElementsForRv(response.body().getMovies());
        setVisibleForRelatedViews();
    }

    /**
     * Set Visible For Related Views
     */
    private void setVisibleForRelatedViews() {
        if (mMovies.size() > 0) {
            tvEmptyList.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.VISIBLE);
        }
        pbMovies.setVisibility(View.GONE);
        sRLMovies.setRefreshing(false);
    }

    /**
     * Clean all elements of the recycler
     */
    public void clearAllElementsForRv() {
        mMovies.clear();
        mMovieAdapter.notifyDataSetChanged();
    }

    /**
     * Add a list of items
     *
     * @param list {@link List}<{@link MovieModel}>
     */
    public void addAllElementsForRv(List<MovieModel> list) {
        mMovies.addAll(list);
        mMovieAdapter.notifyDataSetChanged();
    }
}
