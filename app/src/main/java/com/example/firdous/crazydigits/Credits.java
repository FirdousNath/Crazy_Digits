package com.example.firdous.crazydigits;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Credits extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        ListView list = (ListView) findViewById(R.id.list);
        String arr[] = new String[]{
                "\nSounds \nCreative Commons Attribution 3.0 \n\nhttp://soundbible.com/royalty-free-sounds-1.html#creativecommonssounds",
                "\n\nIcons \nFlaticons\n\nhttps://www.flaticon.com/ ",
        };
        final String arr1[] = new String[]{
                "http://soundbible.com/royalty-free-sounds-1.html#creativecommonssounds",
                "nhttps://www.flaticon.com/ ",
        };
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(arr1[i]));
                startActivity(myIntent);
            }
        });
    }
}
