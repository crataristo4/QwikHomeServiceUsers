package com.users.quickhomeservices.activities.auth.signup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.auth.LoginActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.users.quickhomeservices.databinding.ActivitySignupBinding;
import com.users.quickhomeservices.utils.MyConstants;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private TextInputLayout txtFullName, txtEmail ;
    private ActivitySignupBinding activitySignupBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Objects.requireNonNull(txtEmail.getEditText()).setText(savedInstanceState.getString(MyConstants.EMAIL));
            Objects.requireNonNull(txtFullName.getEditText()).setText(savedInstanceState.getString(MyConstants.NAME));
        }
        super.onCreate(savedInstanceState);
        activitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);


        txtEmail = activitySignupBinding.txtEmailLayout;
        txtFullName = activitySignupBinding.txtfullNameLayout;

    }

    public void gotoLogin(View view) {
        startActivity(
                new Intent(SignupActivity.this, LoginActivity.class)
        );
    }

    public void gotoNext(View view) {

        validateAndProceed();


    }

    private void validateAndProceed() {
        String getFullName = Objects.requireNonNull(txtFullName.getEditText()).getText().toString();
        String getEmail = Objects.requireNonNull(txtEmail.getEditText()).getText().toString();

        if (getFullName.trim().isEmpty()) {
            txtFullName.setErrorEnabled(true);
            txtFullName.setError("Full name required");
        } else {
            txtFullName.setErrorEnabled(false);
        }

        if (getEmail.trim().isEmpty()) {
            txtEmail.setErrorEnabled(true);
            txtEmail.setError("Email required");
        } else {
            txtEmail.setErrorEnabled(false);
        }

        if ( ! android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){
            txtEmail.setErrorEnabled(true);
            txtEmail.setError("invalid email");
        } else {
            txtEmail.setErrorEnabled(false);
        }

        if (!txtFullName.getEditText().getText().toString().isEmpty()
                && !txtEmail.getEditText().getText().toString().isEmpty()
                && android.util.Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
            txtFullName.setErrorEnabled(false);
            txtEmail.setErrorEnabled(true);

            Intent gotoCompleteSignUp = new Intent(SignupActivity.this, SignupCompleteActivity.class);
            gotoCompleteSignUp.putExtra("name", getFullName);
            gotoCompleteSignUp.putExtra("email", getEmail);
            startActivity(gotoCompleteSignUp);
            finish();

        }



    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MyConstants.NAME, Objects.requireNonNull(txtFullName.getEditText()).getText().toString());
        outState.putString(MyConstants.EMAIL, Objects.requireNonNull(txtEmail.getEditText()).getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Objects.requireNonNull(txtFullName.getEditText()).setText(savedInstanceState.getString(MyConstants.PASS));
        Objects.requireNonNull(txtEmail.getEditText()).setText(savedInstanceState.getString(MyConstants.CONFIRM_PASS));
    }


    @Override
    protected void onPause() {
        super.onPause();

        try {
            SharedPreferences preferences = getSharedPreferences("namePrefs",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("fullName", txtFullName.getEditText().getText().toString());
            editor.putString("email", txtEmail.getEditText().getText().toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("namePrefs",
                    MODE_PRIVATE);
            String name = sharedPreferences.getString("fullName", "");
            String email = sharedPreferences.getString("email", "");

            txtFullName.getEditText().setText(name);
            txtEmail.getEditText().setText(email);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
