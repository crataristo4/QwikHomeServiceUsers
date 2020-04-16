package com.users.quickhomeservices.activities.home.serviceTypes;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.home.MainActivity;
import com.users.quickhomeservices.adapters.TestAcceptAdatapter;
import com.users.quickhomeservices.models.RequestModel;

public class TestAcceptOrRejectActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private TestAcceptAdatapter customerRequestSent;
    SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_accept_or_reject);

        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("Requests");
        databaseReference.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadData();
    }

    private void loadData() {
        Query query;
        String uid = MainActivity.uid;


        query = databaseReference.orderByChild("senderId").equalTo(uid);

        FirebaseRecyclerOptions<RequestModel> options = new FirebaseRecyclerOptions.Builder<RequestModel>().
                setQuery(query, RequestModel.class).build();
        customerRequestSent = new TestAcceptAdatapter(options, getSupportFragmentManager());
        recyclerView.setAdapter(customerRequestSent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        customerRequestSent.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        customerRequestSent.stopListening();
    }
}
