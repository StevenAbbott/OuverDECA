package com.example.sbabb.ouver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sbabb.ouver.Model.DestinationLab;
import com.example.sbabb.ouver.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SingInActivity extends AppCompatActivity {

    //public static final String TAG = LoadingActivity.class.getSimpleName();

    private DestinationLab mdl;

    private Button mSignInButton;
    private Button mCreateAccountButton;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private TextView mErrorTextView;
    private ProgressBar mSingInProgressBar;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mdl = DestinationLab.get(getApplicationContext());

        mEmailEditText = (EditText) findViewById(R.id.email_text_field);
        mErrorTextView = (TextView) findViewById(R.id.error_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_text_field);
        mSingInProgressBar = (ProgressBar) findViewById(R.id.sign_in_progress);


        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if (password.length() < 6) {
                    mErrorTextView.setText("The password must be at least 6 characters in length.");
                } else {
                    mSingInProgressBar.setVisibility(View.VISIBLE);
                    mdl.getmAuth().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SingInActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Successful Login", "signInWithEmail:success");
                                        Log.d("HoRP DoRP", "" + mdl.getmKey());
                                        Log.d("HONK DONK", "" + mUser);
                                        mdl.getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                mUser = dataSnapshot.getValue(User.class);
                                                Log.d("HERP DERP", "" + mUser);
                                                Log.d("SignInAct", "sign in button pressed so about to make pager from SingInAct");
                                                Intent i = EverythingPagerActivity.newIntent(getApplicationContext(), mUser.ismJunior(), 1); // null
                                                startActivity(i);
                                                finish();
                                                Log.d("User field updated.", "onDataChange:success");
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                mSingInProgressBar.setVisibility(View.GONE);
                                                Log.d("User field update fail.", "onDataChange:failure");
                                            }
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Unsuccessful login", "signInWithEmail:failure", task.getException());
                                        mSingInProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(SingInActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        mCreateAccountButton = (Button) findViewById(R.id.create_account_button);
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AccountInfoActivity.newIntent(getApplicationContext(), true);
                startActivity(intent);
                finish();

            }
        });
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SingInActivity.class);
        return intent;
    }
}
