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
    private ItemViewClickEvents itemViewClickEvents;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private ActivityLoginBinding activityLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Objects.requireNonNull(mLoginEmail.getEditText()).setText(savedInstanceState.getString(MyConstants.EMAIL));
            Objects.requireNonNull(mLoginPassword.getEditText()).setText(savedInstanceState.getString(MyConstants.PASS));
        }
        super.onCreate(savedInstanceState);

        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        itemViewClickEvents = new ItemViewClickEvents(this);
        activityLoginBinding.setOnItemClick(itemViewClickEvents);

        itemViewClickEvents = new ItemViewClickEvents(activityLoginBinding.txtEmailLayout, activityLoginBinding.txtPasswordLayout);
        activityLoginBinding.setValidateInput(itemViewClickEvents);

       /* mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();


        mLoginEmail = activityLoginBinding.txtEmailLayout;
        mLoginPassword = activityLoginBinding.txtPasswordLayout;*/



    }



/*
    public void gotoMainPage(View view) {
        validateLogin(view);
    }
*/

/*
    private void validateLogin(View view) {
        String email = Objects.requireNonNull(mLoginEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(mLoginPassword.getEditText()).getText().toString();

        if (password.trim().isEmpty()) {
            mLoginPassword.setErrorEnabled(true);
            mLoginPassword.setError("password required");
        } else {
            mLoginPassword.setErrorEnabled(false);
        }
        if (email.trim().isEmpty()) {
            mLoginEmail.setErrorEnabled(true);
            mLoginEmail.setError("email required");
        } else {
            mLoginEmail.setErrorEnabled(false);
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
           // loadingbar.setTitle("");
            final ProgressDialog loading =   DisplayViewUI.displayProgress(view.getContext(),"Please wait...");
            loading.show();


            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                assert firebaseUser != null;
                                //currentUserId = firebaseUser.getUid();

                                Intent gotoAbout = new Intent(LoginActivity.this, MainActivity.class);
                                //gotoAbout.putExtra("userId", currentUserId);
                                // gotoAbout.putExtra("accountType", passAccountTypeValue);

                                startActivity(gotoAbout
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                                loading.dismiss();
                            } else {
                                loading.dismiss();
                                DisplayViewUI.displayAlertDialogMsg(view.getContext(), "Hello" + " " + email + " "
                                        + " " + "\n" + "please check your email  " + "\n" +
                                        "to verify and continue", "ok", (dialog, which) -> dialog.dismiss());

                            }


                        } else {
                            loading.dismiss();
                            String message = task.getException().getMessage();
                            DisplayViewUI.displayToast(view.getContext(),message);
                        }
                    });

        }
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MyConstants.PASS, Objects.requireNonNull(mLoginPassword.getEditText()).getText().toString());
        outState.putString(MyConstants.EMAIL, Objects.requireNonNull(mLoginEmail.getEditText()).getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Objects.requireNonNull(mLoginPassword.getEditText()).setText(savedInstanceState.getString(MyConstants.PASS));
        Objects.requireNonNull(mLoginEmail.getEditText()).setText(savedInstanceState.getString(MyConstants.EMAIL));
    }
}
