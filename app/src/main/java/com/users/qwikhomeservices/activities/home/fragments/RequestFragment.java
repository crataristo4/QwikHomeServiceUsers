package com.users.qwikhomeservices.activities.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.MainActivity;
import com.users.qwikhomeservices.adapters.RequestAdapter;
import com.users.qwikhomeservices.databinding.FragmentRequestBinding;
import com.users.qwikhomeservices.models.RequestModel;
import com.users.qwikhomeservices.utils.DisplayViewUI;
import com.users.qwikhomeservices.utils.MyConstants;


public class RequestFragment extends Fragment {
    private FragmentRequestBinding fragmentRequestBinding;
    private RequestAdapter requestAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static DatabaseReference requestDbRef;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Parcelable mState;
    private Bundle mBundleState;


    //Endpoint to verify transaction
    private final String VERIFY_ENDPOINT = "https://api.ravepay.co/flwv3-pug/getpaidx/api/v2/verify";

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentRequestBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_request, container, false);
        return fragmentRequestBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        intViews();

    }

    private void intViews() {
        recyclerView = fragmentRequestBinding.rvRequests;
        swipeRefreshLayout = fragmentRequestBinding.swipeRefresh;

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        requestDbRef = FirebaseDatabase.getInstance().getReference("Requests");
        requestDbRef.keepSynced(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.amber, R.color.fb,
                R.color.colorAccent, R.color.colorAsh, R.color.colorOrange);

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {

            // TODO: 12-Apr-20 refresh data on refreshing
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);

        }, 3000));


        requireActivity().runOnUiThread(this::loadData);

        requestAdapter.setOnItemClickListener((view, position) -> {

            double amountToPay = Double.parseDouble(requestAdapter.getItem(position).getPrice());
            String customerFirstName = requestAdapter.getItem(position).getFirstName();
            String customerLastName = requestAdapter.getItem(position).getLastName();
            String customerNumber = requestAdapter.getItem(position).getMobileNumber();
            String number = "0244123567";
            //todo make request database include first and last name

            proceedToPayment(amountToPay, customerFirstName, customerLastName, number);

        });


    }

    private void proceedToPayment(double amountToPay, String customerFirstName, String customerLastName, String number) {

        new RavePayManager(this)
                .setAmount(amountToPay)
                .setCountry("GH")
                .setCurrency("GHS")
                .setEmail("crataristo4@gmail.com")
                .setfName(customerFirstName)
                .setlName(customerLastName)
                .setPhoneNumber(number)
                .acceptAccountPayments(true)
                .setPublicKey("FLWPUBK-ece94eea6997bfd0ee3054f5f2519f77-X")
                .setEncryptionKey("d56907b8547b4423bbc875a0")
                .setTxRef("logTrail")
                .acceptGHMobileMoneyPayments(true)
                .onStagingEnv(false)
                .isPreAuth(true)
                .acceptUssdPayments(true)
                .shouldDisplayFee(true)
                .initialize();

    }

    private void loadData() {

        String uid = MainActivity.uid;

        Query query = requestDbRef.orderByChild("senderId").equalTo(uid);

        FirebaseRecyclerOptions<RequestModel> options = new FirebaseRecyclerOptions.Builder<RequestModel>().
                setQuery(query, RequestModel.class).build();
        requestAdapter = new RequestAdapter(options);

            recyclerView.setAdapter(requestAdapter);
            requestAdapter.notifyDataSetChanged();

    }

    @Override
    public void onStart() {
        super.onStart();
        requestAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBundleState = new Bundle();
        mState = layoutManager.onSaveInstanceState();
        mBundleState.putParcelable(MyConstants.KEY, mState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBundleState != null) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    mState = mBundleState.getParcelable(MyConstants.KEY);
                    layoutManager.onRestoreInstanceState(mState);
                }
            }, 50);
        }

        recyclerView.setLayoutManager(layoutManager);
        requestAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        requestAdapter.stopListening();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {

                //get data from json object and change database
                DisplayViewUI.displayToast(requireActivity(), message);


            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                DisplayViewUI.displayToast(requireActivity(), "Pleas try again");
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {

                DisplayViewUI.displayToast(requireActivity(), "Payment cancelled");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
