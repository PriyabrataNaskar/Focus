package com.priyo.focus;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    NumberPicker timePicker;
    MaterialButton startButton;
    MaterialButton pauseButton;

    MaterialButton resumeButton;
    MaterialButton exitButton;

    //cards for set timer and countdown
    MaterialCardView cardTimePicker;
    MaterialCardView cardCountDownTimer;

    //LinearLayout resumeExitViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        timePicker.setMaxValue(23);
        timePicker.setDisplayedValues(new String[]{"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "70", "80", "90", "100", "110", "120", "130", "140", "150", "160", "170", "180"});

    }

    //Handle Click Events on start button
    @Override
    public void onClick(View view) {
        //When button text is Start-> change to Pause
        if (view.getId() == R.id.start_button){
            cardTimePicker.setVisibility(View.GONE);
            //show count down
            cardCountDownTimer.setVisibility(View.VISIBLE);

            //hide resume & exit button
            //resumeExitViews.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            exitButton.setVisibility(View.GONE);

            //show pause button
            pauseButton.setVisibility(View.VISIBLE);
        }else if(view.getId() == R.id.pause_button){
            //resumeExitViews.setVisibility(View.VISIBLE);

            resumeButton.setVisibility(View.VISIBLE);
            resumeButton.setOnClickListener(this);

            exitButton.setVisibility(View.VISIBLE);
            exitButton.setOnClickListener(this);

            pauseButton.setVisibility(View.GONE);
        }else if (view.getId()== R.id.resume_button){
            //hide resume & exit button
            //resumeExitViews.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            exitButton.setVisibility(View.GONE);

            pauseButton.setVisibility(View.VISIBLE);
            pauseButton.setOnClickListener(this);
        }else if (view.getId() == R.id.exit_button){

            //manage cards
            cardTimePicker.setVisibility(View.VISIBLE);
            cardCountDownTimer.setVisibility(View.GONE);

            //show start button
            startButton.setVisibility(View.VISIBLE);
            startButton.setOnClickListener(this);
        }
    }
}