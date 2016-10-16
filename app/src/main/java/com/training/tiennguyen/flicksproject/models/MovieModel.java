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
import com.training.tiennguyen.flicksproject.constants.UrlConstants;

/**
 * {@link }
 *
 * @author TienVNguyen
 */
public class MovieModel implements Parcelable {
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
     * id
     */
    @SerializedName("id")
    private int mId;
    /**
     * release_date
     */
    @SerializedName("release_date")
    private String mReleaseDate;
    /**
     * id
     */
    @SerializedName("vote_average")
    private float mVoteAverage;

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
        mId = in.readInt();
        mReleaseDate = in.readString();
        mVoteAverage = in.readFloat();
    }

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
        dest.writeInt(mId);
        dest.writeString(mReleaseDate);
        dest.writeFloat(mVoteAverage);
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmPosterPath() {
        return UrlConstants.BASE_URL + mPosterPath;
    }

    public String getmBackdropPath() {
        return UrlConstants.BASE_URL + mBackdropPath;
    }

    public int getmId() {
        return mId;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public float getmVoteAverage() {
        return mVoteAverage;
    }
}
