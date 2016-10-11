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
import com.training.tiennguyen.flicksproject.constants.UrlConstant;

/**
 * {@link }
 *
 * @author TienVNguyen
 */
public class MovieModel implements Parcelable {
    /**
     * title
     */
    @SerializedName("title")
    private String mTitle;

    /**
     * overview
     */
    @SerializedName("overview")
    private String mOverview;

    /**
     * poster_path
     */
    @SerializedName("poster_path")
    private String mPosterPath;

    /**
     * backdrop_path
     */
    @SerializedName("backdrop_path")
    private String mBackdropPath;

    /**
     * Constructor
     */
    public MovieModel() {
    }

    /**
     * Constructor
     *
     * @param in {@link Parcel}
     */
    private MovieModel(final Parcel in) {
        mTitle = in.readString();
        mOverview = in.readString();
        mPosterPath = in.readString();
        mBackdropPath = in.readString();
    }

    /**
     * Creator
     */
    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mPosterPath);
        dest.writeString(mBackdropPath);
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmPosterPath() {
        return UrlConstant.BASE_URL + mPosterPath;
    }

    public String getmBackdropPath() {
        return UrlConstant.BASE_URL + mBackdropPath;
    }
}