package com.priyo.focus.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.material.button.MaterialButton;
import com.priyo.focus.Constants;
import com.priyo.focus.R;
import com.priyo.focus.Utils.FormatUtils;
import com.priyo.focus.adpater.StatAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.priyo.focus.Constants.ARG_PROGRESS;
import static com.priyo.focus.Constants.TAG;

public class StatisticsActivity extends AppCompatActivity {
    TextView timeTextView;
    MaterialButton exitButton;

    // Member variables for recycler view
    private RecyclerView mRecyclerView;
    private List<Integer> mMessageData;
    private StatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        timeTextView = findViewById(R.id.time_text);
        exitButton = findViewById(R.id.exit_button);

        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.recycler_view);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ArrayList that will contain the data.
        mMessageData = new ArrayList<>();
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.exit_button){
                    finish();
                }
            }
        });
        //timeTextView.setText(null);
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
                            timeTextView.setText(FormatUtils.formatTime(value));
                            if (workInfos.get(0).getState() == WorkInfo.State.SUCCEEDED) {
                                Toast.makeText(getApplicationContext(), "Work Success", Toast.LENGTH_SHORT).show();
                            }
                        }else if (workInfos.get(0).getState() != WorkInfo.State.RUNNING){
                            timeTextView.setText("");
                        }
                    }
                }
            });
            setRecyclerView();
    }

    private void setRecyclerView(){
        List<Integer> timeList = new ArrayList<Integer>();
        timeList.clear();

        for (int i=0;i<15;i++){
            timeList.add(new Random().nextInt(20)*5);
        }
        if (timeList.size()>0){
            mMessageData = timeList;
            mAdapter = new StatAdapter(mMessageData,this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}