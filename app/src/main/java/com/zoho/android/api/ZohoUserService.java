package com.zoho.android.api;

/**
 * Created by Balamurugan on 24/6/18.
 */


import com.zoho.android.vo.Page;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZohoUserService {
    @GET("/api/users")
    Call<Page> getUser(@Query("page") long since, @Query("per_page") int perPage);
}
