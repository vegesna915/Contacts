package com.example.varma.contacts;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.varma.contacts.Database.RequestsDb;
import com.example.varma.contacts.Objects.Request;

public class TestActivty extends AppCompatActivity {

    TextView textView;
    Button button;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);


        textView = (TextView) findViewById(R.id.textView_Test);
        button = (Button) findViewById(R.id.button_test);
        editText = (EditText) findViewById(R.id.editText_test);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String a = editText.getText().toString();
                RequestsDb requestsDb = new RequestsDb(getApplicationContext());
                Request request = requestsDb.getRequest(a);

                if (request == null) {
                    textView.setText(" Request not registered ");
                } else {
                    String name = request.get_Name();
                    String _id = request.getREQUEST_ID();

                    textView.setText(_id + " " + name + " ");
                }




            }
        });


    }

}
