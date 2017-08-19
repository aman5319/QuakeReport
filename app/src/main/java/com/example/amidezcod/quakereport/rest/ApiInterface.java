package com.example.amidezcod.quakereport.rest;

import com.example.amidezcod.quakereport.Model.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by amidezcod on 18/8/17.
 */

public interface ApiInterface {
    @GET("query")
    Call<Example> getEarthquake(@Query("format") String format,
                                @Query("minmagnitude") String minmag,
                                @Query("orderby") String orderby);

}
