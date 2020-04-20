package com.users.qwikhomeservices.activities.welcome;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.ItemViewClickEvents;
import com.users.qwikhomeservices.adapters.SlidePagerAdapter;
import com.users.qwikhomeservices.databinding.ActivityWelcomeBinding;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding activityWelcomeBinding;
    private ItemViewClickEvents itemViewClickEvents;
    private Timer timer = new Timer();
    private Runnable runnable;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityWelcomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        itemViewClickEvents = new ItemViewClickEvents(this);
        activityWelcomeBinding.setOnItemClick(itemViewClickEvents);

        initViews();

    }


    private void initViews() {


        SlidePagerAdapter slidePagerAdapter = new SlidePagerAdapter(this);

        activityWelcomeBinding.Viewpager.setAdapter(slidePagerAdapter);
        activityWelcomeBinding.slideDots.setViewPager(activityWelcomeBinding.Viewpager);
        activityWelcomeBinding.slideDots.setBackgroundColor(Color.BLACK);


        runnable = () -> {

            int count = activityWelcomeBinding.Viewpager.getCurrentItem();
            if (count == slidePagerAdapter.slideDescriptions.length - 1) {
                count = 0;
                activityWelcomeBinding.Viewpager.setCurrentItem(count, true);
            } else {
                count++;
                activityWelcomeBinding.Viewpager.setCurrentItem(count, true);

            }

        };


        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 2000, 2000);


    }


}
