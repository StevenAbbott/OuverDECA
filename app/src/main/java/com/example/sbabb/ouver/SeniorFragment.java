package com.example.sbabb.ouver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SeniorFragment extends Fragment{

    private static final String EXTRA_SCREEN_NUM = "screenNum";
    private static final String SAVED_SCREEN_NUM = "screenNum";

    private Switch mSeniorSwitch;

    private int mScreenNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_senior, container, false);

        if (savedInstanceState != null) {
            mScreenNum = savedInstanceState.getInt(SAVED_SCREEN_NUM);
        }

        updateChildFragment();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SCREEN_NUM, mScreenNum);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_senior, menu);

        MenuItem seniorSwitchItem = menu.findItem(R.id.senior_switch_item);
        seniorSwitchItem.setActionView(R.layout.switch_layout);
        mSeniorSwitch = seniorSwitchItem
                .getActionView().findViewById(R.id.senior_switch);

        mSeniorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateChildFragment();
            }
        });
    }

    private void updateChildFragment() {
        Fragment childFragment;
        if (mSeniorSwitch.isChecked()) {
            childFragment = DestinationListFragment.newInstance();
        } else {
            childFragment = DriverFragment.newInstance();
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        /*FragmentTransaction transaction = .beginTransaction()
                .add(R.id.child_fragment_holder, childFragment)
                .commit();
        transaction.replace(R.id.child_fragment_holder, childFragment).commit();*/
    }

    public static SeniorFragment newInstance(int screenNum) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_SCREEN_NUM, screenNum);

        SeniorFragment fragment = new SeniorFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
