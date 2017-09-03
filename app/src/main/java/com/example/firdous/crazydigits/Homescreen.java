package com.example.firdous.crazydigits;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Homescreen extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnClickListener {
    public static int length = 0;
    PrefManager pref;
    String TAG="see";
    Dialog dialog;
    FirebaseUser user;
    PlaySound ps = new PlaySound();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        pref = new PrefManager(this);
//        Log.d("total",""+pref.getTotal());
        if (!pref.isFirstTime()) {
            pref.setTOTAL(0);
            pref.setIsRated(false);
        } else {
            if (pref.getTotal() == 0)
                pref.setTOTAL(1);
            else {

                if (!pref.getIsRated()) {
                    pref.setTOTAL(pref.getTotal() + 1);
                    if ((pref.getTotal() % 6) == 0) {
                        pref.setTOTAL(0);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                this);

                        // set title
                        alertDialogBuilder.setMessage("Kindly Rate us 5 star");

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(true)
                                .setPositiveButton("kindly rate us", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Uri uri = Uri.parse("market://details?id=" + Homescreen.this.getPackageName());
                                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                        // To count with Play market backstack, After pressing back button,
                                        // to taken back to our application, we need to add following flags to intent.
                                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                        try {
                                            startActivity(goToMarket);
                                        } catch (ActivityNotFoundException e) {
                                            startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse("http://play.google.com/store/apps/details?id=" + Homescreen.this.getPackageName())));
                                        }
                                    }
                                })
                                .setNegativeButton("never", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        pref.setIsRated(true);
                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();
                    }
                }
            }
        }
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.exit).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);
        findViewById(R.id.menu).setOnClickListener(this);
        findViewById(R.id.leaderboard).setOnClickListener(this);
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
        try {
            ps.createSoundBackground(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ps.playSoundBackground();
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
        dialog = new Dialog(Homescreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmexitdialog);
        dialog.setCancelable(true);
        dialog.show();

        dialog.findViewById(R.id.yes).setOnClickListener(this);
        dialog.findViewById(R.id.no).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        play(1);
        showCustomDialog();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.play:
                play(0);
                startActivity(new Intent(Homescreen.this, SelectMode.class));
                break;
            case R.id.exit:
                play(1);
                showCustomDialog();
                break;
            case R.id.share:
                play(0);
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
                break;

            case R.id.menu:
                play(0);
                startActivity(new Intent(Homescreen.this, OtherMenus.class));
                break;

            case R.id.yes:
                play(0);
                dialog.dismiss();
                finish();
                break;

            case R.id.no:
                play(0);
                dialog.dismiss();
                break;

            default:
                play(0);
                if (user != null) {
                    Intent i = new Intent(Homescreen.this, LeaderBoard.class);
                    i.putExtra("user", user.getUid());
                    startActivity(i);
                } else signIn();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ps.destroyObject();
        ps.destroyObjectBackground();
    }

    private void play(int type) {
        try {
            ps.createSound(this, type);
            ps.playSound();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ps.resumeBackgroundMusic(length);
    }

}
