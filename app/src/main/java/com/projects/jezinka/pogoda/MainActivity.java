package com.projects.jezinka.pogoda;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class MainActivity extends AppCompatActivity {

    @Inject
    NotificationService notificationService;

    private SensorsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        Scope appScope = Toothpick.openScope(this);
        appScope.installModules(new Module() {{
            bind(NotificationService.class).toInstance(new NotificationService());
        }});
        Toothpick.inject(this, appScope);

        notificationService.createNotificationChannel(this);

        adapter = new SensorsAdapter();
        RecyclerView recyclerView = findViewById(R.id.list);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, Constants.SPAN_COUNT));
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        sendQueryForData();
                    }
                }
        );

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
                    Timber.i("Server response with: %s", body.toString());
                    List<Sensor> sensors = createSensorsFromJsonObject(body);
                    adapter.updateResults(sensors);
                    notificationService.checkSensorsState(sensors, mContext);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Timber.e(t.getLocalizedMessage());
                Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private List<Sensor> createSensorsFromJsonObject(JsonObject body) {
        List<Sensor> sensors = new ArrayList<>();

        for (Map.Entry<String, JsonElement> entry : body.getAsJsonObject(getString(R.string.readings)).entrySet()) {
            String label = SensorJsonHelper.getLabelFromJson(body, entry.getKey());
            sensors.add(new Sensor(entry, label));
        }

        return sensors;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_refresh:
                swipeRefreshLayout.setRefreshing(true);
                sendQueryForData();
                return true;
        }

        // User didn't trigger a refresh, let the superclass handle this action
        return super.onOptionsItemSelected(item);
    }
}
