package com.users.quickhomeservices.activities.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.ItemViewClickEvents;
import com.users.quickhomeservices.databinding.ActivityLoginBinding;
import com.users.quickhomeservices.utils.MyConstants;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout mLoginEmail, mLoginPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if (savedInstanceState != null) {
            Objects.requireNonNull(activityLoginBinding.txtPasswordLayout.getEditText()).setText(savedInstanceState.getString(MyConstants.PASS));
            Objects.requireNonNull(activityLoginBinding.txtEmailLayout.getEditText()).setText(savedInstanceState.getString(MyConstants.EMAIL));
        }
        ItemViewClickEvents itemViewClickEvents = new ItemViewClickEvents(this);
        activityLoginBinding.setOnItemClick(itemViewClickEvents);

        ItemViewClickEvents itemViewClickEvent = new ItemViewClickEvents(activityLoginBinding.txtEmailLayout, activityLoginBinding.txtPasswordLayout);
        activityLoginBinding.setValidateInput(itemViewClickEvent);

    }



    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(MyConstants.PASS, Objects.requireNonNull(activityLoginBinding.txtPasswordLayout.getEditText()).getText().toString());
        outState.putString(MyConstants.EMAIL, Objects.requireNonNull(activityLoginBinding.txtEmailLayout.getEditText()).getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Objects.requireNonNull(activityLoginBinding.txtPasswordLayout.getEditText()).setText(savedInstanceState.getString(MyConstants.PASS));
        Objects.requireNonNull(activityLoginBinding.txtEmailLayout.getEditText()).setText(savedInstanceState.getString(MyConstants.EMAIL));
    }
}
