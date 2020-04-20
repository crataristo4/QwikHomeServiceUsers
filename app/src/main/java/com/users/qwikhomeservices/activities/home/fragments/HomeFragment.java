package com.users.qwikhomeservices.activities.home.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.serviceTypes.AllServicesActivity;
import com.users.qwikhomeservices.databinding.FragmentHomeBinding;
import com.users.qwikhomeservices.utils.MyConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding fragmentHomeBinding;
    private Intent intent;
    private ArrayAdapter<String> adapter;


    public HomeFragment() {
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
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        return fragmentHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = fragmentHomeBinding.servicesListView;
        //string list from xml
        List<String> serviceList = Arrays.asList(getResources().getStringArray(R.array.services));

        adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_1, serviceList);
        listView.setAdapter(adapter);

        //on item click
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            switch (position) {
                case 0:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.WOMEN_HAIR_STYLIST);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.INTERIOR_DERCORATOR);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.BARBERS);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.CARPENTERS);
                    startActivity(intent);
                    break;
                case 4:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.MECHANICS);
                    startActivity(intent);
                    break;
                case 5:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.PEST_CONTROLS);
                    startActivity(intent);
                    break;
                case 6:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.PLUMBERS);
                    startActivity(intent);
                    break;
                case 7:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.TV_INSTALLERS);
                    startActivity(intent);
                    break;
                case 8:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.TILERS);
                    startActivity(intent);
                    break;
                case 9:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.WELDERS);
                    startActivity(intent);
                    break;
                case 10:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.ROLLERS);
                    startActivity(intent);
                    break;
                case 11:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.GARDENERS);
                    startActivity(intent);
                    break;
                case 12:
                    intent = new Intent(getContext(), AllServicesActivity.class);
                    intent.putExtra(MyConstants.ACCOUNT_TYPE, MyConstants.PAINTERS);
                    startActivity(intent);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + MyConstants.SERVICES);




            }
        });


        fragmentHomeBinding.searchList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });


    }
}
