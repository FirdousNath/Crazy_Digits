package com.example.firdous.crazydigits;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Homescreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    PrefManager pref;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG="see";
    private GoogleApiClient mGoogleApiClient;
    ProgressDialog dialog;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        pref= new PrefManager(this);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };
        final Button play=(Button)findViewById(R.id.play);
         play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                       startActivity(new Intent(Homescreen.this, SelectMode.class));
             }
         });
        Button exit=(Button)findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showCustomDialog();
            }
        });

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + Homescreen.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                finish();
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + Homescreen.this.getPackageName())));
                    finish();
                }
            }
        });
        ImageView menu =(ImageView)findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homescreen.this, OtherMenus.class));
            }
        });
        findViewById(R.id.leaderboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null)
                startActivity(new Intent(Homescreen.this, LeaderBoard.class));
                else signIn();
            }
        });
    }
    private void signIn() {
        if(mGoogleApiClient == null || !mGoogleApiClient.isConnected())
        {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // [END config_signin]

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dialog = ProgressDialog.show(Homescreen.this, "Please wait", "while we Sign In");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 10) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                Log.d(account.getId(),account.getIdToken());
                firebaseAuthWithGoogle(account);
            } else {
                dialog.dismiss();
                mGoogleApiClient.stopAutoManage(Homescreen.this);
                mGoogleApiClient.disconnect();
                Toast.makeText(Homescreen.this, "Google play services error", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            dialog.dismiss();
                            if (task.getException().toString().contains("com.google.firebase.FirebaseNetworkException")) {
                                Toast.makeText(Homescreen.this, "No Internet connection",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                Toast.makeText(Homescreen.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dialog.dismiss();
                            startActivity(new Intent(Homescreen.this, LeaderBoard.class));
                        }
                    }
                });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    void showCustomDialog()
    {
        final Dialog dialog = new Dialog(Homescreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmexitdialog);
        dialog.setCancelable(true);
        dialog.show();

        Button yes = (Button) dialog.findViewById(R.id.yes);
        Button no = (Button) dialog.findViewById(R.id.no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        showCustomDialog();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
