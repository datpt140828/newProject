package com.example.foodsellappproject.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class CheckDeliveringService extends Service {

    public CheckDeliveringService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 100; i++) {
                    try {
                        synchronized (this) {
                            wait(300);
                        }
                        Intent broadcastIntent = new Intent("CHECK_DELIVERING");
                        broadcastIntent.putExtra("percent", i);
                        sendBroadcast(broadcastIntent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        Log.i("Service", "Service is started");
      //  startForeground(123, new Notification());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Service", "Service is destroyed");
    }
}
