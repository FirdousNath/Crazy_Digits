package com.example.firdous.crazydigits;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class OtherMenus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_menus);
        ListView menulist = (ListView)findViewById(R.id.menulist);
        String[] arr= {"Rate ","About","Contact","Credits"};
        menulist.setAdapter(new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, arr));
        menulist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        Uri uri = Uri.parse("market://details?id=" + OtherMenus.this.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        finish();
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + OtherMenus.this.getPackageName())));
                            finish();
                        }
                        break;
                    case 1: startActivity(new Intent(OtherMenus.this, Aboutus.class));
                            break;
                    case 2:
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "firunath@gmail.com"});
                        email.putExtra(Intent.EXTRA_SUBJECT, "Feedback about Crazy Digits");
                        email.putExtra(Intent.EXTRA_TEXT, "");
                        //need this to prompts email client only
                        email.setType("message/rfc822");
                        startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        break;

                    case 3:
                        startActivity(new Intent(OtherMenus.this, Credits.class));
                        break;
                }
            }
        });
    }
}
