package com.users.quickhomeservices.activities.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.auth.LoginActivity;
import com.users.quickhomeservices.activities.auth.signup.SignUpServicePersonelActivity;
import com.users.quickhomeservices.adapters.SlidePagerAdapter;
import com.users.quickhomeservices.databinding.ActivityOnboardingScreenBinding;

import me.relex.circleindicator.CircleIndicator;

public class OnboardingScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Removes anything relating to a title
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //Splash Screen activity displays in a full screen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivityOnboardingScreenBinding activityOnboardingScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding_screen);

        CircleIndicator indicator = activityOnboardingScreenBinding.slideDots;
        ViewPager viewPager = activityOnboardingScreenBinding.viewPager;
        SlidePagerAdapter slidePagerAdapter = new SlidePagerAdapter(this);

        viewPager.setAdapter(slidePagerAdapter);
        indicator.setViewPager(viewPager);
        indicator.setBackgroundColor(getResources().getColor(R.color.amber));
    }

    public void loginSignUpAction(View view) {
        if (view.getId() == R.id.btnBack) {
            startActivity(
                    new Intent(OnboardingScreenActivity.this, LoginActivity.class)
            );
        } else if (view.getId() == R.id.btnNext) {
            startActivity(
                    new Intent(OnboardingScreenActivity.this, SignUpServicePersonelActivity.class)
            );
        }
    }
}
