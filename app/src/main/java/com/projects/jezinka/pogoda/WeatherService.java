package com.projects.jezinka.pogoda;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface WeatherService {

    @GET("data.json")
    Call<JsonObject> loadData();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_ADDRESS)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
