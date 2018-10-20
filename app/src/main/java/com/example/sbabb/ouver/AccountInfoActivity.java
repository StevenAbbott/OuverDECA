package com.example.sbabb.ouver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.sbabb.ouver.Model.SingleFragmentActivity;

public class AccountInfoActivity extends SingleFragmentActivity {

    public static final String EXTRA_FIRST_TIME = "firstTime";

    public static Intent newIntent(Context context, boolean firstTime) {
        Intent intent = new Intent(context, AccountInfoActivity.class);
        intent.putExtra(EXTRA_FIRST_TIME, firstTime);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        boolean firstTime = getIntent().getBooleanExtra(EXTRA_FIRST_TIME, true);
        return AccountInfoFragment.newInstance(firstTime);
    }

}
