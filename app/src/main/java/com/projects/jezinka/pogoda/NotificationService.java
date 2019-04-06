package com.projects.jezinka.pogoda;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import java.util.List;

import timber.log.Timber;

public class NotificationService {

    public void createNotificationChannel(Context mContext) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getResources().getString(R.string.sensor_channel);
            String description = mContext.getResources().getString(R.string.sensor_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Constants.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                Timber.i("Notification channel created");
            }
        }
    }

    public void checkSensorsState(List<Sensor> sensors, Context mcontext) {
        for (Sensor sensor : sensors) {
            if (sensor.isSensorDead()) {
                sendDeadSensorNotification(sensor, mcontext);
            } else if (sensor.batteryNeedRecharge()) {
                sendRechargeNotification(sensor, mcontext);
            }
        }
    }

    private void sendDeadSensorNotification(Sensor sensor, Context mContext) {
        CharSequence message = TextUtils.concat(sensor.getLabel(), mContext.getResources().getString(R.string.dead_sensor_notification_text), " ", sensor.getTimestamp());
        String title = mContext.getResources().getString(R.string.dead_sensor_notification_title);
        sendNotification(sensor, title, message, mContext);
    }

    private void sendRechargeNotification(Sensor sensor, Context mContext) {
        CharSequence message = TextUtils.concat(sensor.getLabel(), mContext.getResources().getString(R.string.recharge_notification_text));
        String title = mContext.getResources().getString(R.string.recharge_notification_title);
        sendNotification(sensor, title, message, mContext);
    }

    private void sendNotification(Sensor sensor, String title, CharSequence message, Context mContext) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_battery)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setOngoing(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify((int) sensor.getId(), mBuilder.build());
        Timber.i("Notification send%s", message);
    }
}
