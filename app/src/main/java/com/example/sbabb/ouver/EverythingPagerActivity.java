package com.example.sbabb.ouver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

public class EverythingPagerActivity extends AppCompatActivity {

    private static final String EXTRA_SCREEN_NUM = "screenNum";
    private static final String EXTRA_IS_JUNIOR = "isJunior";
    private static final String EXTRA_RIDE_ID = "rideId";
    private static final String SAVED_SCREEN_NUM = "screenNum";
    private static final String SAVED_IS_JUNIOR = "isJunior";

    private static final int REQUEST_RIDE = 0;
    private static final int REQUEST_DRIVER = 1;
    private static final String EXTRA_DEST_ID = "destId";


    private ViewPager mViewPager;


    private int mScreenNum;
    private boolean mJunior;

    public static Intent newIntent(Context packageContext, boolean isJunior, int screen) {
        Intent intent = new Intent(packageContext, EverythingPagerActivity.class);
        intent.putExtra(EXTRA_SCREEN_NUM, screen);
        intent.putExtra(EXTRA_IS_JUNIOR, isJunior);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everything_pager);

        Log.d("EverythingPager", "You're here!");

        mScreenNum = getIntent().getIntExtra(EXTRA_SCREEN_NUM, 1);
        mJunior = getIntent().getBooleanExtra(EXTRA_IS_JUNIOR, true);

        if (savedInstanceState != null) {
            mScreenNum = savedInstanceState.getInt(SAVED_SCREEN_NUM);
            mJunior = savedInstanceState.getBoolean(SAVED_IS_JUNIOR);
        }

        Log.d("EverythingPager", "just got stuff from extras and args and screen is " + mScreenNum);

        mViewPager = (ViewPager) findViewById(R.id.everything_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Log.d("EverythingPager", "getItem called with " + position);
                if (position == 0) {
                    return AccountInfoFragment.newInstance(false);
                } else {
                    if (mJunior) {
                        return DestinationListFragment.newInstance();
                    } else {
                        return DriverFragment.newInstance();
                    }
                }
            }

            @Override
            public int getCount() {
                Log.d("EverythingPager", "get count called");
                return 2; // hardcoded value for the number of screens currently in use
            }
        });

        mViewPager.setCurrentItem(mScreenNum);
        Log.d("EverythingPager", "setCurrentItem called with " + mScreenNum);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("EverythingPager", "on destroy called");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SCREEN_NUM, mScreenNum);
        outState.putBoolean(SAVED_IS_JUNIOR, mJunior);
        Log.d("EverythingPager", "savedInstanceState was just called screen " + mScreenNum + " junior: " + mJunior);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("EverythingPager", "onActivityResult() called");
        /*if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_RIDE) {
            String rideId = data.getStringExtra(EXTRA_RIDE_ID);
            Fragment f = PassengerFragment.newInstance(rideId);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        } else if (requestCode == REQUEST_DRIVER) { // from a destination fragment
            Log.d("EverythingPager", "new passenger fragment about to be created");
            String destId = data.getStringExtra(EXTRA_RIDE_ID);
            Fragment f = PassengerFragment.newInstance(destId);
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }*/
    }
}
