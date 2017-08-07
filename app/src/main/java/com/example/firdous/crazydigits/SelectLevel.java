package com.example.firdous.crazydigits;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectLevel extends AppCompatActivity
        implements ImageAdapter.ItemClickListener{
    public static ArrayList<LevelStatus> levelStatusArrayList;
    public  static int postion;
    PlaySound ps = new PlaySound();
    PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);
        TextView stars=(TextView)findViewById(R.id.totalstars);
        pref=new PrefManager(this);
        if(!pref.isFirstTime()) {
            levelStatusArrayList =new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                LevelStatus ls = new LevelStatus();
                ls.setStarperlevel(0);
                ls.setTotaltime(0.00);
                ls.setIsLocked(true);
                levelStatusArrayList.add(ls);
            }
            levelStatusArrayList.get(0).setIsLocked(false);
            pref.createSession(levelStatusArrayList);
            stars.setText("Stars : 0/" + (levelStatusArrayList.size() * 3));
            startActivity(new Intent(SelectLevel.this,Tutorial.class));
            finish();
        }
        else {
            int totalstars=0;
            switch (SelectDificulty.difficulty)
            {
                case 0 : levelStatusArrayList = pref.getListObject("veryeasy",LevelStatus.class);
                    break;
                case 1 :levelStatusArrayList = pref.getListObject("easy",LevelStatus.class);
                    break;
                case 2 :levelStatusArrayList = pref.getListObject("medium",LevelStatus.class);
                    break;
                case 3 : levelStatusArrayList = pref.getListObject("hard",LevelStatus.class);
                    break;
                default : levelStatusArrayList = pref.getListObject("legend",LevelStatus.class);
            }

            for (int i = 0; i < 30; i++)
                totalstars=totalstars+levelStatusArrayList.get(i).getStarperlevel();
            stars.setText("Stars : " + totalstars + "/"+(levelStatusArrayList.size()*3));
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        ImageAdapter adapter = new ImageAdapter(this, levelStatusArrayList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(View view, int position) {

       if(!levelStatusArrayList.get(position).isLocked())
       {
           play(0);
           postion = position;
           startActivity(new Intent(SelectLevel.this, MainActivity.class));
           finish();
       } else play(1);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        play(1);
        finish();
        startActivity(new Intent(SelectLevel.this, SelectDificulty.class));
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
