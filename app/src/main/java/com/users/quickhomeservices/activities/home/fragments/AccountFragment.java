package com.users.quickhomeservices.activities.home.fragments;


import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.home.MainActivity;
import com.users.quickhomeservices.adapters.StylesAdapter;
import com.users.quickhomeservices.databinding.FragmentAccountBinding;
import com.users.quickhomeservices.models.StylesItemModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
class AccountFragment extends Fragment {
   /* private static final String TAG = "AccountFragment";
    String uid, accountType, name, about, imageUrl;
    private FirebaseAuth mAuth;
    private DatabaseReference serviceAccountDbRef, serviceTypeDbRef;
    private FragmentAccountBinding accountBinding;
    private TextView txtName, txtServiceType, txtAbout;
    private CircleImageView mPhoto;*/

    private FragmentAccountBinding accountBinding;
    private DatabaseReference databaseReference;
    private StylesAdapter adapter;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        accountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);

        /*if (savedInstanceState != null) {
            accountBinding.txtAboutPerson.setText(savedInstanceState.getString(MyConstants.ABOUT));
            accountBinding.txtName.setText(savedInstanceState.getString(MyConstants.NAME));
            accountBinding.txtAccountType.setText(savedInstanceState.getString(MyConstants.ACCOUNT_TYPE));

            Glide.with(getActivity())
                    .load(savedInstanceState.getString(MyConstants.IMAGE_URL))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(accountBinding.imgProfilePhoto);
        }*/
        return accountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            return;
        }
        uid = mFirebaseUser.getUid();

        accountType = MainActivity.serviceType;


        serviceAccountDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Services").child(accountType).child(uid);

        txtAbout = accountBinding.txtAboutPerson;
        txtName = accountBinding.txtName;
        txtServiceType = accountBinding.txtAccountType;
        mPhoto = accountBinding.imgProfilePhoto;

        txtAbout.setText(MainActivity.about);
        txtName.setText(MainActivity.name);
        txtServiceType.setText(MainActivity.serviceType);

    */

        RecyclerView rv = accountBinding.rvbb;
        rv.setHasFixedSize(true);
        // databaseReference = FirebaseDatabase.getInstance().getReference().child("Styles");
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Styles")
                .child(MainActivity.uid);
        databaseReference.keepSynced(true);

        //querying the database BY NAME
        Query query = databaseReference.orderByChild("price");

  /*      PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(3)
                .build();

        DatabasePagingOptions<StylesItemModel> databasePagingOptions = new DatabasePagingOptions.Builder<StylesItemModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, StylesItemModel.class)
                .build();
*/

        FirebaseRecyclerOptions<StylesItemModel> options =
                new FirebaseRecyclerOptions.Builder<StylesItemModel>().setQuery(query,
                        StylesItemModel.class)
                        .build();

        if (Objects.requireNonNull(getActivity()).getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv.setLayoutManager(new GridLayoutManager(getContext(), 3));

        } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv.setLayoutManager(new GridLayoutManager(getContext(), 2));

        }


        adapter = new StylesAdapter(options);
        rv.setAdapter(adapter);


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
       /* outState.putString(MyConstants.ACCOUNT_TYPE, MainActivity.serviceType);
        outState.putString(MyConstants.NAME, MainActivity.name);
        outState.putString(MyConstants.ABOUT, MainActivity.about);
        outState.putString(MyConstants.IMAGE_URL, MainActivity.imageUrl);*/
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
