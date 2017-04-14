package com.example.damkarlearning;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Test", "asdasd");

        CountDownTimer cdt = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.i("Test", "Countdown seconds remaining: " + millisUntilFinished / 1000);
            }

            @TargetApi(Build.VERSION_CODES.M)
            public void onFinish() {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyService.this);
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                mBuilder.setContentTitle("Notification!");
                mBuilder.setContentText("Main lagi dong!!");

                Intent notificationIntent = new Intent(MyService.this, MenuActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(MyService.this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(contentIntent);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // notificationID allows you to update the notification later on.
                mNotificationManager.notify(0, mBuilder.build());
            }
        };
        cdt.start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
