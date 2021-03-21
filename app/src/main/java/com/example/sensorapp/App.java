package com.example.sensorapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "Channel1";
   public static  final String CHANNEL_2_ID = "Channel2";
    @Override
    public void onCreate() {
        super.onCreate();
      notification();
    }
    private void notification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel n1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            n1.setDescription("Channel 1");

            NotificationChannel n2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel2",
                    NotificationManager.IMPORTANCE_LOW
            );
            n1.setDescription("Channel 2");
  NotificationManager n = getSystemService(NotificationManager.class);
  n.createNotificationChannel(n2);
  n.createNotificationChannel(n1);





        }

    }
}
