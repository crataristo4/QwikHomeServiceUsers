package com.users.qwikhomeservices.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.users.qwikhomeservices.models.Users;


public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static SharedPrefManager instance;
    private Context context;

    private SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }

        return instance;
    }

    //save user details
    public void saveUsers(Users users) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accountType", users.getAccountType());
        editor.apply();
    }


    //check if user is already logged in
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getInt("id", -1) != -1;
    }


    //get user logged in
    public String getUsa() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString("accountType", "");


    }


    //clear content when the user logs out
    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
