package com.example.sbabb.ouver;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.Ride;
import com.example.sbabb.ouver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class FindDriverService extends IntentService {

    private static final String TAG = "FindDriverService";
    private static final String EXTRA_RIDE_ID = "rideId";

    private static final long POLL_INTERVAL_MS = TimeUnit.SECONDS.toMillis(4);
    private DestinationLab mdl;
    private String mRideId;
    private Ride mRide;
    private User mUser;

    public static Intent newIntent(Context context, String uid) {
        Intent i = new Intent(context, FindDriverService.class);
        i.putExtra(EXTRA_RIDE_ID, uid);
        return i;
    }

    public static boolean isServiceAlarmOn(Context context, String uid) {
        Intent i = FindDriverService.newIntent(context, uid);
        PendingIntent pi = PendingIntent
                .getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public FindDriverService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Recieved an intent: " + intent);

        if (!isNetworkAvailableAndConnected()) {
            return;
        }

        mRideId = intent.getStringExtra(EXTRA_RIDE_ID);
        mdl = DestinationLab.get(getApplicationContext());

        mdl.getRideRef(mRideId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRide = dataSnapshot.getValue(Ride.class);
                if (mRide.getmDriver() != null) {
                    mdl.getUserRef().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mUser = dataSnapshot.getValue(User.class);
                            Resources resources = getResources();
                            Intent i = PassengerActivity.newIntent(getApplicationContext(), mRideId);
                            PendingIntent pi = PendingIntent
                                    .getActivity(getApplicationContext(), 1, i, 0);

                            Notification notification;
                            if (Build.VERSION.SDK_INT >= 26) {
                                String channelId = initNotificationChannels();
                                notification = new NotificationCompat.Builder(getApplicationContext(), channelId)
                                        .setTicker(resources.getString(R.string.found_driver_text))
                                        .setSmallIcon(R.drawable.ic_driver_found)
                                        .setContentTitle(resources.getString(R.string.found_driver_title))
                                        .setContentText(resources.getString(R.string.found_driver_text))
                                        .setContentIntent(pi)
                                        .setAutoCancel(true)
                                        .build();
                            } else {
                                notification = new NotificationCompat.Builder(getApplicationContext())
                                        .setTicker(resources.getString(R.string.found_driver_text))
                                        .setSmallIcon(R.drawable.ic_driver_found)
                                        .setContentTitle(resources.getString(R.string.found_driver_title))
                                        .setContentText(resources.getString(R.string.found_driver_text))
                                        .setContentIntent(pi)
                                        .setAutoCancel(true)
                                        .build();
                            }

                            NotificationManagerCompat notificationManager =
                                    NotificationManagerCompat.from(getApplicationContext());
                            notificationManager.notify(0, notification);
                            Log.d("User field updated.", "onDataChange:success");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("User field update fail.", "onDataChange:failure");
                        }
                    });
                }
                Log.d("User field updated.", "onDataChange:success");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User field update fail.", "onDataChange:failure");
            }
        });
    }

    @TargetApi(26)
    private String initNotificationChannels() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelIdOne = "com.example.sbabb.ouver.channel1";
        CharSequence nameOne = "Ouver";
        String descriptionOne = "yo notifications channel fo ouver";
        int importanceOne = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channelOne = new NotificationChannel(channelIdOne, nameOne, importanceOne);
        channelOne.setDescription(descriptionOne);
        channelOne.enableLights(true);
        channelOne.setLightColor(Color.GREEN);
        channelOne.enableVibration(false);
        mNotificationManager.createNotificationChannel(channelOne);

        return channelIdOne;
    }

    public static void setServiceAlarm(Context context, boolean isOn, String uid) {
        Intent i = FindDriverService.newIntent(context, uid);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }



    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();

        return isNetworkConnected;
    }
}
