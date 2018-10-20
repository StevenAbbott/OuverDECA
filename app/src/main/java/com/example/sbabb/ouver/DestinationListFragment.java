package com.example.sbabb.ouver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sbabb.ouver.Model.Destination;
import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.Passenger;
import com.example.sbabb.ouver.Model.Ride;
import com.example.sbabb.ouver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class DestinationListFragment extends Fragment {

    private static final String EXTRA_DEST_ID = "destId";

    private static final int REQUEST_DRIVER = 0;

    private Switch mSeniorSwitch;

    private DestinationLab mdl;
    private User mUser;


    public static DestinationListFragment newInstance() {
        Bundle args = new Bundle();

        DestinationListFragment fragment = new DestinationListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private RecyclerView mDestinationRecyclerView;
    private DestinationAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdl = DestinationLab.get(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination_list, container, false);

        mdl.getUserRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                if (!mUser.ismJunior()) { setHasOptionsMenu(true); }
                Log.d("User field updated.", "onDataChange:success");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User field update fail.", "onDataChange:failure");
            }
        });

        mDestinationRecyclerView = (RecyclerView) view.findViewById(R.id.destinations_recycler_view);
        mDestinationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            //
        }

        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_senior, menu);

        MenuItem seniorSwitchItem = menu.findItem(R.id.senior_switch_item);
        seniorSwitchItem.setActionView(R.layout.switch_layout);
        mSeniorSwitch = seniorSwitchItem
                .getActionView().findViewById(R.id.senior_switch);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.senior_switch_item:
                updateChildFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateChildFragment() {
        Fragment childFragment;
        Log.d("EverythingPager", "update child fragment called");
        if (mSeniorSwitch.isChecked()) {
            childFragment = DestinationListFragment.newInstance();
        } else {
            childFragment = DriverFragment.newInstance();
        }
        Log.d("EverythingPager", "fm about to start a transaction with child frag " + childFragment);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.everything_view_pager, childFragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //
    }

    private void updateUI() {
        Log.d("DestinationListFrag", "updateUI called");
        DestinationLab destinationLab = DestinationLab.get(getActivity());
        List<Destination> destinations = destinationLab.getmDestinationList();

        if (mAdapter == null) {
            mAdapter = new DestinationAdapter(destinations);
            mDestinationRecyclerView.setAdapter(mAdapter);
            Log.d("DestinationListFrag", "adapter was null so one was made");
        } else {
            Log.d("DestinationListFrag", "m adapter jst had destinations set " + destinations);
            mAdapter.setDestinations(destinations);
            mAdapter.notifyDataSetChanged();
        }

    }

    private class DestinationHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Destination mDestination;

        private TextView mNameTextView;
        private TextView mPriceTextView;

        public DestinationHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_destination, parent, false));
            itemView.setOnClickListener(this);
            Log.d("DestinationListFrag", "DestinationHolder just made");

            mNameTextView = (TextView) itemView.findViewById(R.id.dest_name_tv);
            mPriceTextView = (TextView) itemView.findViewById(R.id.dest_fare_tv);
        }

        public void bind(Destination destination) {
            Log.d("DestinationListFrag", "bind just called " + destination);
            mDestination = destination;
            mNameTextView.setText(mDestination.getmName());
            mPriceTextView.setText("" + mDestination.getmPrice());
        }

        @Override
        public void onClick(View view) {
            Passenger newPassenger = new Passenger(mUser.getmFname(), mUser.getmCell(), mDestination);
            Ride newRide = new Ride(newPassenger, mDestination);
            mUser.setmRide(newRide);
            Log.d("DestListFrag", "about to update user onClick user email " + mUser.getmEmail());
            mdl.updateUser(mUser);
            Log.d("DestinationListFrag", "ride id " + newRide.getmUid());
            mdl.updateRide(newRide);
            Intent i = PassengerActivity.newIntent(getContext(), newRide.getmUid());
            getActivity().finish();
            startActivity(i);
            /*Fragment f = PassengerFragment.newInstance(newRide.getmUid());
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.everything_view_pager, f)
                    .commit();*/
        }

    }

    private class DestinationAdapter extends RecyclerView.Adapter<DestinationHolder> {

        private List<Destination> mDestinations;

        public DestinationAdapter(List<Destination> destinations) {
            Log.d("DestinationListFrag", "new destinationAdapter just made");
            mDestinations = destinations;
        }

        @Override
        public DestinationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d("DestinationListFrag", "new destinationholder just made");
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new DestinationHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DestinationHolder holder, int position) {
            Log.d("DestinationListFrag", "onBindViewHolder called");
            Destination destination = mDestinations.get(position);
            holder.bind(destination);
        }

        @Override
        public int getItemCount() {
            Log.d("DestinationListFrag", "get item count " + mDestinations.size());
            return mDestinations.size();
        }

        public void setDestinations(List<Destination> destinations) {
            Log.d("DestinationListFrag", "setDestinations called");
            mDestinations = destinations;
        }
    }
}
