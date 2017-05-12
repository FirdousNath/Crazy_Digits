package com.example.firdous.gridview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Tutorial extends AppCompatActivity implements View.OnClickListener{
    TextView data;
    int dialogcount =0;
    int count=0;
    int arrayofinput[];
    ArrayList<Integer> list;
    TextView t[];
    int id[];
    TextView input;
    Chronometer simpleChronometer;
    Dialog dialog;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (TextView) findViewById(R.id.input);
        t = new TextView[20];

        id = new int[]{
                R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7,
                R.id.t8, R.id.t9, R.id.t10, R.id.t11, R.id.t12, R.id.t13,
                R.id.t14, R.id.t15, R.id.t16, R.id.t17, R.id.t18, R.id.t19, R.id.t20
        };
        input.setText("4590");
        simpleChronometer = (Chronometer) findViewById(R.id.timer);
        InitializeGrid();
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.tutorial);
        data = (TextView) dialog.findViewById(R.id.data);
        data.setText("Welcome to Number game \n\n" +
                " The aim of the game is to type onscreen numbers as fast as you can" +
                "\n\nYou need at least 2 Stars to unlock next level");
        dialog.show();
        next = (Button) dialog.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatatoDialog();
            }
        });
    }
    void setDatatoDialog()
    {
        switch (dialogcount) {
            case 0:
                simpleChronometer.start();
                dialogcount++;
                data.setText("Timer on the top will keep on running" +
                        "\n\nUntil you type all the numbers \n( 4590 )" +
                        "\n\n\n So are you ready to play ?");
                break;
            case 1:
                dialogcount++;
                data.setText("First number is 4\n\n" +
                        "Grid in orange color contains 1-9 digits, so find and click 4 from the grid");
                break;
            case 2:
                dialog.dismiss();
                break;
            case 3:
                dialog.show();
                data.setText("Correct number will turn to GREEN " +
                    "Try clicking other numbers too" +
                    "\n\nEvery wrong click costs you additional 0.8 seconds");
                dialogcount++;
                break;
            case 4: dialog.dismiss();
                break;
            case 5:  dialog.show();
                data.setText("Hurrah! you successfully completed the tutorial" +
                        "\n\n All the best for next level !");
                next.setText("finish");
                dialogcount ++;break;

            default: dialog.dismiss();
                startActivity(new Intent(Tutorial.this, SelectDificulty.class));
                finish();
        }
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
                if(dialogcount<3) {
                    dialogcount =3;
                    setDatatoDialog();
                }
                if(count == arrayofinput.length)
                {
                    simpleChronometer.stop();
                    dialogcount =5;
                    setDatatoDialog();
                }
                else ResetGrid(list);
            }
        }

    void InitializeGrid()
    {
        int randomNum=4590;
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
        for (int i = 0; i < t.length; i++)
            t[i].setText("");

        Collections.shuffle(list);

        for (int i = 0; i < 10; i++)
            t[list.get(i)].setText("" + i);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {

    }
}
