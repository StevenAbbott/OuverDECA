package com.example.sbabb.ouver.Model;


import com.example.sbabb.ouver.Model.Car;
import com.example.sbabb.ouver.Model.Destination;

public class Driver {

    private String mName;
    private String mCell;
    private Destination mZone;
    private int mRating;
    private Car mCar;

    public Driver() {

    }

    public Driver(User u) {
        mName = u.getmFname();
        mCell = u.getmCell();
        mRating = u.getmDriverRating();
        mCar = u.getmCar();
    }

    public String getmName() {
        return mName;
    }

    public String getmCell() {
        return mCell;
    }

    public Destination getmZone() {
        return mZone;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmCell(String mCell) {
        this.mCell = mCell;
    }

    public void setmZone(Destination mZone) {
        this.mZone = mZone;
    }

    public int getmRating() {
        if (mRating == 0) {
            return 4;
        }
        return mRating;
    }

    public Car getmCar() {
        return mCar;
    }

    public void setmRating(int mRating) {
        this.mRating = mRating;
    }

    public void setmCar(Car mCar) {
        this.mCar = mCar;
    }
}
