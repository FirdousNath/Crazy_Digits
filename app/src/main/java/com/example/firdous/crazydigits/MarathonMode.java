package com.example.firdous.crazydigits;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MarathonMode extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    int count=0;
    int arrayofinput[];
    ArrayList<Integer> list;
    TextView t[];
    int id[];
    TextView input,blinkmode;
    Chronometer simpleChronometer;
    int MAX =0, MIN =0;
    long timeWhenStopped = 0, MAXINTERVEL=0;
    boolean showdialog=false;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String TAG="see";
    private GoogleApiClient mGoogleApiClient;
    ProgressDialog dialog;
    boolean isAuthenticateUser=false;
    Dialog signInDialog;
    DatabaseReference databaseReference;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        BasicIntitalization();
        startCountDown();
        final Animation blink = new AlphaAnimation(1.0f, 0.0f);
        blink.setDuration(850); //You can manage the blinking time with this parameter
        blink.setRepeatMode(Animation.REVERSE);
        blink.setRepeatCount(2);
        if( SelectMode.level > 7 && (SelectMode.level % 8 ) == 0 )
        blinkmode.setText("Blink Mode ON");
        else blinkmode.setText("");
        simpleChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SelectMode.level > 7 && (SelectMode.level % 8) == 0) {
                    if (chronometer.getTimeElapsed() / 1000.0 > 9.0 && chronometer.getTimeElapsed() / 1000.0 < 9.2) {
                        for (TextView at : t)
                            at.startAnimation(blink);
                    }
                    if (chronometer.getTimeElapsed() / 1000.0 > 7.0 && chronometer.getTimeElapsed() / 1000.0 < 7.2) {
                        for (TextView at : t)
                            at.setVisibility(View.INVISIBLE);
                    }
                    if (chronometer.getTimeElapsed() / 1000.0 > 5.0 && chronometer.getTimeElapsed() / 1000.0 < 5.2) {
                        for (TextView at : t)
                            at.setVisibility(View.VISIBLE);
                    }
                }


                if (chronometer.getTimeElapsed() / 1000.0 < 0.9 ) {
                    Log.d("","aaya");
                    chronometer.stop();
                    chronometer.setText("00:00:00");
                    if (showdialog)
                        showMarathonScore();
                    else showdialog = true;
                }
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    isAuthenticateUser=true;
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
            }
        };
    }

    private void showMarathonScore() {
        simpleChronometer.setText("00:00:00");
        signInDialog=new Dialog(this);
        signInDialog.setCancelable(false);
        signInDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signInDialog.setContentView(R.layout.marathon_score_dialog);
        Button share=(Button)signInDialog.findViewById(R.id.share);
        Button exit=(Button)signInDialog.findViewById(R.id.exit);
        Button retry=(Button)signInDialog.findViewById(R.id.retry);
        TextView level=(TextView)signInDialog.findViewById(R.id.level);
        TextView time=(TextView)signInDialog.findViewById(R.id.time);

        level.setText("You cleared "+ SelectMode.level +" levels");
        time.setText("Time taken: " + (SelectMode.time / 1000.0) + " sec");
        signInDialog.show();
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInDialog.dismiss();
                finish();
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInDialog.dismiss();
                SelectMode.level = 0;
                SelectMode.time = 0;
                startActivity(new Intent(MarathonMode.this, MarathonMode.class));
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetworkConnection(MarathonMode.this)) {
                    if(isAuthenticateUser)
                    {

                        databaseReference = FirebaseDatabase.getInstance().
                                    getReferenceFromUrl("https://crazy-digits-2d7a1.firebaseio.com/Marathon");
                        databaseReference.child(user.getUid()).child("Level").
                                    setValue(SelectMode.level);
                        databaseReference.child(user.getUid()).child("Time").
                                    setValue(SelectMode.time / 1000.0);
                        databaseReference.child(user.getUid()).child("Name").
                                    setValue(user.getDisplayName());
                        Toast.makeText(MarathonMode.this, "Scored Shared", Toast.LENGTH_LONG).show();
                        finish();
                    }else  signIn();
                }else ShowNoInternetDialog(MarathonMode.this);

            }
        });

    }

    void BasicIntitalization()
    {
        int choice;
        if(SelectMode.level < 10)
        {
            choice=0;
            MAXINTERVEL=11100;

            switch (SelectMode.level)
            {
                case 0 :
                    MAX = 9999;
                    MIN = 999;
                    break;
                case 1 :
                    MAX = 9999;
                    MIN = 999;
                    break;
                case 2 :
                    MAX = 99999;
                    MIN = 9999;
                    break;
                case 3 :
                    MAX = 99999;
                    MIN = 9999;
                    break;
                case 4 :
                    MAX = 999999;
                    MIN = 99999;
                    break;
                case 5 :
                    MAX = 999999;
                    MIN = 99999;
                    break;
                case 6 :
                    MAX = 9999999;
                    MIN = 999999;
                    break;
                case 7 :
                    MAX = 9999999;
                    MIN = 999999;
                    break;
                case 8 :
                    MAXINTERVEL=16100;
                    MAX = 99999999;
                    MIN = 9999999;
                    break;
                default:
                    MAXINTERVEL=16100;
                    MAX = 999999999;
                    MIN = 99999999;
            }

        }
        else
        {
            choice=1;
            MAXINTERVEL=16100;
            switch (SelectMode.level)
            {
                case 10 :
                    MAX = 9999;
                    MIN = 999;
                    break;
                case 11 :
                    MAX = 9999;
                    MIN = 999;
                    break;
                case 12 :
                    MAX = 99999;
                    MIN = 9999;
                    break;
                case 13 :
                    MAX = 99999;
                    MIN = 9999;
                    break;
                case 14 :
                    MAX = 999999;
                    MIN = 99999;
                    break;
                case 15 :
                    MAX = 999999;
                    MIN = 99999;
                    break;
                case 16 :
                    MAX = 9999999;
                    MIN = 999999;
                    break;
                case 17 :
                    MAX = 9999999;
                    MIN = 999999;
                    break;
                case 18 :
                    MAX = 99999999;
                    MIN = 9999999;
                    break;
                default:
                    MAX = 999999999;
                    MIN = 99999999;
            }

        }
        switch (choice) {
            case  0:
                setContentView(R.layout.activity_main);
                input = (TextView) findViewById(R.id.input);
                blinkmode = (TextView) findViewById(R.id.blinkmode);
                simpleChronometer = (Chronometer) findViewById(R.id.timer);

                t = new TextView[20];

                id = new int[]{
                        R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7,
                        R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12, R.id.t13,
                        R.id.t14, R.id.t15, R.id.t16, R.id.t17, R.id.t18, R.id.t19, R.id.t20
                };

                break;
            default:
                setContentView(R.layout.hard);
                blinkmode = (TextView) findViewById(R.id.blinkmode);
                input = (TextView) findViewById(R.id.input);
                simpleChronometer = (Chronometer) findViewById(R.id.timer);
                t = new TextView[30];

                id = new int[]{
                        R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7,
                        R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12, R.id.t13,
                        R.id.t14, R.id.t15, R.id.t16, R.id.t17, R.id.t18, R.id.t19, R.id.t20,
                        R.id.t21, R.id.t22, R.id.t23, R.id.t24, R.id.t25, R.id.t26, R.id.t27,
                        R.id.t28, R.id.t29, R.id.t30
                };
        }
    }

    void startCountDown()
    {
        InitializeGrid();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.countdown);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        final TextView timer=(TextView)dialog.findViewById(R.id.count);
        new CountDownTimer(2000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished/1000 == 0)
                    timer.setText("Go");
                else  timer.setText("Get Set");
            }
            @Override
            public void onFinish() {

                dialog.dismiss();
                simpleChronometer = (Chronometer) findViewById(R.id.timer);
                simpleChronometer.setBase(SystemClock.elapsedRealtime() + MAXINTERVEL);
                simpleChronometer.start();
            }
        }.start();
    }

    public void onClick(View v) {
        TextView temp = (TextView) v;
        if (count < arrayofinput.length && !temp.getText().toString().equalsIgnoreCase("") && Integer.parseInt(temp.getText().toString()) == arrayofinput[count] ) {
            count++;
            String one=input.getText().toString();
            String inserted=one.substring(0,count);
            String tobeInsert=one.substring(count,one.length());
            String text = "<font color=#08C279>"+inserted+"</font>"+tobeInsert;
            input.setText(Html.fromHtml(text));

            if(count == arrayofinput.length)
            {
                simpleChronometer.stop();
                SelectMode.time+=MAXINTERVEL-simpleChronometer.getTimeElapsed();
                showCustomDialog();
            }
            else ResetGrid(list);
        }
        else simpleChronometer.setBase(simpleChronometer.getBase() - 800);
    }

    private void showCustomDialog() {
        final Dialog dialog =new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.marathondialog);
        TextView title=(TextView)dialog.findViewById(R.id.title);
        TextView time=(TextView)dialog.findViewById(R.id.time);
        title.setText("Level " + (SelectMode.level + 1));
        time.setText("Total time : "+(SelectMode.time /1000.0)+ " sec");
        Button retry =(Button)dialog.findViewById(R.id.retry);
        dialog.show();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SelectMode.level++;
                startActivity(new Intent(MarathonMode.this, MarathonMode.class));
                finish();
            }
        });
    }

    void InitializeGrid()
    {
        int randomNum=GenerateRandomNumber(MAX, MIN);
        input.setText("" + randomNum);

        ArrayList<Integer> array = new ArrayList<>();
        list = new ArrayList<>();

        for (int i = 0; i < t.length; i++) {
            t[i] = (TextView) findViewById(id[i]);
            t[i].setOnClickListener(this);
        }

        do {
            array.add(randomNum % 10);
            randomNum /= 10;
        } while (randomNum > 0);

        Collections.reverse(array);
        arrayofinput = new int[array.size()];

        for (int i = 0; i < array.size(); i++)
            arrayofinput[i] = array.get(i);

        for (int i = 0; i < t.length; i++) {
            list.add(i);
            t[i].setText("");
        }
        ResetGrid(list);
    }

    void ResetGrid(ArrayList<Integer> list)
    {
        for (TextView aT : t) aT.setText("");

        Collections.shuffle(list);

        for (int i = 0; i < 10; i++)
            t[list.get(i)].setText("" + i);

    }

    int GenerateRandomNumber(int max, int min)
    {
        Random rand = new Random();
        return rand.nextInt(max - min) + min;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        timeWhenStopped = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
        simpleChronometer.stop();
        final Dialog dialog =new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pausedialog);
        TextView title=(TextView)dialog.findViewById(R.id.title);
        title.setText("Level "+ (SelectMode.level+1));
        Button retry =(Button)dialog.findViewById(R.id.retry);
        Button list =(Button)dialog.findViewById(R.id.list);
        Button resume =(Button)dialog.findViewById(R.id.resume);
        dialog.show();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                SelectMode.level=0;
                SelectMode.time=0;
                startActivity(new Intent(MarathonMode.this, MarathonMode.class));
                finish();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                simpleChronometer.start();
                dialog.dismiss();
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
        dialog = ProgressDialog.show(MarathonMode.this, "Please wait", "while we Sign In");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 10) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                Log.d(account.getId(),account.getIdToken());
                firebaseAuthWithGoogle(account);
            } else {
                Log.d("see", ""+ result.getStatus().getStatusMessage());
                Log.d("see", ""+ result.getStatus().getStatusCode());
                dialog.dismiss();
                mGoogleApiClient.stopAutoManage(MarathonMode.this);
                mGoogleApiClient.disconnect();
                Toast.makeText(MarathonMode.this, "Google play services error", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(MarathonMode.this, "No Internet",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(TAG, "signInWithCredential", task.getException());
                                Toast.makeText(MarathonMode.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dialog.dismiss();
                            signInDialog.dismiss();
                            databaseReference = FirebaseDatabase.getInstance().
                                    getReferenceFromUrl("https://crazy-digits-2d7a1.firebaseio.com/Marathon");
                            databaseReference.child(user.getUid()).child("Level").
                                    setValue(SelectMode.level);
                            databaseReference.child(user.getUid()).child("Time").
                                    setValue(SelectMode.time / 1000.0);
                            databaseReference.child(user.getUid()).child("Name").
                                    setValue(user.getDisplayName());

                            Toast.makeText(MarathonMode.this, "Scored Shared", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                });
    }

    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void ShowNoInternetDialog( final Context context)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("No Internet Connectivity !");
        alertDialogBuilder.setMessage("Turn On WiFi or Mobile-Data and refresh the App");

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (!haveNetworkConnection(context))
                            MarathonMode.ShowNoInternetDialog(context);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
