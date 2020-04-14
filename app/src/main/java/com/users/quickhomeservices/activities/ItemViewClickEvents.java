package com.users.quickhomeservices.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.users.quickhomeservices.activities.auth.LoginActivity;
import com.users.quickhomeservices.activities.auth.signup.SignUpServicePersonelActivity;

public class ItemViewClickEvents {
    private Context context;

    public ItemViewClickEvents(Context context) {
        this.context = context;
    }

    //simple click
    public void onWelcomeLoginButtonClicked(View view) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    //simple click
    public void onWelcomeLSignUpButtonClicked(View view) {
        context.startActivity(new Intent(context, SignUpServicePersonelActivity.class));
    }


}
