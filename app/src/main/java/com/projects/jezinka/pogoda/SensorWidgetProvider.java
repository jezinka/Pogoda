package com.projects.jezinka.pogoda;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class SensorWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Sensor sensor, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sensor_widget_provider);
        views.setTextViewText(R.id.appwidget_text, sensor.getTemperature());
        views.setTextViewText(R.id.widget_timestamp_tv, sensor.getTimestamp());

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.sensor_widget, pendingIntent);

        Intent refreshIntent = new Intent(context, SensorIntentService.class);
        refreshIntent.setAction(Constants.ACTION_REFRESH_WIDGET);
        PendingIntent refreshPendingIntent = PendingIntent.getService(
                context,
                0,
                refreshIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.refresh_ic, refreshPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SensorIntentService.startActionUpdateWidgets(context);
    }

    public static void updateSensorWidgets(Context context, AppWidgetManager appWidgetManager, Sensor sensor, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, sensor, appWidgetId);
        }
    }
}

