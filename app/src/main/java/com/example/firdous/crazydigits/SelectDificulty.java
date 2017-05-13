package com.example.firdous.crazydigits;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectDificulty extends AppCompatActivity {
    public  static int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dificulty);
        findViewById(R.id.veryeasy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty=0;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
            }
        });
        findViewById(R.id.easy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty=1;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
            }
        });
        findViewById(R.id.medium).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty=2;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
            }
        });
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty=3;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
            }
        });
        findViewById(R.id.legend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                difficulty=4;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
