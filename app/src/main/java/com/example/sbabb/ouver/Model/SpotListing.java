package com.example.sbabb.ouver.Model;

import java.util.Date;

public class SpotListing {

    private int mSpotNum;
    private Date mStartDate;
    private Date mEndDate;
    private int mPrice;

    public SpotListing() {

    }

    public int getmSpotNum() {
        return mSpotNum;
    }

    public Date getmStartDate() {
        return mStartDate;
    }

    public Date getmEndDate() {
        return mEndDate;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmSpotNum(int mSpotNum) {
        this.mSpotNum = mSpotNum;
    }

    public void setmStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public void setmEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }
}
