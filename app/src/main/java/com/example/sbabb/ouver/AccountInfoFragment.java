package com.example.sbabb.ouver;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AccountInfoFragment extends Fragment implements TextWatcher {

    public static final String ARG_FIRST_TIME = "firstTime";
    private static final String DIALOG_PAYMENT = "DialogPayment";

    private static final int REQUEST_PAYMENT = 0;

    private TextView mEmailTextView;
    private TextView mPasswordTextView;
    private TextView mCellTextView;
    private TextView mFnameTextView;
    private TextView mLnameTextView;
    private TextView mdobTextView;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mPassword2EditText;
    private EditText mCellEditText;
    private EditText mFnameEditText;
    private EditText mLnameEditText;
    private EditText mdobEditText;
    private Button mCompleteButton;
    private Button mPaymentInfoButton;
    private Button mSingOutButton;
    private Button mResetPasswordButton;
    private Button mEditCarInfoButton;
    private ProgressBar mPasswordProgress;
    private ToggleButton mJuniorToggleButton;

    private boolean mFirstTime;
    private boolean mJunior;
    private boolean mSignOut;
    private View mV;

    private User mUser;
    private DestinationLab mdl;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mSignOut = false;

        mFirstTime = getArguments().getBoolean(ARG_FIRST_TIME);

        mdl = DestinationLab.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_account_info, container, false);
        mV = v;

        mEmailTextView = (TextView) v.findViewById(R.id.email_tv);
        mPasswordTextView = (TextView) v.findViewById(R.id.password_tv);
        mCellTextView = (TextView) v.findViewById(R.id.cell_tv);
        mFnameTextView = (TextView) v.findViewById(R.id.first_name_tv);
        mLnameTextView = (TextView) v.findViewById(R.id.last_name_tv);
        mdobTextView = (TextView) v.findViewById(R.id.dob_tv);
        mEmailEditText = (EditText) v.findViewById(R.id.acc_email_et);
        mPasswordEditText = (EditText) v.findViewById(R.id.acc_password_et);
        mPassword2EditText = (EditText) v.findViewById(R.id.acc_password_et2);
        mCellEditText = (EditText) v.findViewById(R.id.acc_cell_et);
        mFnameEditText = (EditText) v.findViewById(R.id.acc_fname_et);
        mLnameEditText = (EditText) v.findViewById(R.id.acc_lname_et);
        mdobEditText = (EditText) v.findViewById(R.id.acc_dob_et);
        mCompleteButton = (Button) v.findViewById(R.id.confirm_button);
        mPaymentInfoButton = (Button) v.findViewById(R.id.payment_info_button);
        mSingOutButton = (Button) v.findViewById(R.id.sign_out_button);
        mResetPasswordButton = (Button) v.findViewById(R.id.reset_password_button);
        mEditCarInfoButton = (Button) v.findViewById(R.id.edit_car_info_button);
        mJuniorToggleButton = (ToggleButton) v.findViewById(R.id.junior_toggle);
        mPasswordProgress = (ProgressBar) v.findViewById(R.id.password_progress);


        if (savedInstanceState != null) {
            mFirstTime = savedInstanceState.getBoolean(ARG_FIRST_TIME);
            Log.d("AccInfFrag", "arg for firstTime just taken out and equals" + mFirstTime);
        }

        Log.d("mFirstTime is ", "" + mFirstTime);
        if (mFirstTime) {
            mEmailTextView.setVisibility(View.GONE);
            mPasswordTextView.setVisibility(View.GONE);
            mCellTextView.setVisibility(View.GONE);
            mFnameTextView.setVisibility(View.GONE);
            mLnameTextView.setVisibility(View.GONE);
            mdobTextView.setVisibility(View.GONE);
            mPaymentInfoButton.setVisibility(View.GONE);
            mSingOutButton.setVisibility(View.GONE);
            mResetPasswordButton.setVisibility(View.GONE);
            mEditCarInfoButton.setVisibility(View.GONE);
        } else {
            mEmailEditText.setVisibility(View.GONE);
            mEmailTextView.setVisibility(View.GONE);
            mPasswordTextView.setVisibility(View.GONE);
            mPassword2EditText.setVisibility(View.GONE);
            mPasswordEditText.setVisibility(View.GONE);
            mCompleteButton.setVisibility(View.GONE);

            if (mdl.getmAuth() != null && mdl.getmAuth().getCurrentUser() != null) {
                mdl.getUserRef().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUser = dataSnapshot.getValue(User.class);
                        mJunior = mUser.ismJunior();
                        if (!mJunior) {
                            mEditCarInfoButton.setVisibility(View.VISIBLE);
                            mJuniorToggleButton.setChecked(!mJunior);
                        } else {
                            if (getActivity() != null) {
                                getActivity().invalidateOptionsMenu();
                            }
                        }
                        Log.d("AccInfAct", "pop fields about to be called with user email " + mUser.getmEmail());
                        populateFields();
                        Log.d("User field updated.", "onDataChange:success");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User field update fail.", "onDataChange:failure");
                    }
                });
            }
        }

        mJuniorToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AcInfFrag", "about to update user JuniorToggleClick user email " + mUser.getmEmail());
                //updateUser();
                if (!mFirstTime) {
                    Log.d("AccInfFrag", "parent act " + getActivity());
                    new AlertDialog.Builder(getContext())
                            .setTitle("Toggle Junior/Senior?")
                            .setMessage("You must sign back in to confirm your change in account type")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    signOut();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mJuniorToggleButton.setChecked(!mJuniorToggleButton.isChecked());
                                }
                            })
                            .show();
                }
            }
        });

        mEditCarInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AcInfFrag", "about to update user editCarInfoClick user email " + mUser.getmEmail());
                updateUser();
                Intent i = CarInfoActivity.newIntent(getContext(), mFirstTime);
                startActivity(i);
                Log.d("AccInfFrag editcar", "about to finish " + getActivity());
                getActivity().finish();
            }
        });

        mPaymentInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AcInfFrag", "about to update user paymentInfButClick user email " + mUser.getmEmail());
                updateUser();
                startPaymentDialog();
            }
        });

        mResetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdl.getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUser = dataSnapshot.getValue(User.class);
                        Log.d("AccountInfoFrag", "" + mUser.getmEmail());
                        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                .setView(mV)
                                .setTitle(R.string.confirm_password_reset_title)
                                .setMessage(R.string.confirm_password_reset)
                                .setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                .setPositiveButton(android.R.string.ok,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                Log.d("AcInfFrag", "about to update user passResetButClick user email " + mUser.getmEmail());
                                                updateUser();
                                                mdl.getmAuth().sendPasswordResetEmail(mUser.getmEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            mdl.getmAuth().signOut();
                                                            Intent i = SingInActivity.newIntent(getContext());
                                                            startActivity(i);
                                                            dialog.dismiss();
                                                            getActivity().finish();
                                                            Toast.makeText(getContext(),"Reset password email sent, check your inbox.",Toast.LENGTH_LONG).show();
                                                        }
                                                        else{
                                                            Toast.makeText(getContext(),task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        })
                                .create();
                        Log.d("User field updated.", "onDataChange:success");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User field update fail.", "onDataChange:failure");
                    }
                });
            }
        });

        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String password = "Goapple09"; //mPasswordEditText.getText().toString();
                String password2 = "Goapple09"; //mPassword2EditText.getText().toString();
                final String email = mEmailEditText.getText().toString();
                Log.d("goink", "" + mdobEditText);
                String dob = mdobEditText.getText().toString();
                mJunior = !mJuniorToggleButton.isChecked();

                if (!password.equals(password2)) {
                    mPasswordEditText.setTextColor(Color.RED);
                    mPassword2EditText.setTextColor(Color.RED);
                    Toast.makeText(getContext(), "Passwords don't match.",
                            Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    mPasswordEditText.setTextColor(Color.RED);
                    mPassword2EditText.setTextColor(Color.RED);
                    Toast.makeText(getContext(), "Your Password must be at least 6 characters in length.",
                            Toast.LENGTH_LONG).show();
                /*} else if (Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(dob.substring(dob.length() - 4)) < 17) {
                    Toast.makeText(getContext(), "You should be at least 17 to have a senior account bud.",
                            Toast.LENGTH_LONG).show();*/
                } else if (mCellEditText.getText().toString() == null || PhoneNumberUtils.isGlobalPhoneNumber(mCellEditText.getText().toString())) {
                    Toast.makeText(getContext(), "Please enter a valid phone number.",
                            Toast.LENGTH_LONG).show();
                } else {
                    mdl.getmAuth().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        mdl.getmAuth().signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // Sign in success, update UI with the signed-in user's information
                                                            mUser = mdl.newUser();
                                                            Log.d("AcInfFrag", "about to update user completeButClick and signInAsyncComplete user email " + mUser.getmEmail());
                                                            updateUser();
                                                            startPaymentDialog();
                                                            Log.d("Successful Login", "signInWithEmail:success");
                                                        } else {
                                                            // If sign in fails, display a message to the user.
                                                            Log.w("Unsuccessful login", "signInWithEmail:failure", task.getException());
                                                            Toast.makeText(getActivity(), "Authentication failed.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        Log.d("new user created!", "createUserWithEmail:success");
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("failed to create user", "createUserWithEmail:failure", task.getException());
                                    }
                                }
                            });
                }
            }
        });

        mSingOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignOut = true;
                Log.d("AcInfFrag", "about to update user signOutButClick user email " + mUser.getmEmail());
                signOut();
            }
        });

        mFnameEditText.addTextChangedListener(this);
        mEmailEditText.addTextChangedListener(this);
        mFnameEditText.addTextChangedListener(this);
        mLnameEditText.addTextChangedListener(this);
        mdobEditText.addTextChangedListener(this);
        mCellEditText.addTextChangedListener(this);

        return v;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateUser();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private void signOut() {
        updateUser();
        Intent i = SingInActivity.newIntent(getContext());
        startActivity(i);
        getActivity().finish();
    }


    public void populateFields() {
        mdobEditText.setText(mUser.getmDob());
        mFnameEditText.setText(mUser.getmFname());
        mLnameEditText.setText(mUser.getmLname());
        mEmailEditText.setText(mUser.getmEmail());
        mCellEditText.setText(mUser.getmCell());
    }

    public void updateUser() {
        if (mUser == null) { mUser = new User(); }
        mUser.setmUid(mdl.getmKey());
        mUser.setmEmail(mEmailEditText.getText().toString());
        mUser.setmCell(mCellEditText.getText().toString());
        mUser.setmDob(mdobEditText.getText().toString());
        mUser.setmFname(mFnameEditText.getText().toString());
        mUser.setmLname(mLnameEditText.getText().toString());
        mUser.setmJunior(!mJuniorToggleButton.isChecked());
        mdl.updateUser(mUser);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mUser != null) {
            Log.d("AcInfFrag", "about to update user savedInstState user email " + mUser.getmEmail());
            updateUser();
        }
        outState.putBoolean(ARG_FIRST_TIME, mFirstTime);
        Log.d("AccountInfoFrag", "save instance state called");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mFirstTime && mUser != null) {
            Log.d("AcInfFrag", "about to update user onPause user email " + mUser.getmEmail());
            mdl.updateUser(mUser);
        }
        if (mSignOut) { mdl.getmAuth().signOut(); }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if (!mSignOut) {
            if (mUser == null) { mUser = new User(); }
            mdl.updateUser(mUser);
        }*/
    }

    public static AccountInfoFragment newInstance(boolean firstTime) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_FIRST_TIME, firstTime);

        AccountInfoFragment fragment = new AccountInfoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    public void startPaymentDialog() {
        FragmentManager manager = getFragmentManager();
        PaymentInfoFragment dialog = PaymentInfoFragment.newInstance(mFirstTime, mUser.ismJunior());
        dialog.setTargetFragment(AccountInfoFragment.this, REQUEST_PAYMENT);
        dialog.show(manager, DIALOG_PAYMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Account info frag", "onActivityResult called");
        if (resultCode != Activity.RESULT_OK && resultCode != Activity.RESULT_CANCELED) {
            return;
        }

        Log.d("AcInfFrag", "about to update user OnActRes user email " + mUser.getmEmail());
        updateUser();

        if (requestCode == REQUEST_PAYMENT) {
            if (mFirstTime) {
                Intent i;
                if (mJunior) {
                    Log.d("AccInfFrag", "starting a pager onActRes from Act " + getActivity() );
                    i = EverythingPagerActivity.newIntent(getContext(), mUser.ismJunior(), 1);
                } else {
                    i = CarInfoActivity.newIntent(getActivity(), mFirstTime);
                }
                startActivity(i);
                Log.d("AccInfFrag", "request payment code returned onActRes so finishing " + getActivity());
                getActivity().finish();
            }
        }
    }
}
