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

/**
 * {@link TrailerModel}
 *
 * @author TienVNguyen
 */
public class TrailerModel implements Parcelable {
    /**
     * name
     */
    @SerializedName("name")
    private String mName;

    /**
     * source
     */
    @SerializedName("source")
    private String mSource;

    /**
     * size
     */
    @SerializedName("size")
    private String mSize;

    /**
     * type
     */
    @SerializedName("type")
    private String mType;

    /**
     * Constructor
     */
    public TrailerModel() {
    }

    private TrailerModel(Parcel in) {
        mName = in.readString();
        mSource = in.readString();
    }

    public static final Creator<TrailerModel> CREATOR = new Creator<TrailerModel>() {
        @Override
        public TrailerModel createFromParcel(Parcel in) {
            return new TrailerModel(in);
        }

        @Override
        public TrailerModel[] newArray(int size) {
            return new TrailerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mSource);
        dest.writeString(mSize);
        dest.writeString(mType);
    }

    public String getmName() {
        return mName;
    }

    public String getmSource() {
        return mSource;
    }

    public String getmSize() {
        return mSize;
    }

    public String getmType() {
        return mType;
    }
}
