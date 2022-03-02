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
        //Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT).show();
//        Data inputData = getInputData();
//        String inputUrl = inputData.getString(KEY_INPUT_URL);
//        String outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME);
//        // Mark the Worker as important
//        String progress = "Starting Download";
//        setForegroundAsync(createForegroundInfo(progress));
//        download(inputUrl, outputFile);
//        return Result.success();
        Log.d(Constants.TAG, "Start job");

        //createNotificationChannel();
        //Notification notification = notificationBuilder.build();

        //For foreground service
        //ForegroundInfo foregroundInfo = new ForegroundInfo(NOTIFICATION_ID, notification);
        //setForegroundAsync(foregroundInfo);

        for (int i = 0; i < 100; i++) {
            // we need it to get progress in UI
            setProgressAsync(new Data.Builder().putInt(ARG_PROGRESS, i).build());
            // update the notification progress
            showProgress(i);
            try {
                Thread.sleep(1000);
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
                //.setSmallIcon(R.drawable.ic_work_notification)
                .setOngoing(true)
                .setContentText(String.valueOf(progress))
                // Add the cancel action to the notification which can
                // be used to cancel the worker
                //.addAction(android.R.drawable.ic_delete, cancel, intent)
                .build();

        return new ForegroundInfo(NOTIFICATION_ID,notification);
    }

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