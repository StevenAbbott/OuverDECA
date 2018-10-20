package com.example.sbabb.ouver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sbabb.ouver.Model.Car;
import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CarInfoFragment extends Fragment {

    public static final String ARG_FIRST_TIME = "first_time";


    private static final String TAG = "CarInfoFragment";

    private TextView mColorTextView;
    private TextView mLpnTextView;
    private TextView mMakeTextView;
    private TextView mModelTextView;
    private TextView mModelYearTextView;
    private TextView mSpotNumTextView;
    private EditText mColorEditText;
    private EditText mLpnEditText;
    private EditText mMakeEditText;
    private EditText mModelEditText;
    private EditText mModelYearEditText;
    private EditText mSpotNumEditText;
    private Button mDoneButton;

    private User mUser;
    private DestinationLab mdl;
    private Car mCar;

    private boolean mFirstTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdl = DestinationLab.get(getContext());

    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_collect_car_info, container, false);

        mColorTextView = (TextView) v.findViewById(R.id.color_tv);
        mLpnTextView = (TextView) v.findViewById(R.id.lpn_tv);
        mMakeTextView = (TextView) v.findViewById(R.id.make_tv);
        mModelTextView = (TextView) v.findViewById(R.id.model_tv);
        mModelYearTextView = (TextView) v.findViewById(R.id.year_tv);
        mSpotNumTextView = (TextView) v.findViewById(R.id.spot_num_tv);
        mColorEditText = (EditText) v.findViewById(R.id.color_et);
        mLpnEditText = (EditText) v.findViewById(R.id.lpn_et);
        mMakeEditText = (EditText) v.findViewById(R.id.make_et);
        mModelEditText = (EditText) v.findViewById(R.id.model_et);
        mModelYearEditText = (EditText) v.findViewById(R.id.model_year_et);
        mSpotNumEditText = (EditText) v.findViewById(R.id.spot_num_et);
        mDoneButton = (Button) v.findViewById(R.id.done_button);

        if (savedInstanceState != null) {
            mFirstTime = savedInstanceState.getBoolean(ARG_FIRST_TIME);
        }

        if (mFirstTime) {
            mCar = new Car();
            mColorTextView.setVisibility(View.GONE);
            mMakeTextView.setVisibility(View.GONE);
            mModelTextView.setVisibility(View.GONE);
            mLpnTextView.setVisibility(View.GONE);
            mSpotNumTextView.setVisibility(View.GONE);
            mModelYearTextView.setVisibility(View.GONE);
        } else {
            mdl.getUserRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser = dataSnapshot.getValue(User.class);
                    mCar = mUser.getmCar();
                    if (!mFirstTime) {
                        mColorEditText.setText(mCar.getmColor());
                        mMakeEditText.setText(mCar.getmMake());
                        mModelEditText.setText(mCar.getmModel());
                        mLpnEditText.setText(mCar.getmPlateNum());
                        mSpotNumEditText.setText("" + mCar.getmSeniorSpot());
                        mModelYearEditText.setText("" + mCar.getmModelYear());
                    }
                    Log.d("User field updated.", "onDataChange:success");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("User field update fail.", "onDataChange:failure");
                }
            });
        }

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCar();
                Intent i;
                if(mFirstTime) {
                    i = EverythingPagerActivity.newIntent(getContext(), false, 1);
                } else {
                    i = EverythingPagerActivity.newIntent(getContext(), false, 0);
                }
                startActivity(i);
                getActivity().finish();
            }
        });



        return v;
    }

    @Override
    public void onPause() {
        super.onPause();

        updateCar();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        updateCar();
    }

    public void updateCar() {
        if (mCar == null) {
            mCar = new Car();
        }
        mCar.setmColor(mColorEditText.getText().toString());
        mCar.setmMake(mMakeEditText.getText().toString());
        mCar.setmModel(mModelEditText.getText().toString());
        mCar.setmModelYear(Integer.parseInt(mModelYearEditText.getText().toString()));
        mCar.setmPlateNum(mLpnEditText.getText().toString());
        mCar.setmSeniorSpot(Integer.parseInt(mSpotNumEditText.getText().toString()));
        mUser.setmCar(mCar);
        mdl.updateUser(mUser);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putBoolean(ARG_FIRST_TIME, mFirstTime);
    }

    public static CarInfoFragment newInstance(boolean firstTime) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIRST_TIME, firstTime);

        CarInfoFragment fragment = new CarInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
