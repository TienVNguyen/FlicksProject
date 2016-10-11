/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.api;

import com.training.tiennguyen.flicksproject.models.NowPlaying;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * {@link MovieApi}
 *
 * @author TienVNguyen
 */
public interface MovieApi {

    /**
     * Now Playing
     *
     * @return Call<NowPlaying>
     */
    @GET("now_playing")
    Call<NowPlaying> getNowPlaying();
}
