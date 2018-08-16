package com.projects.jezinka.pogoda;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SensorIntentService extends IntentService {

    private static final String BALKON = "Balkon";
    private static final String ACTION_UPDATE_WIDGET = "com.projects.jezinka.pogoda.action.update";
    public static final String ACTION_REFRESH_WIDGET = "com.projects.jezinka.pogoda.action.refresh";

    public SensorIntentService() {
        super("SensorIntentService");
    }

    public static void startActionUpdateWidgets(Context context) {
        Intent intent = new Intent(context, SensorIntentService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                refreshWidget();
            } else if (ACTION_REFRESH_WIDGET.equals(action)) {
                refreshWidget();
            }
        }
    }

    private void refreshWidget() {
        final Context mContext = this;
        WeatherService weatherService = WeatherService.retrofit.create(WeatherService.class);
        final Call<JsonObject> call = weatherService.loadData();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                JsonObject body = response.body();
                if (body != null) {
                    Sensor sensor = createSensorsFromJsonObject(body);

                    if (sensor != null) {
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, SensorWidgetProvider.class));
                        SensorWidgetProvider.updateSensorWidgets(mContext, appWidgetManager, sensor, widgetIds);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                Log.e("LOAD_DATA", t.getLocalizedMessage());
                Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Sensor createSensorsFromJsonObject(JsonObject body) {

        for (Map.Entry<String, JsonElement> entry : body.getAsJsonObject(getString(R.string.readings)).entrySet()) {
            String label = SensorJsonHelper.getLabelFromJson(body, entry.getKey());
            if (label.equals(BALKON)) {
                return new Sensor(entry, label);
            }
        }
        return null;
    }
}
