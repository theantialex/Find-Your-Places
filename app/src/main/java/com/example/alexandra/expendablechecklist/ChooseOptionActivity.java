package com.example.alexandra.expendablechecklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseOptionActivity extends AppCompatActivity {
    private Button act_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);
        addListenerOnButton();

    }

    public void addListenerOnButton() {

        act_new = (Button) findViewById(R.id.btnNewRoute);

        act_new.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Opens second activity
                        Intent intent = new Intent(".NewRouteActivity");
                        startActivity(intent);


                    }
                }
        );
    }}