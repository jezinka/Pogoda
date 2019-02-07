package com.projects.jezinka.pogoda;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class SensorIntentService extends IntentService {

    @Inject
    NotificationService notificationService;

    public SensorIntentService() {
        super("SensorIntentService");
        Scope appScope = Toothpick.openScope(this);
        appScope.installModules(new Module() {{
            bind(NotificationService.class).toInstance(new NotificationService());
        }});
        Toothpick.inject(this, appScope);
    }

    public static void startActionUpdateWidgets(Context context) {
        Intent intent = new Intent(context, SensorIntentService.class);
        intent.setAction(Constants.ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION_UPDATE_WIDGET.equals(action) || Constants.ACTION_REFRESH_WIDGET.equals(action)) {
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
                    Timber.i("Server response with: %s", body.toString());
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
                Timber.e(t.getLocalizedMessage());
                Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Sensor createSensorsFromJsonObject(JsonObject body) {
        List<Sensor> sensorList = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : body.getAsJsonObject(getString(R.string.readings)).entrySet()) {
            String label = SensorJsonHelper.getLabelFromJson(body, entry.getKey());
            Sensor sensor = new Sensor(entry, label);
            sensorList.add(sensor);
        }

        notificationService.checkSensorsState(sensorList, this);

        for (Sensor sensor : sensorList) {
            if (sensor.getLabel().equals(Constants.BALKON)) {
                return sensor;
            }
        }
        return null;
    }
}
