package com.example.sbabb.ouver;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.Stripe;

import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

public class PaymentInfoFragment extends DialogFragment {

    private DestinationLab mdl;
    private User mUser;
    private Stripe mStripe;

    private Card mCard;
    private boolean mFirstTime;
    private boolean mJunior;

    public static final String ARG_FIRST_TIME = "firstTime";
    public static final String ARG_IS_JUNIOR = "isJunior";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdl = DestinationLab.get(getContext());

        mStripe = new Stripe(getContext(), "pk_test_aXyBTqfj78sS5WFCtdAisK1j");
        mFirstTime = getArguments().getBoolean(ARG_FIRST_TIME);
        mJunior = getArguments().getBoolean(ARG_IS_JUNIOR);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_payment_info, null);

        final CardInputWidget mCardInputWidget = (CardInputWidget) v.findViewById(R.id.card_input_widget);


        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.payment_info_title)
                .setNegativeButton(R.string.payment_info_neg,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_CANCELED);
                            }
                        })
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateUser();
                                if (mFirstTime) {
                                    sendResult(Activity.RESULT_OK);
                                }
                            }
                        })
                .create();

        if(mFirstTime) {
            if (mJunior) {
                dialog.setMessage("Congrats on the new account! Let's set up some payment info.");
            } else {
                dialog.setMessage("Congrats on the new account. Enter a DEBIT card to get paid!");
            }
        } else {
            if (mJunior) {
                dialog.setMessage("Enter your card information here.");
            } else {
                dialog.setMessage("Enter a DEBIT card to get paid!");
            }
        }

        // TODO: Remove when you add support for payment integration.
        if (mFirstTime) {
            dialog.setMessage("Congrats on the new account! For security reasons Ouver does not process payments at this time, but if we did this is where you would enter your payment information.");
        } else {
            dialog.setMessage("For security reasons Ouver does not process payments at this time, but if we did this is where you would enter your payment information.");
        }

        return dialog;


        /*if (cardToSave != null) {

            SourceParams cardSourceParams = SourceParams.createCardParams(card);
// The asynchronous way to do it. Call this method on the main thread.
            mStripe.createSource(
                    cardSourceParams,
                    new SourceCallback() {
                        @Override
                        public void onSuccess(Source source) {
                            // Store the source somewhere, use it, etc
                        }
                        @Override
                        public void onError(Exception error) {
                            // Tell the user that something went wrong
                        }
                    });

            // ask my server to make a new Customer with the source then update the database
        }*/

    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();

        Log.d("PaymentInfFrag/Dialog", "about to call onActRes of target frag " + getTargetFragment());
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    public void updateUser() {
        mdl.getUserRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUser = dataSnapshot.getValue(User.class);
                mUser.setmPaymentCustomer(mCard); // null
                mdl.updateUser(mUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public static PaymentInfoFragment newInstance(boolean firstTime, boolean isJunior) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIRST_TIME, firstTime);
        args.putBoolean(ARG_IS_JUNIOR, isJunior);

        PaymentInfoFragment fragment = new PaymentInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }
}
