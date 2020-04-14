package com.users.quickhomeservices;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.customeractivity.CustomerRegister;
import com.users.quickhomeservices.activities.handymanactivity.HandymanRegister;

public class Chooseurregistertype extends AppCompatActivity {
    Button btncustomer,btnhandyman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseurregistertype);

        btncustomer=findViewById(R.id.btncustomerlogin1);
        btnhandyman=findViewById(R.id.btnhandyman1);

        btncustomer.setOnClickListener(view -> startActivity(new Intent(Chooseurregistertype.this, CustomerRegister.class)));

        btnhandyman.setOnClickListener(view -> startActivity(new Intent(Chooseurregistertype.this, HandymanRegister.class)));

    }
}
