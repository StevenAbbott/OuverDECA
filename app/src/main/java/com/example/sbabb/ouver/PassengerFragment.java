package com.example.sbabb.ouver;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.Ride;
import com.example.sbabb.ouver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class PassengerFragment extends Fragment {

    //private static final String ARG_DEST_NUM = "destNum";
    private static final String ARG_RIDE_ID = "rideId";

    private DestinationLab mdl;
    private User mUser;
    private Ride mRide;
    private String mRideId;
    private boolean mRideCanceled;
    private View mV;
    private Context mContext;

    private FloatingActionButton mTipButton;
    private Button mCancelRideButton;
    private Button mCallDriverButton;
    private TextView mDestTextView;
    private TextView mDriverTextView;
    private TextView mFareTextView;
    private TextView mDriverCapTextView;
    private ConstraintLayout mSearchIndicatorLayout;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

        Log.d("PassengerFrag", "You have arrived!");
        mContext = getActivity().getApplicationContext();

        mdl = DestinationLab.get(getContext());
        mRideCanceled = false;
        mRideId = getArguments().getString(ARG_RIDE_ID);

        if (savedInstanceState != null) {
            mRideId = getArguments().getString(ARG_RIDE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_passenger, container, false);
        mV = v;

        mTipButton = (FloatingActionButton) v.findViewById(R.id.tip_button);
        mCancelRideButton = (Button) v.findViewById(R.id.cancel_ride_d_button);
        mCallDriverButton = (Button) v.findViewById(R.id.call_driver_button);
        mDestTextView = (TextView) v.findViewById(R.id.dest_tv);
        mDriverTextView = (TextView) v.findViewById(R.id.pass_tv);
        mFareTextView = (TextView) v.findViewById(R.id.fare_tv);
        mDriverCapTextView = (TextView) v.findViewById(R.id.pass_loc_tv);
        mSearchIndicatorLayout = (ConstraintLayout) v.findViewById(R.id.search_indicator_driver);

        Log.d("PassengerFrag", "Checking for that ride.");
        if (mRideId != null) {
            mdl.getRideRef(mRideId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mRide = dataSnapshot.getValue(Ride.class);
                    mdl.getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            checkForDriver();
                            Log.d("User field updated.", "onDataChange:success");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("User field update fail.", "onDataChange:failure");
                }
            });
        }

        mCancelRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdl.getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUser = dataSnapshot.getValue(User.class);
                        removeRide();
                        mRideId = null;
                        mRideCanceled = true;
                        if(getActivity() != null) {
                            Log.d("PassFrag", "act not null mCancelRide clicked so making pager from act " + getActivity());
                            Intent i = EverythingPagerActivity.newIntent(getActivity(), mUser.ismJunior(), 1);
                            startActivity(i);
                            Log.d("PassFrag", "made a new pager in mCancelRideClick and about to finish " + getActivity());
                            getActivity().finish();
                        }
                        Log.d("User field updated.", "onDataChange:success");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User field update fail.", "onDataChange:failure");
                    }
                });
            }
        });

        mCallDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.putExtra(Intent.EXTRA_PHONE_NUMBER, mRide.getmDriver().getmCell());
                startActivity(i);
            }
        });

        mTipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tip, null))
                        .setTitle("Tip Driver")
                        .setMessage("How much would you like to tip your driver?")
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        EditText tipET = getView().findViewById(R.id.tip_et);
                                        mRide.getmPassenger().setmTip(mRide.getmPassenger().getmTip() + Double.parseDouble(tipET.getText().toString()));
                                        mFareTextView.setText("" + (Double.parseDouble(mFareTextView.getText().toString()) + Double.parseDouble(tipET.getText().toString())));
                                    }
                                })
                        .create();
                mdl.updateRide(mRide);
            }
        });

        return v;
    }

    public void removeRide() {
        mdl.getRideRef(mRideId).removeValue();
    }

    public void checkForDriver() {
        if (mRide != null) {
            mDestTextView.setText(mRide.getmDestination().getmName());
            mFareTextView.setText("$ " + mRide.getmDestination().getmPrice());
            if (mRide.getmDriver() != null) {
                mSearchIndicatorLayout.setVisibility(View.GONE);
                mDriverCapTextView.setVisibility(View.VISIBLE);
                if (mRide.getmDriver().getmCell() != null) {
                    mCallDriverButton.setVisibility(View.VISIBLE);
                }
                mDriverTextView.setVisibility(View.VISIBLE);
                mTipButton.setVisibility(View.VISIBLE);
                mDriverTextView.setText(mRide.getmDriver().getmName() + " (" + mRide.getmDriver().getmRating() + "/5)");
                mUser.setmRide(mRide);
                if (mRide.getmPassenger().ismArrived()) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_rate, null))
                            .setTitle("You have arrived!")
                            .setMessage("Once again, for security reasons Ouver does not process payments at this time, but if we did you would be charged $" + mRide.calculateFare())
                            .setPositiveButton(android.R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {
                                            Toast.makeText(getContext(), "Charged " , Toast.LENGTH_LONG).show();
                                            Log.d("PassFrag", "arrived dialog posBut clicked so about to make pager from act " + getActivity());
                                            Intent i = EverythingPagerActivity.newIntent(getContext(), mUser.ismJunior(), 1);
                                            Log.d("PassFrag", "just made a pager and about to finish act " + getActivity());
                                            getActivity().finish();
                                            startActivity(i);
                                        }
                                    })
                            .create();
                }
                Log.d("PassFrag", "about to update user checkForDriver user email " + mUser.getmEmail());
                mdl.updateUser(mUser);
            } else {
                mSearchIndicatorLayout.setVisibility(View.VISIBLE);
                mDriverCapTextView.setVisibility(View.GONE);
                mCallDriverButton.setVisibility(View.GONE);
                mDriverTextView.setVisibility(View.GONE);
                mTipButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_RIDE_ID, mRideId);
        Log.d("PassFrag", "save instance state called");
    }

    public static PassengerFragment newInstance(String rideId) {
        Bundle args = new Bundle();
        args.putString(ARG_RIDE_ID, rideId);
        PassengerFragment fragment = new PassengerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if (!mRideCanceled) {
            Intent i = FindDriverService.newIntent(getContext(), mRideId);
            getActivity().startService(i);
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRideId != null) {
            removeRide();
        }
    }
}
