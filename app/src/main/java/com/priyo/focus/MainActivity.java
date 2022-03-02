package com.priyo.focus;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    NumberPicker timePicker;
    MaterialButton startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        timePicker = findViewById(R.id.time_picker);
        timePicker.setMinValue(0);
        timePicker.setMaxValue(23);
        timePicker.setDisplayedValues(new String[]{"5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "70", "80", "90", "100", "110", "120", "130", "140", "150", "160", "170", "180"});

    }

    //Handle Click Events on start button
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_button){

            //When button text is Start-> change to stop
            if (startButton.getText().toString().contentEquals(getText(R.string.start))) {
                startButton.setText(R.string.pause);
            }else if (startButton.getText().toString().contentEquals(getText(R.string.pause))){ //handle click on stop
                startButton.setText(R.string.start);
            }
        }
    }
}