package com.example.sbabb.ouver.Model;

import android.support.constraint.solver.widgets.Rectangle;

public class Destination {

    public Rectangle mCoords;
    public String mName;
    public double mPrice;

    public Destination() {

    }

    public Rectangle getmCoords() {
        return mCoords;
    }

    public String getmName() {
        return mName;
    }

    public void setmCoords(Rectangle mCoords) {
        this.mCoords = mCoords;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }
}
