package com.priyo.focus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import static com.priyo.focus.Constants.ARG_PROGRESS;
import static com.priyo.focus.Constants.CHANNEL_ID;
import static com.priyo.focus.Constants.DELAY_DURATION;
import static com.priyo.focus.Constants.NOTIFICATION_ID;

/**
 * Created by Priyabrata Naskar on 02-03-2022.
 */
public class CountDownWorker extends Worker {
    private static final String KEY_INPUT_URL = "KEY_INPUT_URL";
    private static final String KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME";

    private final NotificationManager notificationManager;

    public CountDownWorker(
            @NonNull Context context,
            @NonNull WorkerParameters parameters) {
        super(context, parameters);
        notificationManager = (NotificationManager)
                context.getSystemService(NotificationManager.class);
    }

    private final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Important background job");


    @NonNull
    @Override
    public Result doWork() {
//        Data inputData = getInputData();
//        String inputUrl = inputData.getString(KEY_INPUT_URL);
//        String outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME);
//        // Mark the Worker as important
//        String progress = "Starting Download";
//        setForegroundAsync(createForegroundInfo(progress));
//        download(inputUrl, outputFile);
//        return Result.success();
        Log.d(Constants.TAG, "Start job");

        createNotificationChannel();
        Notification notification = notificationBuilder.build();
        ForegroundInfo foregroundInfo = new ForegroundInfo(NOTIFICATION_ID, notification);
        setForegroundAsync(foregroundInfo);

        for (int i=0; i<100;i++) {
            // we need it to get progress in UI
            setProgressAsync(new Data.Builder().putInt(ARG_PROGRESS, 100).build());
            // update the notification progress
            showProgress(i);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            },DELAY_DURATION);
            //delay(DELAY_DURATION);
        }

        Log.d(Constants.TAG, "Finish job");
        return Result.success();
    }

//    private void download(String inputUrl, String outputFile) {
//        // Downloads a file and updates bytes read
//        // Calls setForegroundAsync(createForegroundInfo(myProgress))
//        // periodically when it needs to update the ongoing Notification.
//    }

    private void showProgress(int progress) {
        Notification notification = notificationBuilder
                .setProgress(100, progress, false)
                .build();
        ForegroundInfo foregroundInfo = new ForegroundInfo(NOTIFICATION_ID, notification);
        setForegroundAsync(foregroundInfo);
    }

//    @NonNull
//    private ForegroundInfo createForegroundInfo(@NonNull String progress) {
//        // Build a notification using bytesRead and contentLength
//
//        Context context = getApplicationContext();
//        String id = context.getString(R.string.notification_channel_id);
//        String title = context.getString(R.string.notification_title);
//        String cancel = context.getString(R.string.cancel_download);
//        // This PendingIntent can be used to cancel the worker
//        PendingIntent intent = WorkManager.getInstance(context)
//                .createCancelPendingIntent(getId());
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel();
//        }
//
//        Notification notification = new NotificationCompat.Builder(context, id)
//                .setContentTitle(title)
//                .setTicker(title)
//                .setSmallIcon(R.drawable.ic_work_notification)
//                .setOngoing(true)
//                // Add the cancel action to the notification which can
//                // be used to cancel the worker
//                .addAction(android.R.drawable.ic_delete, cancel, intent)
//                .build();
//
//        return new ForegroundInfo(notification);
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private void createChannel() {
//        // Create a Notification channel
//    }

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