package com.example.alexandra.expendablechecklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

public class NewRouteActivity extends AppCompatActivity {

    NumberPicker thouPicker = null;
    NumberPicker tminPicker = null;
    private Button act_choose;
    public String radius;

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

        act_choose = (Button) findViewById(R.id.btnChoose);


        act_choose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((thouPicker.getValue() == 0) && (tminPicker.getValue() == 0) ){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Введите время", Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            int a = thouPicker.getValue();
                            a *= 3600;
                            int b =  tminPicker.getValue();
                            int c = ((a+b)/951)*500;
                            radius = String.valueOf(c);
                            Intent intent = new Intent(".PlacesListActivity");
                            intent.putExtra("rad", radius);
                            startActivity(intent);
                        }

                    }
                }
        );
    }
    }

