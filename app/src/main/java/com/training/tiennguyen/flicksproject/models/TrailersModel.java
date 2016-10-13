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
 * {@link TrailersModel}
 *
 * @author TienVNguyen
 */
public class TrailersModel implements Parcelable {
    /**
     * youtube
     */
    @SerializedName("youtube")
    private List<VideoModel> mVideos;

    private TrailersModel(Parcel in) {
        mVideos = in.createTypedArrayList(VideoModel.CREATOR);
    }

    public static final Creator<TrailersModel> CREATOR = new Creator<TrailersModel>() {
        @Override
        public TrailersModel createFromParcel(Parcel in) {
            return new TrailersModel(in);
        }

        @Override
        public TrailersModel[] newArray(int size) {
            return new TrailersModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mVideos);
    }

    public List<VideoModel> getmVideos() {
        return mVideos;
    }
}
