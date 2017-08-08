package com.example.firdous.crazydigits;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LeaderBoard extends AppCompatActivity {
    ListView list;
    ProgressDialog dialog;
    DatabaseReference databaseReference;
    FirebaseListAdapter<LeaderView> listAdapter;
    CardView myscore;
    TextView name, level, time;
    PlaySound ps = new PlaySound();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        myscore = (CardView) findViewById(R.id.myscore);
        name = (TextView) findViewById(R.id.name);
        level = (TextView) findViewById(R.id.level);
        time = (TextView) findViewById(R.id.time);
        list = (ListView) findViewById(R.id.leaderlist);
        list.setDivider(new ColorDrawable(Color.TRANSPARENT));
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://crazy-digits-2d7a1.firebaseio.com/Marathon");
        dialog = ProgressDialog.show(this, "Please wait...", "retrieving data from server");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // the initial data has been loaded, hide the progress bar
                dialog.dismiss();
                String userId = getIntent().getStringExtra("user");
                if (dataSnapshot.child(userId).exists()) {
                    showMyScore(dataSnapshot.child(userId).child("name").getValue().toString(),
                            dataSnapshot.child(userId).child("level").getValue().toString(),
                            dataSnapshot.child(userId).child("time").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(LeaderBoard.this, "Try Again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        final Query queryRef = databaseReference.orderByChild("level").limitToLast(20);

        listAdapter = new FirebaseListAdapter<LeaderView>(
                this,
                LeaderView.class,
                R.layout.showleaderboard,
                queryRef) {
            @Override
            protected void populateView(View view, final LeaderView cardsView, final int i) {

                setData(view, cardsView, i);
            }

            @Override
            public LeaderView getItem(int position) {
                return super.getItem(getCount() - (position + 1));
            }

        };
        list.setAdapter(listAdapter);
    }

    public void setData(View view, LeaderView cardsView, int i) {
        if (i == 0)
            view.findViewById(R.id.root).setBackgroundColor(Color.rgb(252, 210, 129));
        else if (i == 1)
            view.findViewById(R.id.root).setBackgroundColor(Color.rgb(194, 194, 194));
        else if (i == 2)
            view.findViewById(R.id.root).setBackgroundColor(Color.rgb(204, 186, 186));
        else
            view.findViewById(R.id.root).setBackgroundColor(Color.WHITE);

        ((TextView) view.findViewById(R.id.index)).setText((i + 1) + ". ");
        ((TextView) view.findViewById(R.id.name)).setText(cardsView.getName());
        ((TextView) view.findViewById(R.id.level)).setText("Levels cleared: " + cardsView.getLevel());
        ((TextView) view.findViewById(R.id.time)).setText("Total time: " + cardsView.getTime() + " sec");

    }

    private void showMyScore(String s1, String s2, String s3) {
        myscore.setVisibility(View.VISIBLE);


        name.setText(s1);
        level.setText("levels cleared: " + s2);
        time.setText("total time: " + s3);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Homescreen.length = ps.getSeekLength();
    }

}

class LeaderView {
    String name;
    int level;
    long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

