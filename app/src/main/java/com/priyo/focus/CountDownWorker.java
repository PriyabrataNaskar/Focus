package com.priyo.focus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static com.priyo.focus.Constants.ARG_PROGRESS;
import static com.priyo.focus.Constants.CHANNEL_ID;
import static com.priyo.focus.Constants.NOTIFICATION_ID;
import static com.priyo.focus.Constants.TAG;

/**
 * Created by Priyabrata Naskar on 02-03-2022.
 */
public class CountDownWorker extends Worker {

    private final NotificationManager notificationManager;
    //private final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Important background job");

    public CountDownWorker(
            @NonNull Context context,
            @NonNull WorkerParameters parameters) {
        super(context, parameters);
        notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        //get the data
        Data inputData = getInputData();
        int inputTime = inputData.getInt(Constants.KEY_COUNTDOWN_TIME,0);
        Log.d(TAG, "Input Data: " + inputTime);
        //int timeInSecond = 60*inputTime;

        Log.d(Constants.TAG, "Start job");

        //createNotificationChannel();
        //Notification notification = notificationBuilder.build();

        //For foreground service
        ForegroundInfo foregroundInfo = createForegroundInfo(0);
        setForegroundAsync(foregroundInfo);

        for (int i = inputTime; i > 0; i--) {
            int percent = FormatUtils.calculatePercentage(inputTime,i);

            // we need it to get progress in UI
            setProgressAsync(new Data.Builder().putInt(ARG_PROGRESS, i).putInt(Constants.PERCENTAGE,percent).build());
            // update the notification progress
            showProgress(i);
            try {
                Thread.sleep(Constants.DELAY_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        Log.d(Constants.TAG, "Finish job");
        return Result.success();
    }

    //Update Progress to the notification
    private void showProgress(int progress) {
        ForegroundInfo foregroundInfo = createForegroundInfo(progress);
        setForegroundAsync(foregroundInfo);
    }

    @NonNull
    private ForegroundInfo createForegroundInfo(@NonNull int progress) {
        // Build a notification using bytesRead and contentLength
        Context context = getApplicationContext();
        String id = CHANNEL_ID;
        String title = context.getString(R.string.notification_title);
        //String cancel = context.getString(R.string.cancel_download);
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        Notification notification = new NotificationCompat.Builder(context, id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setTicker(title)
                .setOngoing(true)
                .setContentText(FormatUtils.formatTime(progress))
                // Add the cancel action to the notification which can
                // be used to cancel the worker
                //.addAction(android.R.drawable.ic_delete, cancel, intent)
                .build();

        return new ForegroundInfo(NOTIFICATION_ID,notification);
    }

    /**
     * Creates Notification Channel
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (notificationChannel == null) {
                notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, Constants.TAG, NotificationManager.IMPORTANCE_LOW));
            }
        }
    }
}