package com.projects.jezinka.pogoda;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendQueryForData();
    }

    private void sendQueryForData() {
        final Context mContext = this;
        WeatherService weatherService = WeatherService.retrofit.create(WeatherService.class);
        final Call<JsonObject> call = weatherService.loadData();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                JsonObject body = response.body();

                if (body != null) {
                    TextView textView = findViewById(R.id.weatherTV);
                    textView.setText(body.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("MAIN", t.getLocalizedMessage());
                Toast.makeText(mContext, "connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
