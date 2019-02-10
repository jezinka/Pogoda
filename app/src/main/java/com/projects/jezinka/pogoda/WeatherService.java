package com.projects.jezinka.pogoda;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;

public interface WeatherService {

    @GET("sensorFriendly")
    Call<List<Sensor>> loadData();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_ADDRESS)
            .addConverterFactory(MoshiConverterFactory.create())
            .build();
}
