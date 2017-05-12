package com.example.firdous.gridview;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by firdous on 4/5/17.
 */

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("ff.otf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}
