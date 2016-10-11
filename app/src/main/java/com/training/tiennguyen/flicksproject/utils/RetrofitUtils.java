/*
 * Copyright (c) 2016. Self Training Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by TienVNguyen <tien.workinfo@gmail.com - tien.workinfo@icloud.com>, October 2015
 */

package com.training.tiennguyen.flicksproject.utils;

import com.training.tiennguyen.flicksproject.constants.UrlConstant;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * {@link }
 *
 * @author TienVNguyen
 */
public class RetrofitUtils {
    /**
     * Subclass
     *
     * @param apiKey {@link String}
     * @return {@link Retrofit}
     */
    public static Retrofit get(final String apiKey) {
        return new Retrofit.Builder()
                .baseUrl(UrlConstant.BASE_CONSTANT)
                .client(createClient(apiKey))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Create Client
     *
     * @param apiKey {@link String}
     * @return {@link OkHttpClient}
     */
    private static OkHttpClient createClient(final String apiKey) {
        return new OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor(apiKey))
                .build();
    }

    /**
     * API key Interceptor
     *
     * @param apiKey {@link String}
     * @return {@link Interceptor}
     */
    private static Interceptor apiKeyInterceptor(final String apiKey) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(createRequest(chain, apiKey));
            }
        };
    }

    /**
     * Create Request
     *
     * @param chain  {@link Interceptor.Chain}
     * @param apiKey {@link String}
     * @return {@link Request}
     */
    private static Request createRequest(final Interceptor.Chain chain, final String apiKey) {
        Request request = chain.request();
        return request.newBuilder()
                .url(createUrl(request, apiKey))
                .build();
    }

    /**
     * Create Url
     *
     * @param request {@link Request}
     * @param apiKey  {@link String}
     * @return {@link HttpUrl}
     */
    private static HttpUrl createUrl(final Request request, final String apiKey) {
        return request.url()
                .newBuilder()
                .addQueryParameter(UrlConstant.BASE_API, apiKey)
                .build();
    }
}
