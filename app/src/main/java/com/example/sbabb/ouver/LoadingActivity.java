package com.example.sbabb.ouver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoadingActivity extends AppCompatActivity {

    private DestinationLab mdl;

    private TextView mAppStartTextView;
    private TextView mFailedToLoadTextView;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mAppStartTextView = (TextView) findViewById(R.id.user_greeting_text);
        mFailedToLoadTextView = (TextView) findViewById(R.id.failed_to_load_tv);
        mdl = DestinationLab.get(getApplicationContext());

        Thread thread = new Thread(runnable);
        thread.start();

        mFailedToLoadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdl.getmAuth().signOut();
                goToNextAct();
            }
        });

        Log.d("loadingact", "current user " + mdl.getmAuth().getCurrentUser());
        if (mdl.getmAuth() != null && mdl.getmAuth().getCurrentUser() != null) {
            Log.d("Loading Activity", "The user IS logged in.");
            mdl.getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mUser = dataSnapshot.getValue(User.class);
                    if (mUser != null && mdl.getmAuth() != null && mdl.getmAuth().getCurrentUser() != null) {
                        Log.d("dod LoadingAct", "found user " + mdl.getmAuth().getCurrentUser().getUid());
                        goToNextAct();
                        Log.d("User field updated.", "onDataChange:success");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("User field update fail.", "onDataChange:failure");
                    goToNextAct();
                }
            });
        } else {
            goToNextAct();
        }

        /*mdl.feed("pendypups", "pendingRides");
        mdl.feed("spotedlists", "spotListings");
        mdl.feed("usey", "userData");
        mdl.feed("revyews", "reviews");*/

        //mdl.getmAuth().signOut();
    }


    public void goToNextAct() {
        Intent i;
        if (mUser == null) {
            i = SingInActivity.newIntent(getApplicationContext());
        } else {
            Log.d("LoadingAct", "goToNextAcc called and user not null so making pager from LoadingAct junior set to " + mUser.ismJunior());
            i = EverythingPagerActivity.newIntent(getApplicationContext(), mUser.ismJunior(), 1);
        }
        startActivity(i);
        finish();
    }

    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                            mFailedToLoadTextView.setVisibility(View.VISIBLE);
                      }
                  }
                );
            }
        }
    };
}
