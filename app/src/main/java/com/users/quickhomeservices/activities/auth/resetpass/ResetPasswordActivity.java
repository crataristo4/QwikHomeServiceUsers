package com.users.quickhomeservices.activities.auth.resetpass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.auth.LoginActivity;
import com.users.quickhomeservices.databinding.ActivityResetPasswordBinding;
import com.users.quickhomeservices.utils.DisplayViewUI;
import com.users.quickhomeservices.utils.MyConstants;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding activityResetPasswordBinding;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Objects.requireNonNull(activityResetPasswordBinding.txtEmailLayout.getEditText()).setText(savedInstanceState.getString(MyConstants.EMAIL));

        }
        super.onCreate(savedInstanceState);
        activityResetPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        loadingbar = DisplayViewUI.displayProgress(this, "sending reset  link...");


        activityResetPasswordBinding.btnReset.setOnClickListener(this::onClick);
    }

    private void onClick(View view) {
        TextInputLayout txtEmail = activityResetPasswordBinding.txtEmailLayout;
        String email = Objects.requireNonNull(txtEmail.getEditText()).getText().toString();

        if ((!email.trim().isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())) {

            loadingbar.show();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    loadingbar.dismiss();
                    DisplayViewUI.displayAlertDialogMsg(ResetPasswordActivity.this,
                            "Please check your email and reset your password",
                            "OK", (dialog, which) -> {
                                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                finish();

                            });

                } else {
                    loadingbar.dismiss();

                    DisplayViewUI.displayToast(ResetPasswordActivity.this, Objects.requireNonNull(task.getException()).getMessage());

                }

            });
        }

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MyConstants.EMAIL, Objects.requireNonNull(activityResetPasswordBinding.txtEmailLayout.getEditText()).getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Objects.requireNonNull(activityResetPasswordBinding.txtEmailLayout.getEditText()).setText(savedInstanceState.getString(MyConstants.EMAIL));
    }
}
