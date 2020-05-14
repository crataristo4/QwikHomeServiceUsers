package com.users.qwikhomeservices.activities.home.fragments;


import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.adapters.ServiceUsersAdapter;
import com.users.qwikhomeservices.databinding.FragmentAllArtisansAccountBinding;
import com.users.qwikhomeservices.models.Users;
import com.users.qwikhomeservices.utils.MyConstants;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllArtisansAccountFragment extends Fragment {
    private FragmentAllArtisansAccountBinding allArtisansAccountBinding;
    private ServiceUsersAdapter adapter;
    private String accountType;
    private ArrayList<Users> arrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private GridLayoutManager layoutManager;
    private Parcelable mState;
    private Bundle mBundleState;
    private RecyclerView recyclerView;
    private Query query;

    public AllArtisansAccountFragment() {
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
        allArtisansAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_artisans_account, container, false);

        return allArtisansAccountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent getIntent = requireActivity().getIntent();

        if (getIntent != null) {

            switch (Objects.requireNonNull(getIntent.getStringExtra(MyConstants.ACCOUNT_TYPE))) {
                case MyConstants.BARBERS:
                    accountType = MyConstants.BARBERS;
                    // requireActivity().setTitle(accountType);

                    break;
                case MyConstants.WOMEN_HAIR_STYLIST:
                    accountType = MyConstants.WOMEN_HAIR_STYLIST;
                    // requireActivity().setTitle(accountType);

                    break;
                case MyConstants.INTERIOR_DERCORATOR:
                    accountType = MyConstants.INTERIOR_DERCORATOR;
                    // requireActivity().setTitle(accountType);

                    break;

                case MyConstants.CARPENTERS:
                    accountType = MyConstants.CARPENTERS;
                    // setTitle(accountType);

                    break;
                case MyConstants.MECHANICS:
                    accountType = MyConstants.MECHANICS;
                    // setTitle(accountType);

                    break;
                case MyConstants.PEST_CONTROLS:
                    accountType = MyConstants.PEST_CONTROLS;
                    // setTitle(accountType);

                    break;
                case MyConstants.PLUMBERS:
                    accountType = MyConstants.PLUMBERS;
                    // setTitle(accountType);

                    break;
                case MyConstants.TILERS:
                    accountType = MyConstants.TILERS;
                    // setTitle(accountType);

                    break;
                case MyConstants.TV_INSTALLERS:
                    accountType = MyConstants.TV_INSTALLERS;
                    // setTitle(accountType);

                    break;
                case MyConstants.WELDERS:
                    accountType = MyConstants.WELDERS;
                    //setTitle(accountType);

                    break;
                case MyConstants.ROLLERS:
                    accountType = MyConstants.ROLLERS;
                    //  setTitle(accountType);

                    break;
                case MyConstants.GARDENERS:
                    accountType = MyConstants.GARDENERS;
                    // setTitle(accountType);

                    break;
                case MyConstants.PAINTERS:
                    accountType = MyConstants.PAINTERS;


                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + getIntent.getStringExtra(MyConstants.ACCOUNT_TYPE));

            }

            requireActivity().setTitle(accountType);


        }


        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(MyConstants.SERVICES).child(MyConstants.SERVICE_TYPE);
        databaseReference.keepSynced(true);
        query = databaseReference.orderByChild("accountType").equalTo(accountType);

        requireActivity().runOnUiThread(this::initRecyclerView);

    }

    private void initRecyclerView() {
        recyclerView = allArtisansAccountBinding.rvAllBarbers;
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

            layoutManager = new GridLayoutManager(requireContext(), 2);
            recyclerView.setLayoutManager(layoutManager);

        } else {

            layoutManager = new GridLayoutManager(requireContext(), 3);
            recyclerView.setLayoutManager(layoutManager);

        }

        adapter = new ServiceUsersAdapter(arrayList, requireActivity());
        recyclerView.setAdapter(adapter);

        loadData();


    }

    private void loadData() {
        //querying the database BY artisan type
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                    arrayList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Users artisans = ds.getValue(Users.class);
                        arrayList.add(artisans);
                    }

                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
