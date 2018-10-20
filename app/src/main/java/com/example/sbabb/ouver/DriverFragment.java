package com.example.sbabb.ouver;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.Driver;
import com.example.sbabb.ouver.Model.Ride;
import com.example.sbabb.ouver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class DriverFragment extends Fragment {

    private static final String ARG_RIDE_ID  = "rideId";
    private static final String ARG_SEARCHING = "searching";

    private Button mCancelRideButton;
    private Button mUpdateTripButton;
    private Button mCallPassengerButton;
    private TextView mDestTextView;
    private TextView mDestCapTextView;
    private TextView mPassengerTextView;
    private TextView mFareTextView;
    private TextView mFareCapTextView;
    private TextView mPassengerCapTextView;
    private ConstraintLayout mSearchIndicatorLayout;
    private Switch mSearchSwitch;
    private TextView mPassLocCapTextView;
    private TextView mPassLocTextView;
    MenuItem seniorSwitchItem;

    private DestinationLab mdl;
    private User mUser;
    private Driver mDriver;
    private boolean mUserPickedUp;

    private Ride mRide;
    private String mRideUid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mdl = DestinationLab.get(getContext());
        mRideUid = getArguments().getString(ARG_RIDE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver, container, false);

        mSearchIndicatorLayout = (ConstraintLayout) v.findViewById(R.id.search_indicator_driver);
        mCallPassengerButton = (Button) v.findViewById(R.id.call_passenger_button);
        mCancelRideButton = (Button) v.findViewById(R.id.cancel_ride_d_button);
        mUpdateTripButton = (Button) v.findViewById(R.id.update_trip_status);
        mDestTextView = (TextView) v.findViewById(R.id.dest_tv);
        mDestCapTextView = (TextView) v.findViewById(R.id.dest_cap_tv);
        mPassengerCapTextView = (TextView) v.findViewById(R.id.pass_cap_tv);
        mPassengerTextView = (TextView) v.findViewById(R.id.pass_tv);
        mFareTextView = (TextView) v.findViewById(R.id.fare_tv);
        mFareCapTextView = (TextView) v.findViewById(R.id.fare_cap_tv);
        mSearchSwitch = (Switch) v.findViewById(R.id.search_switch);
        mPassLocCapTextView = (TextView) v.findViewById(R.id.pass_loc_cap_tv);
        mPassLocTextView = (TextView) v.findViewById(R.id.pass_loc_tv);


        if(savedInstanceState != null) {
            mRideUid = savedInstanceState.getString(ARG_RIDE_ID);
            mSearchSwitch.setChecked(savedInstanceState.getBoolean(ARG_SEARCHING));
        }



        mCallPassengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.putExtra(Intent.EXTRA_PHONE_NUMBER, mRide.getmPassenger().getmCell());
                startActivity(i);
            }
        });

        mCancelRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DriverFragment", "cancelRideBut.onClick() pre leave ride() searchSwitchChecked: " + mSearchSwitch.isChecked());
                leaveRide();
                Log.d("DriverFragment", "cancelRideBut.onClick() post leave ride() searchSwitchChecked: " + mSearchSwitch.isChecked());
                //Intent i = EverythingPagerActivity.newIntent(getContext(), false, 1);
                //startActivity(i);
                //getActivity().finish();
            }
        });

        mSearchSwitch.setChecked(false);
        mSearchSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchSwitch.isChecked()) {
                    mSearchIndicatorLayout.setVisibility(View.VISIBLE);
                    Log.d("DriverFrag", "searching for passenger");
                    Log.d("DriverFragment", "mSearchSwitch.onClick(): search switch checked: " + mSearchSwitch.isChecked() + " searching now");
                    searchForRide();
                } else {
                    mSearchIndicatorLayout.setVisibility(View.GONE);
                }
                updateUI();
            }
        });

        mUpdateTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserPickedUp) {
                    mdl.getRideRef(mRideUid).removeValue();
                    leaveRide();
                } else {
                    mUserPickedUp = true;
                    mUpdateTripButton.setText("Press here when you have dropped off the user at " + mRide.getmPassenger().getmDestination().getmName());
                }
            }
        });

        updateUI();

        return v;
    }

    private void searchForRide() {
        mdl.getAllRidesRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mSearchSwitch.isChecked()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Ride ride = snapshot.getValue(Ride.class);
                        if ((ride.getmDriver() == null) && (ride.getmDriver().getmName() != mUser.getmFname())) {
                            Log.d("DriverFrag", "found ride with null driver");
                            joinRide(ride);
                        }
                    }
                    Log.d("User field updated.", "onDataChange:success");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User field update fail.", "onDataChange:failure");
                mRide = null;
            }
        });
    }

    private void getRideInfo() {
        if (mRideUid != null) {
            mdl.getRideRef(mRideUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (mRideUid != null) {
                        mRide = dataSnapshot.getValue(Ride.class);
                        mDestTextView.setText(mRide.getmDestination().getmName());
                        mPassengerTextView.setText(mRide.getmPassenger().getmName());
                        mFareTextView.setText("" + mRide.calculateFare());
                        mPassLocTextView.setText("Example: \"Junior Lot.\"");
                        if (mRide.getmPassenger().getmCell() != null) {
                            mCallPassengerButton.setVisibility(View.VISIBLE);
                        }
                        Log.d("User field updated.", "onDataChange:success");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("User field update fail.", "onDataChange:failure");
                }
            });
        }
    }

    private void updateUI() {
        getRideInfo();

        if (mRideUid != null) {
            mCancelRideButton.setVisibility(View.VISIBLE);
            mSearchIndicatorLayout.setVisibility(View.GONE);
            mSearchSwitch.setVisibility(View.GONE);
            mDestCapTextView.setVisibility(View.VISIBLE);
            mDestTextView.setVisibility(View.VISIBLE);
            mPassengerTextView.setVisibility(View.VISIBLE);
            mPassengerCapTextView.setVisibility(View.VISIBLE);
            mFareCapTextView.setVisibility(View.VISIBLE);
            mFareTextView.setVisibility(View.VISIBLE);
            mPassLocTextView.setVisibility(View.VISIBLE);
            mPassLocCapTextView.setVisibility(View.VISIBLE);
            mUpdateTripButton.setVisibility(View.VISIBLE);
        } else {
            mSearchSwitch.setVisibility(View.VISIBLE);
            if (mSearchSwitch.isChecked()) {
                mSearchIndicatorLayout.setVisibility(View.VISIBLE);
            } else {
                mSearchIndicatorLayout.setVisibility(View.GONE);
            }
            mDestCapTextView.setVisibility(View.GONE);
            mDestTextView.setVisibility(View.GONE);
            mPassengerTextView.setVisibility(View.GONE);
            mPassengerCapTextView.setVisibility(View.GONE);
            mFareCapTextView.setVisibility(View.GONE);
            mFareTextView.setVisibility(View.GONE);
            mPassLocTextView.setVisibility(View.GONE);
            mPassLocCapTextView.setVisibility(View.GONE);
            mCallPassengerButton.setVisibility(View.GONE);
            mUpdateTripButton.setVisibility(View.GONE);
            mCancelRideButton.setVisibility(View.GONE);
        }

        mdl.getUserRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                Log.d("User field updated.", "onDataChange:success");
                mDriver = new Driver(mUser);
                if (mSearchSwitch.isChecked()) {
                    Log.d("DriverFragment", "dml.getUserRef(): search switch checked: " + mSearchSwitch.isChecked() + " searching now");
                    searchForRide();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User field update fail.", "onDataChange:failure");
            }
        });
    }

    public void joinRide(Ride ride) {
        mRide = ride;
        mRideUid = ride.getmUid();
        mUser.setmRide(mRide);
        mRide.setmDriver(mDriver);
        mdl.updateRide(ride);
        updateUI();
    }

    public void leaveRide() {
        mRide.setmDriver(null);
        mdl.updateRide(mRide);
        mRide = null;
        mRideUid = null;
        mSearchSwitch.setChecked(false);
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_senior, menu);

        seniorSwitchItem = menu.findItem(R.id.senior_switch_item);
        seniorSwitchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!seniorSwitchItem.isChecked()) {
                    Log.d("DriverFragment", "seniorSwitch checked starting DestinationListFragment");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.driver_fragment_container, DestinationListFragment.newInstance()).commit();
                } else {
                    Log.d("DriverFragment", "seniorSwitch unchecked starting DriverFragment");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.driver_fragment_container, DriverFragment.newInstance()).commit();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.senior_switch_item:
                if (!seniorSwitchItem.isChecked()) {
                    Log.d("DriverFragment", "seniorSwitch checked starting DestinationListFragment");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.driver_fragment_container, DestinationListFragment.newInstance()).commit();
                } else {
                    Log.d("DriverFragment", "seniorSwitch unchecked starting DriverFragment");
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.driver_fragment_container, DriverFragment.newInstance()).commit();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_SEARCHING, mSearchSwitch.isChecked());
        if (mRideUid != null) {
            outState.putString(ARG_RIDE_ID, mRideUid);
        } else if (mRide != null) {
            outState.putString(ARG_RIDE_ID, mRide.getmUid());
        }
    }


    public static DriverFragment newInstance() {
        Bundle args = new Bundle();

        DriverFragment fragment = new DriverFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
