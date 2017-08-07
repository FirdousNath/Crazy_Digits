package com.example.firdous.crazydigits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectMode extends AppCompatActivity implements View.OnClickListener {
    public  static int level=0;
    public  static long time=0;
    PlaySound ps = new PlaySound();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        findViewById(R.id.classic).setOnClickListener(this);
        findViewById(R.id.marathon).setOnClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
    public void onClick(View view) {
        play(0);
        switch (view.getId()) {
            case R.id.marathon:
                level = 0;
                time = 0;
                startActivity(new Intent(SelectMode.this, MarathonMode.class));
                break;

            default:
                startActivity(new Intent(SelectMode.this, SelectDificulty.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ps.destroyObject();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Homescreen.length = ps.getSeekLength();
    }
}
