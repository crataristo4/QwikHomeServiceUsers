package com.users.qwikhomeservices.activities.home;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.about.SettingsActivity;
import com.users.qwikhomeservices.activities.home.bottomsheets.WelcomeNoticeBottomSheet;
import com.users.qwikhomeservices.activities.welcome.SplashScreenActivity;
import com.users.qwikhomeservices.databinding.ActivityMainBinding;
import com.users.qwikhomeservices.utils.MyConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static String name, uid, imageUrl, dateJoined, firstName, lastName, mobileNumber;
    public static DatabaseReference usersAccountDbRef;
    public static FirebaseAuth mAuth;
    public static FirebaseUser firebaseUser;
    private static Object mContext;
    private ActivityMainBinding activityMainBinding;

    private static Context getAppContext() {
        return (Context) mContext;
    }

    public static void retrieveSingleUserDetails() {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> updateToken = new HashMap<>();
        updateToken.put("deviceToken", deviceToken);
        usersAccountDbRef.updateChildren(updateToken);

        usersAccountDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    //name = (String) dataSnapshot.child("fullName").getValue();
                    firstName = (String) dataSnapshot.child("firstName").getValue();
                    lastName = (String) dataSnapshot.child("lastName").getValue();
                    name = firstName.concat(" ").concat(lastName);
                    dateJoined = (String) dataSnapshot.child("dateJoined").getValue();
                    mobileNumber = (String) dataSnapshot.child("mobileNumber").getValue();
                    imageUrl = (String) dataSnapshot.child("image").getValue();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mContext = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        setUpAppBarConfig();

    }

    private void checkDisplayAlertDialog() {
        SharedPreferences pref = getSharedPreferences(MyConstants.PREFS, 0);
        boolean alertShown = pref.getBoolean(MyConstants.IS_DIALOG_SHOWN, false);

        if (!alertShown) {
            new Handler().postDelayed(() -> {

                WelcomeNoticeBottomSheet welcomeNoticeBottomSheet = new WelcomeNoticeBottomSheet();
                welcomeNoticeBottomSheet.setCancelable(false);
                welcomeNoticeBottomSheet.show(getSupportFragmentManager(), "welcome");

            }, 2000);

            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean(MyConstants.IS_DIALOG_SHOWN, true);
            edit.apply();
        }
    }

    private void SendUserToLoginActivity() {
        Intent Login = new Intent(MainActivity.this, SplashScreenActivity.class);
        Login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Login);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.main_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent gotoSettingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(gotoSettingsIntent);

        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void setUpAppBarConfig() {
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_activities, R.id.navigation_home, R.id.navigation_request)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(activityMainBinding.bottomNavigationView, navController);

        activityMainBinding.bottomNavigationView.setOnNavigationItemReselectedListener(menuItem -> {
            //do nothing
        });
    }

    private void checkUid() {
        usersAccountDbRef = FirebaseDatabase.getInstance()
                .getReference().child("Users");
        usersAccountDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    if (!dataSnapshot.hasChild(Objects.requireNonNull(mAuth.getUid()))) {

                        SendUserToLoginActivity();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            SendUserToLoginActivity();
        } else {


            uid = firebaseUser.getUid();
            usersAccountDbRef = FirebaseDatabase.getInstance()
                    .getReference().child("Users")
                    .child(uid);
            usersAccountDbRef.keepSynced(true);
            checkDisplayAlertDialog();
            runOnUiThread(MainActivity::retrieveSingleUserDetails);
        }
        //todo check null db

    }


}
