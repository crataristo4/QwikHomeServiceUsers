package com.users.qwikhomeservices.activities.home.serviceTypes;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.MainActivity;
import com.users.qwikhomeservices.activities.home.bottomsheets.SendRequestBottomSheet;
import com.users.qwikhomeservices.adapters.ItemStyleAdapter;
import com.users.qwikhomeservices.databinding.ActivityDetailsScrollingBinding;
import com.users.qwikhomeservices.models.StylesItemModel;
import com.users.qwikhomeservices.utils.MyConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsScrollingActivity extends AppCompatActivity {
    private int numberOfItems = 0;
    private ActivityDetailsScrollingBinding activityDetailsScrollingBinding;
    private DatabaseReference databaseReference;
    private ItemStyleAdapter adapter;
    private List<StylesItemModel> itemsList;
    private String servicePersonName, servicePersonAbout, servicePersonPhoto,
            servicePersonId, servicePersonMobileNumber;
    private long mLastClickTime = 0;
    private Intent intent;
    private RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        activityDetailsScrollingBinding = DataBindingUtil.setContentView(this, R.layout.activity_details_scrolling);
        setSupportActionBar(activityDetailsScrollingBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        initViews();
        loadStyleItems();


        //get number of items in database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    numberOfItems = (int) ds.getChildrenCount();

                }

                //check if the adapter is empty and notify users
                if (numberOfItems == 0) {

                    activityDetailsScrollingBinding.contentDetails.txtStyleLabel.setText(getResources().getString(R.string.noStyles));


                } else {

                    activityDetailsScrollingBinding.contentDetails.txtStyleLabel.setText(getString(R.string.styles_offered));

                    Log.i("Number of items: ", " " + numberOfItems);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        intent = getIntent();
        if (intent != null) {
            String position = intent.getStringExtra("position");
            assert position != null;
            servicePersonName = intent.getStringExtra("fullName");
            servicePersonAbout = intent.getStringExtra("about");
            servicePersonPhoto = intent.getStringExtra("image");
            servicePersonId = intent.getStringExtra("servicePersonId");
            servicePersonMobileNumber = intent.getStringExtra("mobileNumber");
        }

        activityDetailsScrollingBinding.fabCall.setOnClickListener(view -> Snackbar.make(view,
                "Call ".concat(servicePersonName),
                Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.WHITE)
                .setTextColor(Color.WHITE)
                .setBackgroundTint(getColor(R.color.purple))
                .setDuration(8000)
                .setAction("CALL NOW", v -> {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.fromParts("tel", servicePersonMobileNumber, null));
                    callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(callIntent);

                }).show());


        itemsList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("Styles")
                .child(servicePersonId);
        databaseReference.keepSynced(true);

        activityDetailsScrollingBinding.collapsingToolBar.setTitle(servicePersonName);
        activityDetailsScrollingBinding.contentDetails.txtAbout.setText(servicePersonAbout);

        Glide.with(this)
                .load(servicePersonPhoto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(activityDetailsScrollingBinding.userImage);

        recyclerView = activityDetailsScrollingBinding.contentDetails.rvStylesItem;
        recyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        }

        adapter = new ItemStyleAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view, stylesItemModel) -> {
            //scroll app bar to state collapsed when item is clicked
            activityDetailsScrollingBinding.appBar.setExpanded(false, true);

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }

            mLastClickTime = SystemClock.elapsedRealtime();

            String price = stylesItemModel.getPrice();
            String itemStyleName = stylesItemModel.getItemDescription();
            String imageItem = stylesItemModel.getItemImage();

            Bundle bundle = new Bundle();
            bundle.putString(MyConstants.PRICE, price);
            bundle.putString(MyConstants.ITEM_DESCRIPTION, itemStyleName);
            bundle.putString(MyConstants.IMAGE_URL, imageItem);
            //pass details of service person to bottom sheet
            bundle.putString(MyConstants.SERVICE_PERSON_NAME, servicePersonName);
            bundle.putString(MyConstants.SERVICE_PERSON_ID, servicePersonId);
            bundle.putString(MyConstants.SERVICE_PERSON_PHOTO, servicePersonPhoto);
            //pass users name , user photo , user id to bundle
            // String fullName = MainActivity.name;
            String firstName = MainActivity.firstName;
            String lastName = MainActivity.lastName;
            String userMobileNumber = MainActivity.mobileNumber;
            String userId = MainActivity.uid;
            String userPhoto = MainActivity.imageUrl;

            // bundle.putString(MyConstants.FULL_NAME, fullName);
            bundle.putString(MyConstants.UID, userId);
            bundle.putString(MyConstants.USER_IMAGE_URL, userPhoto);
            bundle.putString(MyConstants.FIRST_NAME, firstName);
            bundle.putString(MyConstants.LAST_NAME, lastName);
            bundle.putString(MyConstants.PHONE_NUMBER, userMobileNumber);

            SendRequestBottomSheet sendRequestBottomSheet = new SendRequestBottomSheet();
            sendRequestBottomSheet.setCancelable(false);
            sendRequestBottomSheet.setArguments(bundle);
            sendRequestBottomSheet.show(getSupportFragmentManager(), MyConstants.SEND_REQUEST_TAG);

        });


    }

    private void loadStyleItems() {

        runOnUiThread(() -> {

            Query query = databaseReference.orderByChild("price");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            StylesItemModel stylesItemModel = ds.getValue(StylesItemModel.class);
                            itemsList.add(stylesItemModel);
                        }

                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // adapter.startListening();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.stopListening();

    }

    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return super.onNavigateUp();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
