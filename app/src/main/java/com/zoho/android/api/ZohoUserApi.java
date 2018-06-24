package com.zoho.android.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZohoUserApi {

    public static ZohoUserService createGitHubService() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://reqres.in");

        return builder.build().create(ZohoUserService.class);
    }
}
