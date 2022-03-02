package com.priyo.focus;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;

import static com.priyo.focus.Constants.ARG_PROGRESS;
import static com.priyo.focus.Constants.TAG;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        WorkManager workManager = WorkManager.getInstance(this);

        //if already background work is running show progress
            workManager.getWorkInfosForUniqueWorkLiveData(Constants.WORK_NAME).observe(this, new Observer<List<WorkInfo>>() {
                @Override
                public void onChanged(List<WorkInfo> workInfos) {
                    if (workInfos!=null && workInfos.size()>0){
                        if (workInfos.get(0).getState() == WorkInfo.State.RUNNING){
                            Data workProgress = workInfos.get(0).getProgress();
                            int value = workProgress.getInt(ARG_PROGRESS, 0);
                            Log.d(TAG,"Val " + value);
                            if (workInfos.get(0).getState() == WorkInfo.State.SUCCEEDED) {
                                Toast.makeText(getApplicationContext(), "Work Success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
    }
}