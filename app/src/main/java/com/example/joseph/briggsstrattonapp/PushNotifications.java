package com.example.joseph.briggsstrattonapp;

/**
 * Created by Kyle on 11/27/2016.
 */
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class PushNotifications extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.battery_img)
                .setContentTitle("Low Voltage!")
                .setContentText("You might want to start your lawnmower; low battery voltage detected!");

        mNotificationManager.notify(123, notification.build());
    }
}