package com.example.firdous.crazydigits;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView t[];
    TextView input;
    int count=0;
    int arrayofinput[];
    Chronometer simpleChronometer;
    int id[];
    ArrayList<Integer>list;
    int MAX,MIN, MinIntervel, AvgIntervel;
    long timeWhenStopped = 0;
    PlaySound ps = new PlaySound();
    int length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        length = ps.pauseBackgroundMusic();
        super.onCreate(savedInstanceState);
        int avg[],min[];
        switch (SelectDificulty.difficulty)
        {

            case 0:
                setContentView(R.layout.activity_main);
                input = (TextView) findViewById(R.id.input);
                t=new TextView[20];
                id = new int[]{
                        R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7,
                        R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12, R.id.t13,
                        R.id.t14, R.id.t15, R.id.t16, R.id.t17, R.id.t18, R.id.t19, R.id.t20
                };
                MIN=99999;
                MAX=999999;

                avg=getResources().getIntArray(R.array.very_easy_level_avg);
                min=getResources().getIntArray(R.array.very_easy_level_min);

                MinIntervel=min[SelectLevel.postion];
                AvgIntervel=avg[SelectLevel.postion];

                startCountDown();
                break;
            case 1:
                setContentView(R.layout.activity_main);
                t=new TextView[20];
                id = new int[]{
                        R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7,
                        R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12, R.id.t13,
                        R.id.t14, R.id.t15, R.id.t16, R.id.t17, R.id.t18, R.id.t19, R.id.t20
                };
                input = (TextView) findViewById(R.id.input);
                list = new ArrayList<>();
                MIN=999999;
                MAX=9999999;

                avg=getResources().getIntArray(R.array.easy_level_avg);
                min=getResources().getIntArray(R.array.easy_level_min);

                MinIntervel=min[SelectLevel.postion];
                AvgIntervel=avg[SelectLevel.postion];

                startCountDown();
                break;
            case 2:
                setContentView(R.layout.hard);
                t=new TextView[30];
                id = new int[]{
                        R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7,
                        R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12, R.id.t13,
                        R.id.t14, R.id.t15, R.id.t16, R.id.t17, R.id.t18, R.id.t19, R.id.t20,
                        R.id.t21,R.id.t22,R.id.t23,R.id.t24,R.id.t25,R.id.t26,R.id.t27,
                        R.id.t28,R.id.t29,R.id.t30
                };
                input = (TextView) findViewById(R.id.input);
                list = new ArrayList<>();
                MIN=999999;
                MAX=9999999;

                avg=getResources().getIntArray(R.array.medium_level_avg);
                min=getResources().getIntArray(R.array.medium_level_min);

                MinIntervel=min[SelectLevel.postion];
                AvgIntervel=avg[SelectLevel.postion];

                startCountDown();
                break;
            case 3:
                setContentView(R.layout.hard);
                t=new TextView[30];
                id = new int[]{
                        R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7,
                        R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12, R.id.t13,
                        R.id.t14, R.id.t15, R.id.t16, R.id.t17, R.id.t18, R.id.t19, R.id.t20,
                        R.id.t21,R.id.t22,R.id.t23,R.id.t24,R.id.t25,R.id.t26,R.id.t27,
                        R.id.t28,R.id.t29,R.id.t30
                };
                input = (TextView) findViewById(R.id.input);
                list = new ArrayList<>();
                MIN=99999999;
                MAX=999999999;

                avg=getResources().getIntArray(R.array.hard_level_avg);
                min=getResources().getIntArray(R.array.hard_level_min);

                MinIntervel=min[SelectLevel.postion];
                AvgIntervel=avg[SelectLevel.postion];

                startCountDown();
                break;
            default:
                setContentView(R.layout.hard);
                t=new TextView[30];
                id = new int[]{
                        R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7,
                        R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12, R.id.t13,
                        R.id.t14, R.id.t15, R.id.t16, R.id.t17, R.id.t18, R.id.t19, R.id.t20,
                        R.id.t21,R.id.t22,R.id.t23,R.id.t24,R.id.t25,R.id.t26,R.id.t27,
                        R.id.t28,R.id.t29,R.id.t30
                };
                input = (TextView) findViewById(R.id.input);
                list = new ArrayList<>();
                MIN=99999999;
                MAX=999999999;

                avg=getResources().getIntArray(R.array.legend_level_avg);
                min=getResources().getIntArray(R.array.legend_level_min);

                MinIntervel=min[SelectLevel.postion];
                AvgIntervel=avg[SelectLevel.postion];

                startCountDown();
                break;
        }
    }

    int GenerateRandomNumber(int max, int min)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min) + min;
        return randomNum;
    }

    void InitializeGrid()
    {
        int randomNum;
        ArrayList<Integer> array = new ArrayList<>();

        randomNum = GenerateRandomNumber(MAX, MIN);
        input.setText("" + randomNum);
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
        for (int i = 0; i < t.length; i++)
            t[i].setText("");

        Collections.shuffle(list);

        for (int i = 0; i < 10; i++)
            t[list.get(i)].setText("" + i);

    }

    void startCountDown()
    {
        InitializeGrid();
        final Dialog dialog = new Dialog(MainActivity.this);
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
                simpleChronometer.start();
            }
        }.start();
    }

    public void onClick(View v) {
        TextView temp = (TextView) v;
        if (count < arrayofinput.length && !temp.getText().toString().equalsIgnoreCase("") && Integer.parseInt(temp.getText().toString()) == arrayofinput[count] ) {
            count++;
            play(0);
            String one=input.getText().toString();
            String inserted=one.substring(0,count);
            String tobeInsert=one.substring(count,one.length());
            String text = "<font color=#08C279>"+inserted+"</font>"+tobeInsert;
            input.setText(Html.fromHtml(text));
            if(count == arrayofinput.length)
            {
                simpleChronometer.stop();
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(false);
                dialog.setTitle("");
                dialog.show();
                Button retry = (Button) dialog.findViewById(R.id.retry);
                Button next = (Button) dialog.findViewById(R.id.next);

                final boolean nextLevelunlocked=showStarsOnDialog(dialog, next);

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        play(0);
                        dialog.dismiss();
                        callNext();
                    }
                });
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        play(0);
                        dialog.dismiss();
                        callRetry(nextLevelunlocked);
                    }
                });
            }
            else ResetGrid(list);
        } else {
            play(1);
            simpleChronometer.setBase(simpleChronometer.getBase() - 800);

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void callNext()
    {
        if(SelectLevel.postion < SelectLevel.levelStatusArrayList.size()-1) {
            SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setTotaltime(simpleChronometer.getTimeElapsed() / 1000.00);
            SelectLevel.postion++;
            SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setIsLocked(false);
            saveList();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }
        else {
            SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setTotaltime(simpleChronometer.getTimeElapsed() / 1000.00);
            saveList();
            startActivity(new Intent(MainActivity.this, SelectLevel.class));
            finish();
        }
    }

    void callRetry( boolean isUnlocked)
    {
        if(isUnlocked) {
            if(SelectLevel.postion <  SelectLevel.levelStatusArrayList.size()-1) {
                SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setTotaltime(simpleChronometer.getTimeElapsed() / 1000.00);
                SelectLevel.postion++;
                SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setIsLocked(false);
                SelectLevel.postion--;
                saveList();
            }
            else {
                SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setTotaltime(simpleChronometer.getTimeElapsed() / 1000.00);
                saveList();
            }
        }else {
            SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setTotaltime(simpleChronometer.getTimeElapsed() / 1000.00);
            saveList();
        }
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    boolean showStarsOnDialog(Dialog dialog, Button next)
    {
        boolean nextLevelUnlocked=true;
        ImageView star1=(ImageView)dialog.findViewById(R.id.star1);
        ImageView star2=(ImageView)dialog.findViewById(R.id.star2);
        ImageView star3=(ImageView)dialog.findViewById(R.id.star3);

        if(simpleChronometer.getTimeElapsed()/1000 < MinIntervel)
        {
            star1.setImageResource(R.drawable.staron);
            star3.setImageResource(R.drawable.staron);
            star2.setImageResource(R.drawable.staron);
            SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setStarperlevel(3);
        }else if(simpleChronometer.getTimeElapsed()/1000 >=MinIntervel && simpleChronometer.getTimeElapsed()/1000 < AvgIntervel)
        {
            star1.setImageResource(R.drawable.staron);
            star2.setImageResource(R.drawable.staron);
            star3.setImageResource(R.drawable.staroff);
            SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setStarperlevel(2);
        }
        else {
            nextLevelUnlocked =false;
            star1.setImageResource(R.drawable.staron);
            star2.setImageResource(R.drawable.staroff);
            star3.setImageResource(R.drawable.staroff);
            SelectLevel.levelStatusArrayList.get(SelectLevel.postion).setStarperlevel(1);
        }
        TextView time=(TextView)dialog.findViewById(R.id.time) ;
        time.setText("Total time :" +
                simpleChronometer.getTimeElapsed() / 1000.00 + "sec");
        TextView title=(TextView)dialog.findViewById(R.id.title) ;
        if(nextLevelUnlocked)
        {
            if(SelectLevel.postion < (SelectLevel.levelStatusArrayList.size()-1))
            title.setText("Level "+(SelectLevel.postion+2)+" Unlocked");
            else title.setText("Woo you completed all levels");

        }else {
            next.setVisibility(View.GONE);
            title.setText("Better luck next time");
        }
        return nextLevelUnlocked;

    }

    void saveList()
    {
        switch (SelectDificulty.difficulty)
        {
            case 0 : new PrefManager(MainActivity.this).putListObject("veryeasy", SelectLevel.levelStatusArrayList);
                break;
            case 1 :new PrefManager(MainActivity.this).putListObject("easy", SelectLevel.levelStatusArrayList);
                break;
            case 2 :new PrefManager(MainActivity.this).putListObject("medium", SelectLevel.levelStatusArrayList);
                break;
            case 3 : new PrefManager(MainActivity.this).putListObject("hard", SelectLevel.levelStatusArrayList);
                break;
            default : new PrefManager(MainActivity.this).putListObject("legend", SelectLevel.levelStatusArrayList);

        }

    }

    @Override
    public void onBackPressed() {
        play(1);
        showPauseDialog();
    }

    private void showPauseDialog() {
        timeWhenStopped = simpleChronometer.getBase() - SystemClock.elapsedRealtime();
        simpleChronometer.stop();
        final Dialog dialog =new Dialog(this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pausedialog);
        TextView title=(TextView)dialog.findViewById(R.id.title);
        title.setText("Level "+ (SelectLevel.postion +1));
        Button retry =(Button)dialog.findViewById(R.id.retry);
        Button list =(Button)dialog.findViewById(R.id.list);
        Button resume =(Button)dialog.findViewById(R.id.resume);
        dialog.show();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(0);
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(0);
                ps.resumeBackgroundMusic(length);
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, SelectLevel.class));
                finish();
            }
        });
        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(0);
                simpleChronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                simpleChronometer.start();
                dialog.dismiss();
            }
        });

    }

    private void play(int type) {
        try {
            ps.createSound(this, type);
            ps.playSound();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
