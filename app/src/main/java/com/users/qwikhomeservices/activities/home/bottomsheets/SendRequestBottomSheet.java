package com.users.qwikhomeservices.activities.home.bottomsheets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.databinding.LayoutSendRequestBinding;
import com.users.qwikhomeservices.models.RequestModel;
import com.users.qwikhomeservices.utils.DisplayViewUI;
import com.users.qwikhomeservices.utils.MyConstants;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class SendRequestBottomSheet extends BottomSheetDialogFragment {

    private LayoutSendRequestBinding layoutSendRequestBinding;
    private String servicePersonName, userName, userPhotoUrl, uid, servicePersonId;
    private String notApproved = "Not yet Approved";
    private String itemName, itemPrice, itemImage;
    private String dateRequested, getReason;
    private TextInputLayout txtReasonInputText;
    private InputMethodManager inputMethodManager;
    private ScrollView scrollView;
    private ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutSendRequestBinding = DataBindingUtil.inflate(inflater, R.layout.layout_send_request, container, false);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return layoutSendRequestBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scrollView = layoutSendRequestBinding.scrolView;
        //scroll the layout up
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(() -> scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN)));

        progressBar = layoutSendRequestBinding.pbLoading;
        txtReasonInputText = layoutSendRequestBinding.reasonInputLayout;
        inputMethodManager = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);


        //get data from bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            //pass value to items
            itemName = bundle.getString(MyConstants.STYLE);
            itemPrice = bundle.getString(MyConstants.PRICE);
            itemImage = bundle.getString(MyConstants.IMAGE_URL);
            servicePersonName = bundle.getString(MyConstants.SERVICE_PERSON_NAME);
            servicePersonId = bundle.getString(MyConstants.SERVICE_PERSON_ID);
            userName = bundle.getString(MyConstants.FULL_NAME);
            uid = bundle.getString(MyConstants.UID);
            userPhotoUrl = bundle.getString(MyConstants.USER_IMAGE_URL);


            layoutSendRequestBinding.toolBarRequestTo.setTitle(MessageFormat.format("Send request to {0}", servicePersonName));
            layoutSendRequestBinding.toolBarRequestTo.setTitleTextColor(Color.WHITE);
            layoutSendRequestBinding.txtStyleName.setText(itemName);
            layoutSendRequestBinding.txtPrice.setText(MessageFormat.format("GH {0}", itemPrice));
            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(itemImage)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(layoutSendRequestBinding.imgItemPhoto);


        }

        //SEND REQUEST from send text
        layoutSendRequestBinding.btnSendRequest.setOnClickListener(v -> sendItemRequest());

        layoutSendRequestBinding.txtCancel.setOnClickListener(this::onDialogCancelled);

    }

    //send request method
    private void sendItemRequest() {

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:MM a");
        dateRequested = dateFormat.format(Calendar.getInstance().getTime());
        getReason = Objects.requireNonNull(layoutSendRequestBinding.reasonInputLayout.getEditText()).getText().toString();

        if (getReason.trim().isEmpty()) {
            txtReasonInputText.setErrorEnabled(true);
            txtReasonInputText.setError("Please state a reason for request");
        } else {
            txtReasonInputText.setErrorEnabled(false);
        }

        if (getReason.length() < 20) {
            txtReasonInputText.setErrorEnabled(true);
            txtReasonInputText.setError("Reason for request too short");
        } else {
            txtReasonInputText.setErrorEnabled(false);
        }

        if (!getReason.trim().isEmpty() && getReason.length() > 20) {

            DisplayViewUI.displayAlertDialog(getActivity(), "Send Request", "Are you sure you want to send request",
                    "Yes", "No", (dialog, which) -> {
                        if (which == -1) {
                            //user wants to send request
                            dialog.dismiss();
                            assert inputMethodManager != null;
                            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(getView()).getWindowToken(), 0);
                            validateRequest();
                        } else if (which == -2) {
                            //user does not want to send request
                            dialog.dismiss();
                        }
                    });


        }

    }

    private void validateRequest() {

        //send request to service person
        DatabaseReference requestDbRef = FirebaseDatabase.getInstance().getReference("Requests");

        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {

            progressBar.setVisibility(View.VISIBLE);
            RequestModel requestSent = new RequestModel(
                    0, uid, servicePersonId,
                    getReason, itemPrice, itemName,
                    notApproved, itemImage, userPhotoUrl,
                    userName, servicePersonName, dateRequested, "NO");

            String requestId = requestDbRef.push().getKey();
            assert requestId != null;

            requestDbRef.child(requestId).setValue(requestSent).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    dismiss();
                    DisplayViewUI.displayToast(getActivity(), "Request has been sent");


                } else {
                    progressBar.setVisibility(View.GONE);
                    String error = Objects.requireNonNull(task.getException()).getMessage();
                    DisplayViewUI.displayToast(getActivity(), error);
                }

            }).addOnFailureListener(e -> {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                DisplayViewUI.displayToast(getActivity(), e.getMessage());

            });
        });
    }

    //cancel dialog
    private void onDialogCancelled(View view) {
        dismiss();
    }


}
