package com.users.qwikhomeservices.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.users.qwikhomeservices.models.Users;
import com.users.qwikhomeservices.repository.AllServicesRepository;

import java.util.ArrayList;

public class AllServicesViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Users>> artisanMutableLiveData;

    public void init(Context context, String accountType) {
        if (artisanMutableLiveData != null) {
            return;
        }

        artisanMutableLiveData = AllServicesRepository.getInstance(context).getArtisans(accountType);
    }

    public LiveData<ArrayList<Users>> getArtisanMutableLiveData() {
        return artisanMutableLiveData;
    }
}
