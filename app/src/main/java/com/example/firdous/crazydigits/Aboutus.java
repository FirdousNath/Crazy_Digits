package com.example.firdous.crazydigits;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        final TextView link1=(TextView)findViewById(R.id.link1);
        final TextView link2=(TextView)findViewById(R.id.link2);
        TextView version=(TextView)findViewById(R.id.version);
        version.setText("version "+BuildConfig.VERSION_NAME);
        link1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link1.getText().toString()));
                startActivity(browserIntent);
            }
        });
        link2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link2.getText().toString()));
                startActivity(browserIntent);
            }
        });
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
