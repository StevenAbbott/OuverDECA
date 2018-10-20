package com.example.sbabb.ouver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.sbabb.ouver.Model.SingleFragmentActivity;

public class PaymentInfoActivity extends SingleFragmentActivity {

    public static final String EXTRA_FIRST_TIME = "firstTime";
    public static final String EXTRA_IS_JUNIOR = "isJunior";

    public static Intent newIntent(Context context, boolean firstTime, boolean isJunior) {
        Intent intent = new Intent(context, PaymentInfoActivity.class);
        intent.putExtra(EXTRA_FIRST_TIME, firstTime);
        intent.putExtra(EXTRA_IS_JUNIOR, isJunior);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        boolean firstTime = getIntent().getBooleanExtra(EXTRA_FIRST_TIME, true);
        boolean isJunior = getIntent().getBooleanExtra(EXTRA_IS_JUNIOR, true);
        return PaymentInfoFragment.newInstance(firstTime, isJunior);
    }
}
