package com.users.qwikhomeservices.activities.home.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.adapters.MultiViewTypeAdapter;
import com.users.qwikhomeservices.databinding.FragmentActivitiesBinding;
import com.users.qwikhomeservices.models.ActivityItemModel;
import com.users.qwikhomeservices.utils.MyConstants;

import java.util.ArrayList;


public class ActivitiesFragment extends Fragment {
    private static final String KEY = "key";
    private static final String TAG = "ActivityFragment";
    private static final int INITIAL_LOAD = 15;
    private boolean userScrolled = false;
    private int currentPage = 1;
    private FragmentActivitiesBinding fragmentActivitiesBinding;
    private RecyclerView recyclerView;
    private MultiViewTypeAdapter adapter;
    private ArrayList<ActivityItemModel> arrayList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private Parcelable mState;
    private Bundle mBundleState;
    private ListenerRegistration registration;

    public ActivitiesFragment() {
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
        fragmentActivitiesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_activities, container, false);

        return fragmentActivitiesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().runOnUiThread(this::loadActivityData);

    }

    private void loadActivityData() {
        recyclerView = fragmentActivitiesBinding.rvItems;
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MultiViewTypeAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);

        fetchDataFromFireStore();

    }


    private void fetchDataFromFireStore() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Activity");

        // Create a query against the collection.
        com.google.firebase.firestore.Query query = collectionReference.orderBy("timeStamp", com.google.firebase.firestore.Query.Direction.DESCENDING).limit(INITIAL_LOAD);

        registration = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            arrayList.clear();


            assert queryDocumentSnapshots != null;
            for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {
                if (ds != null) {

                    Log.i(TAG, "onEvent: " + ds.getData());
                    ActivityItemModel itemModel = ds.toObject(ActivityItemModel.class);
//group data by status
                    if (ds.getData().containsKey("status")) {
                        Log.i(TAG, "status: " + ds.getData().get("status"));

                        arrayList.add(new ActivityItemModel(ActivityItemModel.TEXT_TYPE,
                                itemModel.getStatus(),
                                itemModel.getUserName(),
                                itemModel.getUserPhoto(),
                                itemModel.getTimeStamp()));

                    }
                    //group data by item description
                    else if (ds.getData().containsKey("itemDescription")) {
                        Log.i(TAG, "itemDescription: " + ds.getData().get("itemDescription"));

                        arrayList.add(new ActivityItemModel(ActivityItemModel.IMAGE_TYPE,
                                itemModel.getItemImage(),
                                itemModel.getItemDescription(),
                                itemModel.getUserName(),
                                itemModel.getUserPhoto(),
                                itemModel.getTimeStamp()));
                    }
                }
            }
            adapter.notifyDataSetChanged();


        });

       /* //get all items from fire store
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot ds : Objects.requireNonNull(task.getResult())) {
                    Log.i(TAG, "onComplete: " + ds.getId() + " " + ds.getData());

                    ActivityItemModel itemModel = ds.toObject(ActivityItemModel.class);

                    //group data by status
                    if (ds.getData().containsKey("status")) {
                        Log.i(TAG, "status: " + ds.getData().get("status"));

                        arrayList.add(new ActivityItemModel(ActivityItemModel.TEXT_TYPE,
                                itemModel.getStatus(),
                                itemModel.getUserName(),
                                itemModel.getUserPhoto(),
                                itemModel.getTimeStamp()));

                    }
                    //group data by item description
                    else if (ds.getData().containsKey("itemDescription")) {
                        Log.i(TAG, "itemDescription: " + ds.getData().get("itemDescription"));

                        arrayList.add(new ActivityItemModel(ActivityItemModel.IMAGE_TYPE,
                                itemModel.getItemImage(),
                                itemModel.getItemDescription(),
                                itemModel.getUserName(),
                                itemModel.getUserPhoto(),
                                itemModel.getTimeStamp()));
                    }

                }

                adapter.notifyDataSetChanged();

            }

        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
        // activityItemAdapter.startListening();

    }


    @Override
    public void onStop() {
        super.onStop();
        // activityItemAdapter.stopListening();
        registration.remove();
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

            new Handler().postDelayed(() -> {

                mState = mBundleState.getParcelable(MyConstants.KEY);
                layoutManager.onRestoreInstanceState(mState);
            }, 50);
        }

        recyclerView.setLayoutManager(layoutManager);
    }


}
