package com.example.sbabb.ouver.Model;

import java.util.UUID;

public class Ride {

    private Passenger mPassenger;
    private String mUid;
    private Driver mDriver;
    private Destination mDestination;

    public Ride() {
        new Ride(null, null);
    }

    public Ride(Passenger passenger, Destination destination) {
        mUid = UUID.randomUUID().toString();
        mPassenger = passenger;
        mDestination = destination;
    }

    public double calculateFare() {
        return mDestination.getmPrice() + mPassenger.getmTip();
    }

    public Passenger getmPassenger() {
        return mPassenger;
    }

    public Driver getmDriver() {
        return mDriver;
    }

    public Destination getmDestination() {
        return mDestination;
    }

    public void setmPassenger(Passenger mPassenger) {
        this.mPassenger = mPassenger;
    }

    public void setmDriver(Driver mDriver) {
        this.mDriver = mDriver;
    }

    public void setmDestination(Destination mDestination) {
        this.mDestination = mDestination;
    }

    public String getmUid() {
        return mUid;
    }
}
