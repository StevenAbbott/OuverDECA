package com.example.sbabb.ouver.Model;

public class Car {
    private String mMake;
    private String mModel;
    private int mModelYear;
    private String mColor;
    private String mPlateNum;
    private int mSeniorSpot;

    public Car() {
        mMake = "";
        mModel = "";
        mModelYear = 1850;
        mColor = "neon magenta";
        mPlateNum = "";
        mSeniorSpot = 420;
    }

    public String getmMake() {
        return mMake;
    }

    public String getmModel() {
        return mModel;
    }

    public int getmModelYear() {
        return mModelYear;
    }

    public String getmColor() {
        return mColor;
    }

    public String getmPlateNum() {
        return mPlateNum;
    }

    public void setmMake(String mMake) {
        this.mMake = mMake;
    }

    public void setmModel(String mModel) {
        this.mModel = mModel;
    }

    public void setmModelYear(int mModelYear) {
        this.mModelYear = mModelYear;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }

    public void setmPlateNum(String mPlateNum) {
        this.mPlateNum = mPlateNum;
    }

    public int getmSeniorSpot() {
        return mSeniorSpot;
    }

    public void setmSeniorSpot(int mSeniorSpot) {
        this.mSeniorSpot = mSeniorSpot;
    }
}
