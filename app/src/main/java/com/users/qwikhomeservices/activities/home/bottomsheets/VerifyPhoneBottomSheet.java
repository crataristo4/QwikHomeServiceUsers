package com.users.qwikhomeservices.activities.home.bottomsheets;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.hbb20.CountryCodePicker;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.databinding.LayoutAddPhoneBinding;
import com.users.qwikhomeservices.utils.DisplayViewUI;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneBottomSheet extends BottomSheetDialogFragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private LayoutAddPhoneBinding layoutAddPhoneBinding;
    private TextInputLayout txtPhoneNumber, txtVerifyCode;
    private int RESOLVE_HINT = 2;
    private String getPhone, getmVerificationCode;

    private String mVerificationCode;
    private DatabaseReference userDbRef;
    private GoogleApiClient mGoogleApiClient;
    private CountryCodePicker countryCodePicker;
    private ProgressBar loading;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationCode = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //this method automatically handles the code sent
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                Objects.requireNonNull(txtVerifyCode.getEditText()).setText(code);

                verifyCode(code);
            }


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            DisplayViewUI.displayToast(getContext(), e.getMessage());

        }
    };

    private void cancelBottomSheetDialog(View view) {

        if (view.getId() == R.id.txtCancel || view.getId() == R.id.txtCancelCode)
            dismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient.stopAutoManage(Objects.requireNonNull(getActivity()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtPhoneNumber = layoutAddPhoneBinding.textInputLayoutPhone;
        txtVerifyCode = layoutAddPhoneBinding.textInputLayoutConfirmCode;
        countryCodePicker = layoutAddPhoneBinding.ccp;
        loading = layoutAddPhoneBinding.progressBarVerify;

        countryCodePicker.registerCarrierNumberEditText(txtPhoneNumber.getEditText());
        countryCodePicker.setNumberAutoFormattingEnabled(true);

        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getActivity()))
                .addConnectionCallbacks(this)
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        //todo complete process
        layoutAddPhoneBinding.btnVerify.setOnClickListener(v -> {
            getmVerificationCode = Objects.requireNonNull(Objects.requireNonNull(txtVerifyCode.getEditText()).getText().toString());

            if (getmVerificationCode.trim().isEmpty() || getmVerificationCode.length() < 6) {
                txtVerifyCode.setErrorEnabled(true);
                txtVerifyCode.setError("Verification code invalid");
            }
            if (!getmVerificationCode.trim().isEmpty() && getmVerificationCode.length() == 6) {

                verifyCode(getmVerificationCode);
            }

        });

        layoutAddPhoneBinding.btnRegisterPhoneNumber.setOnClickListener(v -> {

            String getPhoneNumber = Objects.requireNonNull(txtPhoneNumber.getEditText()).getText().toString();
            if (!getPhoneNumber.trim().isEmpty()) {
                txtPhoneNumber.setErrorEnabled(false);

                getPhone = countryCodePicker.getFormattedFullNumber();
                Log.i("Number : ", getPhone);
                sendVerificationCode(getPhone);

                showHideLayout();


            } else if (getPhoneNumber.trim().isEmpty()) {
                txtPhoneNumber.setErrorEnabled(true);
                txtPhoneNumber.setError("phone number invalid");
            }

        });

        getHintPhoneNumber();
        layoutAddPhoneBinding.txtCancel.setOnClickListener(this::cancelBottomSheetDialog);
        layoutAddPhoneBinding.txtCancelCode.setOnClickListener(this::cancelBottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutAddPhoneBinding = DataBindingUtil.inflate(inflater, R.layout.layout_add_phone, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        return layoutAddPhoneBinding.getRoot();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void sendVerificationCode(String number) {
//        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Objects.requireNonNull(getActivity()),               // Activity (for callback binding)
                mCallbacks);
    }

    private void verifyCode(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationCode, code);

        signInWithCredentials(phoneAuthCredential);

    }

    private void signInWithCredentials(PhoneAuthCredential phoneAuthCredential) {

        loading.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = Objects.requireNonNull(user).getUid();
        String number = user.getPhoneNumber();

        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loading.setVisibility(View.GONE);

                    DisplayViewUI.displayToast(getActivity(), "Success");
                    dismiss();


                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    loading.setVisibility(View.GONE);
                    dismiss();
                    DisplayViewUI.displayToast(getActivity(), task.getException().getMessage());
                }

            }
        });

    }


    private void getHintPhoneNumber() {
        HintRequest hintRequest =
                new HintRequest.Builder()
                        .setPhoneNumberIdentifierSupported(true)
                        .build();
        PendingIntent mIntent = Auth.CredentialsApi.getHintPickerIntent(mGoogleApiClient, hintRequest);
        try {
            startIntentSenderForResult(mIntent.getIntentSender(), RESOLVE_HINT, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void showHideLayout() {
        loading.setVisibility(View.VISIBLE);
        new Handler().postDelayed(() -> {
            loading.setVisibility(View.GONE);
            layoutAddPhoneBinding.constrainLayoutConfrimNumber.setVisibility(View.GONE);
            layoutAddPhoneBinding.constrainLayoutVerifyPhone.setVisibility(View.VISIBLE);
        }, 5000);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Result if we want hint number
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    String getProvider = Objects.requireNonNull(credential).getId();

                    if (isNetworkConnected()) {
                        sendVerificationCode(getProvider);
                        showHideLayout();
                    } else {
                        DisplayViewUI.displayToast(getActivity(), "No internet");
                    }


                }

            }
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        assert networkInfo != null;
        return networkInfo.isConnectedOrConnecting();

    }
}
