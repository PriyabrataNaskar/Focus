package com.priyo.focus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

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

    WorkManager workManager;
    OneTimeWorkRequest workRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressIndicator = findViewById(R.id.circle_timer);

        stats = findViewById(R.id.statistics_text);
        stats.setOnClickListener(this);

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
                        int value = workProgress.getInt(ARG_PROGRESS, 0);
                        progressIndicator.setProgress(value);
                        if (workInfos.get(0).getState() == WorkInfo.State.SUCCEEDED) {
                            Toast.makeText(getApplicationContext(), "Work Success", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    //Handle Click Events on start button
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
        }
    }

    private void setExitButton() {
        cardTimePicker.setVisibility(View.VISIBLE);
        //show count down
        cardCountDownTimer.setVisibility(View.GONE);

        //hide resume & exit button
        resumeButton.setVisibility(View.GONE);
        exitButton.setVisibility(View.GONE);

        startButton.setVisibility(View.VISIBLE);
    }

    private void setResumeButton() {
        resumeButton.setVisibility(View.GONE);
        exitButton.setVisibility(View.GONE);

        pauseButton.setVisibility(View.VISIBLE);
        pauseButton.setOnClickListener(this);
    }

    private void setPauseButton() {
        resumeButton.setVisibility(View.VISIBLE);
        resumeButton.setOnClickListener(this);

        exitButton.setVisibility(View.VISIBLE);
        exitButton.setOnClickListener(this);

        pauseButton.setVisibility(View.GONE);
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

    private void readyWorker() {
        workRequest = OneTimeWorkRequest.from(CountDownWorker.class);
        workManager.getWorkInfoByIdLiveData(workRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null) {
                    Data workProgress = workInfo.getProgress();
                    int value = workProgress.getInt(ARG_PROGRESS, 0);
                    progressIndicator.setProgress(value);
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        Toast.makeText(getApplicationContext(), "Work Success", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        workManager.enqueueUniqueWork(Constants.WORK_NAME, ExistingWorkPolicy.KEEP, workRequest);
    }
}