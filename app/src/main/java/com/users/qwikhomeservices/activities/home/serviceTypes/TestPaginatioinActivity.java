package com.users.qwikhomeservices.activities.home.serviceTypes;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.adapters.ItemStyleAdapter;
import com.users.qwikhomeservices.adapters.MultiViewTypeAdapter;
import com.users.qwikhomeservices.models.ActivityItemModel;
import com.users.qwikhomeservices.models.StylesItemModel;
import com.users.qwikhomeservices.utils.DisplayViewUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TestPaginatioinActivity extends AppCompatActivity {
    private static final String TAG = "TestPaginatioinActivity";
    final int pagePerLimit = 3;
    int lastVisibleItem;
    boolean isMaxData = false;
    String lastNode = "";
    private int currentItem;
    private int totalItem = 0;
    private int scrolledOutItem;
    private boolean isScrolled = false;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ItemStyleAdapter itemStyleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String lastKey = "";

    private List<StylesItemModel> stylesItemModelList;
    private static final int INITIAL_LOAD = 15;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference collectionReference = db.collection("Test");
    private MultiViewTypeAdapter adapter;
    private ArrayList<ActivityItemModel> arrayList;

    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_paginatioin);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        assert firebaseUser != null;
        String uid = firebaseUser.getUid();



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new MultiViewTypeAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorAsh, R.color.colorOrange);
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            loadData();
        }, 3000);


        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);

                //refresh data
                DisplayViewUI.displayToast(TestPaginatioinActivity.this, "refreshing data");
            }

        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolled = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = layoutManager.getChildCount();
                totalItem = layoutManager.getItemCount();
                scrolledOutItem = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0) {

                    if (isScrolled && (currentItem + scrolledOutItem == totalItem) && lastKey != null) {
                        isScrolled = false;
                        //fetchData...
                        DisplayViewUI.displayToast(TestPaginatioinActivity.this, "fetching data...");

                        // TODO: 12-Apr-20  paginate through items...


                    }

                }

            }
        });


    }


    private void loadData() {
        Query query = collectionReference.orderBy("timeStamp", Query.Direction.DESCENDING).limit(INITIAL_LOAD);

        registration = query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            arrayList.clear();

            assert queryDocumentSnapshots != null;
            for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {

                ActivityItemModel itemModel = ds.toObject(ActivityItemModel.class);
                //get data from model
                String userName = itemModel.getUserName();
                String userPhoto = itemModel.getUserPhoto();
                String itemDescription = itemModel.getItemDescription();
                String status = itemModel.getStatus();
                String itemImage = itemModel.getItemImage();
                long timeStamp = itemModel.getTimeStamp();
                int numOfLikes = itemModel.getNumOfLikes();
                int numOfComments = itemModel.getNumOfComments();
                String id = ds.getId();

                //group data by status
                if (Objects.requireNonNull(ds.getData()).containsKey("status")) {

                    arrayList.add(new ActivityItemModel(ActivityItemModel.TEXT_TYPE,
                            status,
                            userName,
                            userPhoto,
                            timeStamp,
                            id,
                            numOfLikes,
                            numOfComments));

                }
                //group data by item description
                else if (ds.getData().containsKey("itemDescription")) {
                    arrayList.add(new ActivityItemModel(ActivityItemModel.IMAGE_TYPE,
                            itemImage,
                            itemDescription,
                            userName,
                            userPhoto,
                            timeStamp,
                            id,
                            numOfLikes,
                            numOfComments
                    ));


                }
            }

            adapter.notifyDataSetChanged();


        });
    }


}
