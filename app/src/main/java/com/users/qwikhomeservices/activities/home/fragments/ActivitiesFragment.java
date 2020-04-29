package com.users.qwikhomeservices.activities.home.fragments;


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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.adapters.ActivityItemAdapter;
import com.users.qwikhomeservices.databinding.FragmentActivitiesBinding;
import com.users.qwikhomeservices.models.StylesItemModel;
import com.users.qwikhomeservices.utils.MyConstants;


public class ActivitiesFragment extends Fragment {
    private Bundle mBundleState;
    private static final String TAG = "ActivityFragment";
    private FragmentActivitiesBinding fragmentActivitiesBinding;
    private RecyclerView rvBarbers, rvHairStylist, rvInteriorDeco, rvItems;
    private DatabaseReference dbRef;
    private ActivityItemAdapter activityItemAdapter;
    private LinearLayoutManager layoutManager;
    private Parcelable mState;

    public ActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentActivitiesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_activities, container, false);

        return fragmentActivitiesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().runOnUiThread(this::loadActivityData);


    }

    private void loadActivityData() {
        rvItems = fragmentActivitiesBinding.rvItems;
        rvItems.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvItems.setLayoutManager(layoutManager);

        dbRef = FirebaseDatabase.getInstance().getReference()
                .child("Activity");
        dbRef.keepSynced(true);

        //querying the database base of the time posted
        Query query = dbRef.orderByValue();
        FirebaseRecyclerOptions<StylesItemModel> options =
                new FirebaseRecyclerOptions.Builder<StylesItemModel>().setQuery(query,
                        StylesItemModel.class)
                        .build();

        activityItemAdapter = new ActivityItemAdapter(options);
        rvItems.setAdapter(activityItemAdapter);
        activityItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        activityItemAdapter.startListening();

    }


    @Override
    public void onStop() {
        super.onStop();
        activityItemAdapter.stopListening();
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

        rvItems.setLayoutManager(layoutManager);
    }


}