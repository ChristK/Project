package com.example.project.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPerencesUtil {
    private static final String TAG="TAG";
    private static final String KEY_LOGIN="KEY_LOGIN";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static SharedPerencesUtil sharedPerencesUtil;
    private final Context context;


    public SharedPerencesUtil(Context context){
        this.context=context.getApplicationContext();
        sharedPreferences=this.context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public static SharedPerencesUtil getInstance(Context context){
        if(sharedPerencesUtil==null){
            sharedPerencesUtil=new SharedPerencesUtil(context);
        }
        return sharedPerencesUtil;
    }

    //Check login
    public boolean isLogin(){
        return  getBoolean(KEY_LOGIN,false);
    }

    //Login state
    public void setLogin(boolean value){
        putBoolean(KEY_LOGIN,value);
    }

    //private method

    private void put(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }

    private void putBoolean(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }

    private String get(String key){
        return sharedPreferences.getString(key,"");
    }

    private boolean getBoolean(String key,boolean defaultValue){
        return sharedPreferences.getBoolean(key,defaultValue);
    }

    private void putInt(String key,int value){
        editor.putInt(key,value);
        editor.apply();
    }

    private int getInt(String key,int defaultValue){
        return  sharedPreferences.getInt(key,defaultValue);
    }


}
