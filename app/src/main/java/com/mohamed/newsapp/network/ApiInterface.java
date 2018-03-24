package com.mohamed.newsapp.network;

import com.mohamed.newsapp.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by mohamed on 09/03/18.
 */

public interface ApiInterface {
    @GET("top-headlines?country=us")
    Call<NewsResponse> getNews(@Query("category") String category, @Query("apiKey") String apiKey);
}
