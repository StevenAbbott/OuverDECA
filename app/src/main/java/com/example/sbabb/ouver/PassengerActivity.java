package com.example.sbabb.ouver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.sbabb.ouver.Model.SingleFragmentActivity;

public class PassengerActivity extends SingleFragmentActivity {

    public static final String EXTRA_RIDE_ID = "rideId";

    public static Intent newIntent(Context context, String rideId) {
        Intent intent = new Intent(context, PassengerActivity.class);
        intent.putExtra(EXTRA_RIDE_ID, rideId);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        String destId = getIntent().getStringExtra(EXTRA_RIDE_ID);
        return PassengerFragment.newInstance(destId);
    }
}
