package com.users.qwikhomeservices.activities.home.about;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.utils.DisplayViewUI;


public class FinishAccountSetUpActivity extends AppCompatActivity {

    private static int INTERVAL = 3000;
    private long mBackPressed;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_account_set_up);


    }

    @Override
    public void onBackPressed() {

        if (mBackPressed + INTERVAL > System.currentTimeMillis())
            super.onBackPressed();
        else DisplayViewUI.displayToast(this, "Please complete your profile");
        mBackPressed = System.currentTimeMillis();
        // todo fix back pressed

    }
}
