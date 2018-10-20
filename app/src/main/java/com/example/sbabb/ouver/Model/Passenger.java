package com.example.sbabb.ouver.Model;

import com.example.sbabb.ouver.Model.Destination;

public class Passenger {

    private String mName;
    private String mCell;
    private Destination mDestination;
    private boolean mArrived;
    private double mTip;

    public Passenger() {

    }

    public Passenger(String name, String cell, Destination destination) {
        mName = name;
        mCell = cell;
        mDestination = destination;
    }

    public String getmName() {
        return mName;
    }

    public String getmCell() {
        return mCell;
    }

    public Destination getmDestination() {
        return mDestination;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmCell(String mCell) {
        this.mCell = mCell;
    }

    public void setmDestination(Destination mDestination) {
        this.mDestination = mDestination;
    }

    public boolean ismArrived() {
        return mArrived;
    }

    public void setmArrived(boolean mArrivedl) {
        this.mArrived = mArrivedl;
    }

    public double getmTip() {
        return mTip;
    }

    public void setmTip(double mTip) {
        this.mTip = mTip;
    }
}
