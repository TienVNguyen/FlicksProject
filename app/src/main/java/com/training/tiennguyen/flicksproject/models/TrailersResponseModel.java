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
 * {@link TrailersResponseModel}
 *
 * @author TienVNguyen
 */
public class TrailersResponseModel implements Parcelable {
    public static final Creator<TrailersResponseModel> CREATOR = new Creator<TrailersResponseModel>() {
        @Override
        public TrailersResponseModel createFromParcel(Parcel in) {
            return new TrailersResponseModel(in);
        }

        @Override
        public TrailersResponseModel[] newArray(int size) {
            return new TrailersResponseModel[size];
        }
    };
    /**
     * youtube
     */
    @SerializedName("youtube")
    private List<TrailerModel> mVideos;

    private TrailersResponseModel(Parcel in) {
        mVideos = in.createTypedArrayList(TrailerModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mVideos);
    }

    public List<TrailerModel> getmVideos() {
        return mVideos;
    }
}
