package com.users.qwikhomeservices.activities.home.serviceTypes;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.adapters.ServiceUsersAdapter;
import com.users.qwikhomeservices.databinding.ActivityAllServicesBinding;
import com.users.qwikhomeservices.models.Users;
import com.users.qwikhomeservices.utils.MyConstants;

import java.util.Objects;

//TODO change class name
public class AllServicesActivity extends AppCompatActivity {
    private ActivityAllServicesBinding allServicesBinding;
    private ServiceUsersAdapter adapter;
    private String accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allServicesBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_services);

        initRecyclerView();

    }

    private void initRecyclerView() {

        Intent getIntent = getIntent();

        if (getIntent != null) {

            switch (Objects.requireNonNull(getIntent.getStringExtra(MyConstants.ACCOUNT_TYPE))) {
                case MyConstants.BARBERS:
                    accountType = MyConstants.BARBERS;
                    setTitle(accountType);

                    break;
                case MyConstants.WOMEN_HAIR_STYLIST:
                    accountType = MyConstants.WOMEN_HAIR_STYLIST;
                    setTitle(accountType);

                    break;
                case MyConstants.INTERIOR_DERCORATOR:
                    accountType = MyConstants.INTERIOR_DERCORATOR;
                    setTitle(accountType);

                    break;

                case MyConstants.CARPENTERS:
                    accountType = MyConstants.CARPENTERS;
                    setTitle(accountType);

                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + getIntent.getStringExtra(MyConstants.ACCOUNT_TYPE));


            }

        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(MyConstants.SERVICES);
        databaseReference.keepSynced(true);

        RecyclerView recyclerView = allServicesBinding.rvAllBarbers;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //querying the database BY NAME
        Query query = databaseReference.orderByChild("accountType").equalTo(accountType);
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>().setQuery(query,
                        Users.class)
                        .build();

        //DISPLAY different layout for screen orientation
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        } else {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        }

        adapter = new ServiceUsersAdapter(options, AllServicesActivity.this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
