package com.projects.jezinka.pogoda;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SensorsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new SensorsAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

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
                    Sensor[] sensors = createSensorsFromJsonObject(body);
                    adapter.updateResults(sensors);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("MAIN", t.getLocalizedMessage());
                Toast.makeText(mContext, "connection Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private Sensor[] createSensorsFromJsonObject(JsonObject body) {
        int sensorsSize = body.getAsJsonObject("readings").entrySet().size();
        Sensor[] sensors = new Sensor[sensorsSize];

        int i = 0;
        for (Map.Entry<String, JsonElement> entry : body.getAsJsonObject("readings").entrySet()) {
            String label = body.getAsJsonObject("sensors").get(entry.getKey()).getAsJsonObject().get("label").toString().replaceAll("\"", "");
            sensors[i] = new Sensor(entry, label);
            i++;
        }

        return sensors;
    }
}
