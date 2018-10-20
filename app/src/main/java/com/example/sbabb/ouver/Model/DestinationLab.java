package com.example.sbabb.ouver.Model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.sbabb.ouver.PassengerActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DestinationLab {

    private static DestinationLab sDestinationLab;
    private Context mContext;
    private List<Destination> mDestinationList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    public static DestinationLab get(Context context) {
        if (sDestinationLab == null) {
            sDestinationLab = new DestinationLab(context);

        }

        return sDestinationLab;
    }

    private DestinationLab(Context context) {
        initLocations();
        mContext = context.getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public void feed(String rootName, String dbReference) {
        mDatabase.getReference(rootName).child(dbReference);
    }

    public DatabaseReference getUserRef() {
        return mDatabase.getReference("userData").child(mAuth.getCurrentUser().getUid());
    }

    public DatabaseReference getRideRef(String uid) {
        Log.d("DestLab", "getRideRef ride id is " + uid);
        return mDatabase.getReference("pendingRides").child(uid);
    }

    public DatabaseReference getAllRidesRef() {
        return mDatabase.getReference("pendingRides");
    }

    public User newUser() {
        User newUser = new User(getKey());
        DatabaseReference userDataRef = mDatabase.getReference("userData");
        userDataRef.child(getKey()).setValue(newUser);
        return newUser;
    }

    public void updateUser(User user) {
        if (mAuth == null) {
            return;
        }
        DatabaseReference userDataRef = mDatabase.getReference("userData").child(getKey());
        userDataRef.setValue(user);
    }

    public void updateRide(Ride ride) {
        DatabaseReference userDataRef = mDatabase.getReference("pendingRides").child(ride.getmUid());
        userDataRef.setValue(ride);
    }

    public int getDestNum(Destination d) {
        return mDestinationList.indexOf(d);
    }

    private String getKey() {
        return mAuth.getCurrentUser().getUid();
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void initLocations() {
        mDestinationList = new ArrayList<Destination>();
        Destination juniorLot = new Destination();
        juniorLot.setmName("Junior Lot");
        juniorLot.setmPrice(2);
        Destination lowerTurnaround = new Destination();
        lowerTurnaround.setmName("Lower Turnaround");
        lowerTurnaround.setmPrice(2);
        Destination upperLot = new Destination();
        upperLot.setmName("Upper Turnaround");
        upperLot.setmPrice(4);
        Destination frontOffice = new Destination();
        frontOffice.setmName("Front Office");
        frontOffice.setmPrice(3.5);
        mDestinationList.add(lowerTurnaround);
        mDestinationList.add(juniorLot);
        mDestinationList.add(frontOffice);
        mDestinationList.add(upperLot);
    }

    public String getmKey() {
        return getKey();
    }

    public List<Destination> getmDestinationList() {
        return mDestinationList;
    }

}
