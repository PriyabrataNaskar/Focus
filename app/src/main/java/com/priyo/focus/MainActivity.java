package com.priyo.focus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

import static com.priyo.focus.Constants.ARG_PROGRESS;
import static com.priyo.focus.Constants.PERCENTAGE;
import static com.priyo.focus.Constants.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    NumberPicker timePicker;
    MaterialButton startButton;
    MaterialButton pauseButton;

    MaterialButton resumeButton;
    MaterialButton exitButton;

    CircularProgressIndicator progressIndicator;

    //cards for set timer and countdown
    MaterialCardView cardTimePicker;
    MaterialCardView cardCountDownTimer;

    TextView stats;
    String[] timerValues = {"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "70", "80", "90", "100", "110", "120", "130", "140", "150", "160", "170", "180"};
    //LinearLayout resumeExitViews;

    TextView timerCountDown;
    WorkManager workManager;
    OneTimeWorkRequest workRequest;

    int numberPickerValueIndex;
    int remainingTime;

    //Congrats Card UIs
    MaterialCardView congratulationCard;
    MaterialButton focusAgainButton;
    TextView subtitleText;
    TextView dateText;
    TextView timeText;
    TextView contentText;

    //tracks if work was running earlier
    Boolean wasRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressIndicator = findViewById(R.id.circle_timer);

        stats = findViewById(R.id.statistics_text);
        stats.setOnClickListener(this);

        timerCountDown = findViewById(R.id.timer_countdown);
        timerCountDown.setText("");

        cardTimePicker = findViewById(R.id.time_picker_card);
        cardCountDownTimer = findViewById(R.id.card_timer_countdown);

        //resumeExitViews = findViewById(R.id.resume_exit);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        pauseButton = findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(this);

        resumeButton = findViewById(R.id.resume_button);
        resumeButton.setOnClickListener(this);

        exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);

        timePicker = findViewById(R.id.time_picker);
        timePicker.setMinValue(0);
        timePicker.setMaxValue(timerValues.length - 1);
        timePicker.setDisplayedValues(timerValues);

        //Congrats Card UI
        congratulationCard = findViewById(R.id.congratulation_card);
        subtitleText = findViewById(R.id.subtitle_text);
        dateText = findViewById(R.id.date);
        timeText = findViewById(R.id.time);
        contentText = findViewById(R.id.content_text);
        focusAgainButton = findViewById(R.id.focus_again_button);
        focusAgainButton.setOnClickListener(this);

        workManager = WorkManager.getInstance(this);

        //if already background work is running show progress
        workManager.getWorkInfosForUniqueWorkLiveData(Constants.WORK_NAME).observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                if (workInfos != null && workInfos.size() > 0) {
                    if (workInfos.get(0).getState() == WorkInfo.State.RUNNING) {
                        //already a worker running, show count down
                        setStartButton(); //ready ui for countdown
                        Data workProgress = workInfos.get(0).getProgress();
                        remainingTime = workProgress.getInt(ARG_PROGRESS, 0);
                        //Log.d(TAG,"Time Remaining: " + value);
                        int percentageProg = workProgress.getInt(PERCENTAGE,0);

                        //Format time in specific manner and set to text
                        timerCountDown.setText(FormatUtils.formatTime(remainingTime));
                        Log.d(TAG,"Percentage: " + percentageProg);
                        progressIndicator.setProgress(percentageProg);
                        wasRunning = true;
                    }else if (workInfos.get(0).getState() == WorkInfo.State.SUCCEEDED && wasRunning){
                        progressIndicator.setProgress(100);
                        wasRunning = false;
                        showSuccess();
                    }
                }
            }
        });

        //listen changes on number picker
        timePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                numberPickerValueIndex = i1;
            }
        });
    }

    /**
     * Show Success after completion of work
     */
    private void showSuccess() {
        openCongratsCard();
        int totalTime = 15; //TODO: fetch this from ROOM Database
        int databaseRowCount = 7; //TODO: fetch total row count from room db
        String content = String.format("I just put down my phone for %d minutes to focus", totalTime);
        String subtitle = String.format("Your %d th time productivity launcher", databaseRowCount);
        dateText.setText(FormatUtils.getDateMonth());
        timeText.setText(FormatUtils.getTime());
        contentText.setText(content);
        subtitleText.setText(subtitle);
    }

    //Handle Click Events
    @Override
    public void onClick(View view) {
        //When button text is Start-> change to Pause
        if (view.getId() == R.id.start_button) {
            //handle UI visibility and invisibility
            setStartButton();
            //ready work request
            readyWorker();
        } else if (view.getId() == R.id.pause_button) {
            //resumeExitViews.setVisibility(View.VISIBLE);
            setPauseButton();
        } else if (view.getId() == R.id.resume_button) {
            //hide resume & exit button
            //resumeExitViews.setVisibility(View.GONE);
            setResumeButton();
        } else if (view.getId() == R.id.exit_button) {
            setExitButton();
        } else if (view.getId() == R.id.statistics_text) {
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.focus_again_button){
            openTimePickerCard();
        }
    }

    private void workSuccess(){
        workManager.getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED){
                    showSuccess();
                    return;
                }
            }
        });
    }
    /**
     * Open Time Picker Card From Congratulation Card
     */
    private void openTimePickerCard() {
        congratulationCard.setVisibility(View.GONE);
        cardCountDownTimer.setVisibility(View.GONE);

        cardTimePicker.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.VISIBLE);
    }

    /**
     * Open Congratulation card after success
     */
    private void openCongratsCard(){
        congratulationCard.setVisibility(View.VISIBLE);

        cardCountDownTimer.setVisibility(View.GONE);
        cardTimePicker.setVisibility(View.GONE);
    }
    private void setExitButton() {
        cardTimePicker.setVisibility(View.VISIBLE);
        //show count down
        cardCountDownTimer.setVisibility(View.GONE);

        //hide resume & exit button
        resumeButton.setVisibility(View.GONE);
        exitButton.setVisibility(View.GONE);

        startButton.setVisibility(View.VISIBLE);

        PrefUtils.clearSharedPrefs(getApplicationContext());
    }

    private void setResumeButton() {
        resumeButton.setVisibility(View.GONE);
        exitButton.setVisibility(View.GONE);

        pauseButton.setVisibility(View.VISIBLE);
        pauseButton.setOnClickListener(this);

        resumeWorker();
    }

    //handle clicks on pause button
    private void setPauseButton() {
        resumeButton.setVisibility(View.VISIBLE);
        resumeButton.setOnClickListener(this);

        exitButton.setVisibility(View.VISIBLE);
        exitButton.setOnClickListener(this);

        pauseButton.setVisibility(View.GONE);
        workManager.cancelAllWork();

        //save remaining time
        PrefUtils.saveRemainingTime(getApplicationContext(), remainingTime);
        //stopService(new Intent(this, CountDownWorker.class));

    }

    private void openResumeCard() {
        cardTimePicker.setVisibility(View.GONE);
        //show count down
        cardCountDownTimer.setVisibility(View.VISIBLE);

        resumeButton.setVisibility(View.VISIBLE);
        resumeButton.setOnClickListener(this);

        exitButton.setVisibility(View.VISIBLE);
        exitButton.setOnClickListener(this);

    }

    private void setStartButton() {
        cardTimePicker.setVisibility(View.GONE);
        //show count down
        cardCountDownTimer.setVisibility(View.VISIBLE);

        //hide resume & exit button
        resumeButton.setVisibility(View.GONE);
        exitButton.setVisibility(View.GONE);

        //show pause button
        pauseButton.setVisibility(View.VISIBLE);
    }


    private void resumeWorker() {
        // Create the Data object:
        int timeInSecond = PrefUtils.fetchRemainingTime(getApplicationContext());
        Log.d(TAG,"Time Remained in second: " + timeInSecond);
        Data myData = new Data.Builder()
                // We need to pass three integers: X, Y, and Z
                .putInt(Constants.KEY_COUNTDOWN_TIME,timeInSecond)
                .build();

        workRequest = new OneTimeWorkRequest.Builder(CountDownWorker.class).setInputData(myData).build();

        //only add work if not running previously
        workManager.enqueueUniqueWork(Constants.WORK_NAME, ExistingWorkPolicy.KEEP, workRequest);
    }

    private void readyWorker() {
        // Create the Data object:
        int minute = Integer.parseInt(timerValues[numberPickerValueIndex]);
        Log.d(TAG,"Picker Value in minute: " + minute);
        Data myData = new Data.Builder()
                // We need to pass three integers: X, Y, and Z
                .putInt(Constants.KEY_COUNTDOWN_TIME,minute*60)
                .build();

        workRequest = new OneTimeWorkRequest.Builder(CountDownWorker.class).setInputData(myData).build();
//        workManager.getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
//            @Override
//            public void onChanged(WorkInfo workInfo) {
//                if (workInfo != null) {
//                    Data workProgress = workInfo.getProgress();
//                    int value = workProgress.getInt(ARG_PROGRESS, 0);
//                    //Log.d(TAG,"Time Remaining: " + value);
//                    int min = (int) (value/60.0);
//                    int sec = value%60;
//                    int percentageProg = workProgress.getInt(PERCENTAGE,0);
//
//                    //Format time in specific manner and set to text
//                    timerCountDown.setText(FormatUtils.formatTime(min, sec));
//                    //Log.d(TAG,"Percentage: " + percentageProg);
//                    progressIndicator.setProgress(percentageProg);
//                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
//                        Toast.makeText(getApplicationContext(), "Work Success", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        });

        //only add work if not running previously
        workManager.enqueueUniqueWork(Constants.WORK_NAME, ExistingWorkPolicy.KEEP, workRequest);
    }
}