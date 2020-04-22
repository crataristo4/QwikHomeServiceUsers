package com.users.qwikhomeservices.activities.home.fragments;

import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.MainActivity;
import com.users.qwikhomeservices.adapters.RequestAdapter;
import com.users.qwikhomeservices.databinding.FragmentRequestBinding;
import com.users.qwikhomeservices.models.RequestModel;


public class RequestFragment extends Fragment {
    private FragmentRequestBinding fragmentRequestBinding;
    private RequestAdapter requestAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static DatabaseReference requestDbRef;
    private RecyclerView recyclerView;

    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        requestDbRef = FirebaseDatabase.getInstance().getReference("Requests");
        requestDbRef.keepSynced(true);

        swipeRefreshLayout.setColorSchemeResources(R.color.amber, R.color.fb,
                R.color.colorAccent, R.color.colorAsh, R.color.colorOrange);

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {

            // TODO: 12-Apr-20 refresh data on refreshing
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);

        }, 3000));


        loadData();


    }

    private void loadData() {

        String uid = MainActivity.uid;

        Query query = requestDbRef.orderByChild("senderId").equalTo(uid);

        FirebaseRecyclerOptions<RequestModel> options = new FirebaseRecyclerOptions.Builder<RequestModel>().
                setQuery(query, RequestModel.class).build();
        requestAdapter = new RequestAdapter(options);
        requestAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(requestAdapter);



    }


    @Override
    public void onStart() {
        super.onStart();
        requestAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        requestAdapter.stopListening();
    }
}
