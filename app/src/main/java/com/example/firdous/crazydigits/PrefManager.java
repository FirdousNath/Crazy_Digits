package com.example.firdous.crazydigits;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by encrypted on 15/4/17.
 */
public class PrefManager {
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final boolean IS_RATED = false;
    // Shared pref file name
    private static final String PREF_NAME = "NumberGame";
    SharedPreferences.Editor editor;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    private SharedPreferences pref;
    private Context context;

    public PrefManager(Context c)
    {
        context=c;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

    }

    void createSession(ArrayList<LevelStatus> list)
    {
        editor.putBoolean("FirstTime", true);
        editor.commit();
        putListObject("veryeasy", list);
        putListObject("easy", list);
        putListObject("medium", list);
        putListObject("hard",list);
        putListObject("legend",list);


    }
    public ArrayList<LevelStatus> getListObject(String key, Class<?> mClass){
    	Gson gson = new Gson();

    	ArrayList<String> objStrings = getListString(key);
    	ArrayList<LevelStatus> objects =  new ArrayList<LevelStatus>();

    	for(String jObjString : objStrings){
            LevelStatus value  = (LevelStatus) gson.fromJson(jObjString,  mClass);
    		objects.add(value);
    	}
    	return objects;
    }

    public void putListObject(String key, ArrayList<LevelStatus> objArray){
    Gson gson = new Gson();
    ArrayList<String> objStrings = new ArrayList<String>();
    for(Object obj : objArray){
        objStrings.add(gson.toJson(obj));
    }
    putListString(key, objStrings);
    }

    public void putListString(String key, ArrayList<String> stringList) {

        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        pref.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(pref.getString(key, ""), "‚‗‚")));
    }

    public boolean isFirstTime() {
        return pref.getBoolean("FirstTime",false);
    }


    public boolean getIsRated() {
        return pref.getBoolean("IsRated", false);
    }

    public void setIsRated(boolean isRated) {
        editor.putBoolean("IsRated", isRated);
        editor.commit();
    }

    public void setTOTAL(int total) {
        editor.putInt("total", total);
        editor.commit();
    }

    public int getTotal() {
        return pref.getInt("total", 0);
    }

}
