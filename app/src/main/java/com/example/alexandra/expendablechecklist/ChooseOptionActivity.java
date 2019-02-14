package com.example.alexandra.expendablechecklist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseOptionActivity extends AppCompatActivity {
    private Button act_new;
    private Button act_old;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);
        addListenerOnButton();

    }

    public void addListenerOnButton() {


        act_new = (Button) findViewById(R.id.btnNewRoute);
        act_old = findViewById(R.id.btnFavRoute);

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
        act_old.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Эта функция находится в разработке", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
        );
    }}