package com.users.quickhomeservices.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.users.quickhomeservices.activities.auth.LoginActivity;
import com.users.quickhomeservices.activities.auth.resetpass.ResetPasswordActivity;
import com.users.quickhomeservices.activities.auth.signup.SignupActivity;
import com.users.quickhomeservices.activities.home.MainActivity;
import com.users.quickhomeservices.utils.DisplayViewUI;

import java.util.Objects;

public class ItemViewClickEvents {
    private Context context;
    private TextInputLayout txtEmail, txtPassword;

    public ItemViewClickEvents(TextInputLayout txtEmail, TextInputLayout txtPassword) {
        this.txtEmail = txtEmail;
        this.txtPassword = txtPassword;
    }

    public ItemViewClickEvents(Context context) {
        this.context = context;
    }


    public void onWelcomeLoginButtonClicked(View view) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }


    public void signIn(View view) {
        validateInputs(view);
    }

    private void validateInputs(View view) {
        String email = Objects.requireNonNull(txtEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(txtPassword.getEditText()).getText().toString();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser;

        firebaseUser = firebaseAuth.getCurrentUser();


        if (password.trim().isEmpty()) {
            txtPassword.setErrorEnabled(true);
            txtPassword.setError("password required");
        } else {
            txtPassword.setErrorEnabled(false);
        }
        if (email.trim().isEmpty()) {
            txtEmail.setErrorEnabled(true);
            txtEmail.setError("email required");
        } else {
            txtEmail.setErrorEnabled(false);
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setErrorEnabled(true);
            txtEmail.setError("invalid email");
        } else {
            txtEmail.setErrorEnabled(false);
        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // loadingbar.setTitle("");
            final ProgressDialog loading = DisplayViewUI.displayProgress(view.getContext(), "Please wait...");
            loading.show();


            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).isEmailVerified()) {
                                assert firebaseUser != null;
                                //currentUserId = firebaseUser.getUid();

                                Intent gotoAbout = new Intent(context, MainActivity.class);
                                //gotoAbout.putExtra("userId", currentUserId);
                                // gotoAbout.putExtra("accountType", passAccountTypeValue);

                                context.startActivity(gotoAbout
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                                loading.dismiss();
                            } else {
                                loading.dismiss();
                                DisplayViewUI.displayAlertDialogMsg(view.getContext(), "Hello" + " " + email + " "
                                        + " " + "\n" + "please check your email  " + "\n" +
                                        "to verify and continue", "ok", (dialog, which) -> dialog.dismiss());

                            }


                        } else {
                            loading.dismiss();
                            String message = Objects.requireNonNull(task.getException()).getMessage();
                            DisplayViewUI.displayToast(view.getContext(), message);
                        }
                    });

        }
    }


    public void toSignUp(View view) {
        context.startActivity(new Intent(context, SignupActivity.class));

    }

    public void toResetPassword(View view) {
        context.startActivity(new Intent(context, ResetPasswordActivity.class));

    }


}
