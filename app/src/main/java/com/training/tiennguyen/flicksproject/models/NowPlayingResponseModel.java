/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * {@link NowPlayingResponseModel}
 *
 * @author TienVNguyen
 */
public class NowPlayingResponseModel implements Parcelable {
    /**
     * results
     */
    @SerializedName("results")
    private List<MovieModel> mMovies;

    private NowPlayingResponseModel(Parcel in) {
        mMovies = in.createTypedArrayList(MovieModel.CREATOR);
    }

    public static final Creator<NowPlayingResponseModel> CREATOR = new Creator<NowPlayingResponseModel>() {
        @Override
        public NowPlayingResponseModel createFromParcel(Parcel in) {
            return new NowPlayingResponseModel(in);
        }

        @Override
        public NowPlayingResponseModel[] newArray(int size) {
            return new NowPlayingResponseModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mMovies);
    }

    public List<MovieModel> getmMovies() {
        return mMovies;
    }
}
