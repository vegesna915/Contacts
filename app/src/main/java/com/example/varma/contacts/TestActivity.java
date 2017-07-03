package com.example.varma.contacts;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.varma.contacts.Extra.Utils;


public class TestActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        final Button button = (Button) findViewById(R.id.button_test);
        button.setText(String.valueOf(Utils.num));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.num++;
                button.setText(String.valueOf(Utils.num));
            }
        });


    }
}
