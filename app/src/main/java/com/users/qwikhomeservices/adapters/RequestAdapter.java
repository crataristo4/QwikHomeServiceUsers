package com.users.qwikhomeservices.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.ChatActivity;
import com.users.qwikhomeservices.activities.home.fragments.RequestFragment;
import com.users.qwikhomeservices.databinding.LayoutRatingBinding;
import com.users.qwikhomeservices.databinding.LayoutUserRequestSentBinding;
import com.users.qwikhomeservices.models.RequestModel;
import com.users.qwikhomeservices.utils.DisplayViewUI;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestAdapter extends FirebaseRecyclerAdapter<RequestModel, RequestAdapter.RequestViewHolder> {
    private static ConfirmPaymentButtonClick confirmPaymentButtonClick;

    public RequestAdapter(@NonNull FirebaseRecyclerOptions<RequestModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i, @NonNull RequestModel requestModel) {

        requestViewHolder.layoutUserRequestSentBinding.setRequestItems(requestModel);
        requestViewHolder.showResponse(requestModel.getResponse());
        requestViewHolder.showRating(requestModel.getRating());
        requestViewHolder.showWorkDoneStatus(requestModel.getIsWorkDone());

        //confirm work done status and rate user
        if (requestModel.getResponse().equals("Request Accepted")) {
            requestViewHolder.btnChat.setVisibility(View.VISIBLE);

            requestViewHolder.btnChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent chatIntent = new Intent(requestViewHolder
                            .layoutUserRequestSentBinding
                            .getRoot()
                            .getContext(), ChatActivity.class);
                    //pass users data
                    String adapterPosition = getRef(i).getKey();
                    chatIntent.putExtra("senderName", requestModel.getSenderName());
                    chatIntent.putExtra("senderPhoto", requestModel.getSenderPhoto());
                    chatIntent.putExtra("senderID", requestModel.getSenderId());
                    chatIntent.putExtra("senderReason", requestModel.getReason());
                    chatIntent.putExtra("adapterPosition", adapterPosition);
                    chatIntent.putExtra("servicePersonName", requestModel.getServicePersonName());
                    chatIntent.putExtra("servicePersonPhoto", requestModel.getSenderPhoto());
                    chatIntent.putExtra("receiverID", requestModel.getReceiverId());


                    requestViewHolder
                            .layoutUserRequestSentBinding
                            .getRoot()
                            .getContext()
                            .startActivity(chatIntent);
                }
            });


            requestViewHolder.btnRateServicePerson.setVisibility(View.VISIBLE);
            requestViewHolder.btnRateServicePerson.setOnClickListener(v -> DisplayViewUI.displayAlertDialog(requestViewHolder.layoutUserRequestSentBinding.getRoot().getContext(),
                    "Confirm work ",
                    "Please confirm that your job requested to " + requestModel.getServicePersonName() + " has been done",
                    "Job done",
                    "Not done", (dialog, which) -> {
                        if (which == -1) {
                            //positive button ,user selects work done

                            Map<String, Object> jobDone = new HashMap<>();
                            jobDone.put("isWorkDone", "YES");
                            String adapterPosition = getRef(i).getKey();

                            RequestFragment.requestDbRef.child(Objects.requireNonNull(adapterPosition))
                                    .updateChildren(jobDone)
                                    .addOnCompleteListener((Activity) requestViewHolder.layoutUserRequestSentBinding.getRoot().getContext(),
                                            new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        dialog.dismiss();
                                                        //display rating dialog
                                                        final Dialog ratingDialog = new Dialog(v.getContext());
                                                        View view = LayoutInflater.from(v.getContext())
                                                                .inflate(R.layout.layout_rating, null);

                                                        LayoutRatingBinding ratingBinding = DataBindingUtil.bind(view);
                                                        ratingDialog.setContentView(Objects.requireNonNull(ratingBinding).getRoot());
                                                        ratingDialog.setCancelable(false);
                                                        ratingBinding.txtRateUser.setText(MessageFormat.format("Please rate {0} to improve our services", requestModel.getServicePersonName()));

                                                        ratingBinding.btnRateNow.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (ratingBinding.ratingBar.getRating() == 0) {
                                                                    DisplayViewUI.displayToast(v.getContext(), "Please tap on rating bar to rate " + requestModel.getServicePersonName());
                                                                } else {
                                                                    float getRating = ratingBinding.ratingBar.getRating();
                                                                    //update database
                                                                    Map<String, Object> jobRating = new HashMap<>();
                                                                    jobRating.put("rating", getRating);
                                                                    RequestFragment.requestDbRef.child(Objects.requireNonNull(adapterPosition)).updateChildren(jobRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                ratingDialog.dismiss();
                                                                                DisplayViewUI.displayToast(v.getContext(), "You have rated " + requestModel.getServicePersonName());
                                                                            } else {
                                                                                DisplayViewUI.displayToast(v.getContext(), "Please try again later.Thank you");
                                                                            }

                                                                        }
                                                                    });
                                                                }


                                                            }
                                                        });

                                                        ratingBinding.btnLater.setOnClickListener(v1 -> ratingDialog.dismiss());

                                                        ratingDialog.show();

                                                    } else {
                                                        DisplayViewUI.displayToast(requestViewHolder.layoutUserRequestSentBinding.getRoot().getContext(),
                                                                Objects.requireNonNull(task.getException()).getMessage());
                                                    }
                                                }
                                            });


                        } else if (which == -2) {
                            //negative button
                            dialog.dismiss();

                        }
                    }));
        } else {
            requestViewHolder.btnChat.setVisibility(View.GONE);
            requestViewHolder.btnRateServicePerson.setVisibility(View.GONE);
            requestViewHolder.txtWorkDone.setTextColor(requestViewHolder.layoutUserRequestSentBinding.getRoot().getResources().getColor(R.color.colorRed));
            requestViewHolder.txtResponse.setTextColor(requestViewHolder.layoutUserRequestSentBinding.getRoot().getResources().getColor(R.color.colorRed));

        }


        //vie details of the request sent
        requestViewHolder.btnView.setOnClickListener(v -> {
            if (requestModel.getRating() == 0.0) {
                new AlertDialog.Builder(v.getContext())
                        .setIcon(v.getResources().getDrawable(R.drawable.applogo))
                        .setTitle("Your request to " + requestModel.getServicePersonName())
                        .setMessage(requestModel.getReason())
                        .setPositiveButton("ok", (dialog, which) -> dialog.dismiss()).create().show();

            } else if (requestModel.getRating() > 0) {

                new AlertDialog.Builder(v.getContext())
                        .setIcon(v.getResources().getDrawable(R.drawable.applogo))
                        .setTitle("Your request to " + requestModel.getServicePersonName())
                        .setMessage(requestModel.getReason() + "\n\n\n" +
                                "You rated " + requestModel.getServicePersonName() + " "
                                + requestModel.getRating() + " stars on the work done")
                        .setPositiveButton("ok", (dialog, which) -> dialog.dismiss()).create().show();
            }

        });


    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutUserRequestSentBinding layoutUserRequestSentBinding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.layout_user_request_sent, viewGroup, false);

        return new RequestViewHolder(layoutUserRequestSentBinding);
    }

    public void setOnItemClickListener(ConfirmPaymentButtonClick confirmPaymentButtonClick) {
        RequestAdapter.confirmPaymentButtonClick = confirmPaymentButtonClick;
    }

    public interface ConfirmPaymentButtonClick {
        void onPayButtonClicked(View view, int position);
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LayoutUserRequestSentBinding layoutUserRequestSentBinding;
        RatingBar ratingBar;
        private ImageButton btnView, btnChat, btnRateServicePerson;
        private TextView txtResponse, txtPaymentStatus, txtWorkDone;
        private LinearLayoutCompat linearLayoutCompat;

        public RequestViewHolder(@NonNull LayoutUserRequestSentBinding layoutUserRequestSentBinding) {
            super(layoutUserRequestSentBinding.getRoot());
            this.layoutUserRequestSentBinding = layoutUserRequestSentBinding;
            ratingBar = layoutUserRequestSentBinding.ratedResults;
            btnRateServicePerson = layoutUserRequestSentBinding.btnRateServicePerson;
            txtResponse = layoutUserRequestSentBinding.txtResponse;
            btnView = layoutUserRequestSentBinding.btnView;
            btnChat = layoutUserRequestSentBinding.btnChat;
            txtPaymentStatus = layoutUserRequestSentBinding.txtPaymentStatus;
            linearLayoutCompat = layoutUserRequestSentBinding.linearLayout;

            txtWorkDone = layoutUserRequestSentBinding.txtWorkDone;

            layoutUserRequestSentBinding.btnConfirmPayment.setOnClickListener(this);
        }

        //display the rating
        void showRating(float rating) {
            if (!String.valueOf(rating).isEmpty() && rating > 0) {
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating(rating);
                btnRateServicePerson.setEnabled(false);

            } else if (rating == 0) {
                ratingBar.setVisibility(View.INVISIBLE);
            }

        }

        //display the response details
        void showResponse(String response) {

            //customer can only chat , rate and view the route only when their request are accepted
            if (response.equals("Request Accepted")) {
                btnRateServicePerson.setVisibility(View.VISIBLE);
                btnChat.setVisibility(View.VISIBLE);
                txtResponse.setTextColor(layoutUserRequestSentBinding.getRoot().getResources().getColor(R.color.colorGreen));
                txtResponse.setText(response);


            }
            if (response.equals("Request Rejected")) {
                btnChat.setVisibility(View.GONE);
                btnRateServicePerson.setVisibility(View.GONE);
                txtWorkDone.setText(R.string.wkDeclined);
                txtWorkDone.setVisibility(View.VISIBLE);
                txtWorkDone.setTextColor(layoutUserRequestSentBinding.getRoot().getResources().getColor(R.color.colorRed));

                txtResponse.setTextColor(layoutUserRequestSentBinding.getRoot().getResources().getColor(R.color.colorRed));
                txtResponse.setText(response);

            }


        }

        void showWorkDoneStatus(String isWorkDone) {
            if (isWorkDone.equals("YES")) {

                txtWorkDone.setTextColor(layoutUserRequestSentBinding.getRoot().getResources().getColor(R.color.colorGreen));
                txtWorkDone.setText(R.string.wkDone);
                txtWorkDone.setVisibility(View.VISIBLE);
                linearLayoutCompat.setVisibility(View.VISIBLE);
                btnRateServicePerson.setEnabled(false);

            } else if (isWorkDone.equals("NO")) {
                txtWorkDone.setText(R.string.wkNtDone);
                txtWorkDone.setVisibility(View.VISIBLE);
                txtWorkDone.setTextColor(layoutUserRequestSentBinding.getRoot().getResources().getColor(R.color.colorRed));

                linearLayoutCompat.setVisibility(View.GONE);
                btnRateServicePerson.setEnabled(true);


            }

        }


        @Override
        public void onClick(View v) {

            confirmPaymentButtonClick.onPayButtonClicked(v, getAdapterPosition());
        }
    }


}
