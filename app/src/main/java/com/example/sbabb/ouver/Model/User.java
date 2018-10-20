package com.example.sbabb.ouver.Model;


import com.example.sbabb.ouver.Model.Car;
import com.example.sbabb.ouver.Model.Ride;
import com.stripe.android.model.Card;

public class User {

    private String mFname;
    private String mEmail;
    //private Uri mPhotoUrl;
    //private boolean mEmailVerified;
    private String mUid;
    private String mLname;
    private String mCell;
    private String mDob;
    private boolean mJunior;
    private Car mCar;
    private Card mPaymentCustomer;
    private Ride mRide;
    private int mDriverRating;
    private int mPassengerRating;

    public User() {}

    public User(String uid) {
        mFname = "";
        mEmail = "";
        mUid = uid;
        mLname = "";
        mCell = "";
        mDob = "";
        mDriverRating = 2;
        mPassengerRating = 3;
        mJunior = true;
        mCar = new Car();
        mRide = new Ride();
    }

    public String getmFname() {
        return mFname;
    }

    public String getmEmail() {
        return mEmail;
    }

    //public Uri getmPhotoUrl() { return mPhotoUrl; }

    //public boolean ismEmailVerified() { return mEmailVerified; }

    public String getmUid() {
        return mUid;
    }

    public void setmFname(String mDisplayName) {
        this.mFname = mDisplayName;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    //public void setmPhotoUrl(Uri mPhotoUrl) { this.mPhotoUrl = mPhotoUrl; }

    //public void setmEmailVerified(boolean mEmailVerified) { this.mEmailVerified = mEmailVerified; }

    public void setmUid(String mUid) { this.mUid = mUid; }

    public String getmLname() {
        return mLname;
    }

    public String getmCell() {
        return mCell;
    }

    public String getmDob() {
        return mDob;
    }

    public void setmLname(String mLname) {
        this.mLname = mLname;
    }

    public void setmCell(String mCell) {
        this.mCell = mCell;
    }

    public void setmDob(String mdob) {
        this.mDob = mdob;
    }

    public Card getmPaymentCustomer() {
        return mPaymentCustomer;
    }

    public void setmPaymentCustomer(Card paymentCustomer) {
        this.mPaymentCustomer = paymentCustomer;
    }

    public boolean ismJunior() {
        return mJunior;
    }

    public void setmJunior(boolean mJunior) {
        this.mJunior = mJunior;
    }

    public Car getmCar() {
        return mCar;
    }

    public void setmCar(Car mCar) {
        this.mCar = mCar;
    }

    public Ride getmRide() {
        return mRide;
    }

    public void setmRide(Ride mRide) {
        this.mRide = mRide;
    }

    public int getmDriverRating() {
        return mDriverRating;
    }

    public void setmDriverRating(int mDriverRating) {
        this.mDriverRating = mDriverRating;
    }

    public int getmPassengerRating() {
        return mPassengerRating;
    }

    public void setmPassengerRating(int mPassengerRating) {
        this.mPassengerRating = mPassengerRating;
    }
}
