package com.example.firdous.crazydigits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectDificulty extends AppCompatActivity implements View.OnClickListener {
    public  static int difficulty;
    PlaySound ps = new PlaySound();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_dificulty);
        findViewById(R.id.veryeasy).setOnClickListener(this);
        findViewById(R.id.easy).setOnClickListener(this);
        findViewById(R.id.medium).setOnClickListener(this);
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.legend).setOnClickListener(this);
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ps.destroyObject();
    }

    @Override
    public void onClick(View view) {
        play(0);
        switch (view.getId()) {
            case R.id.veryeasy:
                difficulty=0;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
                break;
            case R.id.easy:
                difficulty=1;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
                break;
            case R.id.medium:
                difficulty=2;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
                break;
            case R.id.legend:
                difficulty = 4;
                startActivity(new Intent(SelectDificulty.this, SelectLevel.class));
                finish();
                break;
            default:
                difficulty=3;
                startActivity(new Intent(SelectDificulty.this,SelectLevel.class));
                finish();
        }
    }
}
