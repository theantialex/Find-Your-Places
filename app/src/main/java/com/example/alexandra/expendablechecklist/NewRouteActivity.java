package com.example.alexandra.expendablechecklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class NewRouteActivity extends AppCompatActivity {

    NumberPicker thouPicker = null;
    NumberPicker tminPicker = null;
    private Button act_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);
        addListenerOnButton();

        thouPicker = (NumberPicker)findViewById(R.id.hour_picker);
        thouPicker.setMaxValue(5);
        thouPicker.setMinValue(0);
        thouPicker.setWrapSelectorWheel(true);

        tminPicker = (NumberPicker)findViewById(R.id.minute_picker);
        tminPicker.setMaxValue(59);
        tminPicker.setMinValue(0);
        tminPicker.setWrapSelectorWheel(true);


    }
    public void addListenerOnButton() {

        act_new = (Button) findViewById(R.id.btnChoose);

        act_new.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Opens second activity
                        Intent intent = new Intent(".PlacesListActivity");
                        startActivity(intent);


                    }
                }
        );
    }
    }

