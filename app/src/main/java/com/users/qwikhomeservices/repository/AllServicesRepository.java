package com.users.qwikhomeservices.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.users.qwikhomeservices.datasource.AllServicesDataSource;
import com.users.qwikhomeservices.models.Users;
import com.users.qwikhomeservices.utils.MyConstants;

import java.util.ArrayList;

public class AllServicesRepository {

    public static AllServicesRepository instance;
    public static AllServicesDataSource allServicesDataSource;
    static Context context;
    private ArrayList<Users> arrayList = new ArrayList<>();

    public static AllServicesRepository getInstance(Context context) {

        context = context;
        if (instance == null) {

            instance = new AllServicesRepository();
        }

        allServicesDataSource = (AllServicesDataSource) context;
        return instance;
    }


    public MutableLiveData<ArrayList<Users>> getArtisans(String accountType) {

        if (arrayList.size() == 0) {

            loadData(accountType);

        }

        MutableLiveData<ArrayList<Users>> listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.setValue(arrayList);

        return listMutableLiveData;

    }

    private void loadData(String accountType) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(MyConstants.SERVICES).child(MyConstants.SERVICE_TYPE);
        databaseReference.keepSynced(true);
        Query query = databaseReference.orderByChild("accountType").equalTo(accountType);

        //querying the database BY artisan type
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                    // arrayList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Users artisans = ds.getValue(Users.class);
                        arrayList.add(artisans);
                    }

                    allServicesDataSource.onAllServicesLoaded();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
