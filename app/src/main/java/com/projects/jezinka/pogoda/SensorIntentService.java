package com.projects.jezinka.pogoda;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.List;

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
        Timber.plant(new Timber.DebugTree());
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
        final Call<List<Sensor>> call = weatherService.loadData();

        call.enqueue(new Callback<List<Sensor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Sensor>> call, @NonNull Response<List<Sensor>> response) {

                List<Sensor> sensors = response.body();
                if (sensors != null) {
                    notificationService.checkSensorsState(sensors, mContext);

                    Sensor sensor = getSensorByLabel(sensors, Constants.BALKON);

                    if (sensor != null) {
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(mContext, SensorWidgetProvider.class));
                        SensorWidgetProvider.updateSensorWidgets(mContext, appWidgetManager, sensor, widgetIds);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Sensor>> call, @NonNull Throwable t) {
                Timber.e(t.getLocalizedMessage());
                Toast.makeText(mContext, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private Sensor getSensorByLabel(List<Sensor> sensors, String label) {
        if (sensors != null) {
            for (Sensor sensor : sensors) {
                if (sensor.getLabel().equals(label)) {
                    return sensor;
                }
            }
        }
        return null;
    }
}
