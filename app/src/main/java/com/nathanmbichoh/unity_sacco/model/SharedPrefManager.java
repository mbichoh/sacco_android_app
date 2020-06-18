package com.nathanmbichoh.unity_sacco.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.nathanmbichoh.unity_sacco.Login;
import com.nathanmbichoh.unity_sacco.pojo.MemberData;

public class SharedPrefManager {

    //constants
    private static final String SHARED_PREF_NAME    = "unitysaccoapp";
    private static final String KEY_NAME            = "keyname";
    private static final String KEY_NATIONAL_ID     = "keynationalid";
    private static final String KEY_PHONE           = "keyphone";
    private static final String KEY_ID              = "keyid";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    //create an instance
    private SharedPrefManager(Context context){
        mCtx = context;
    }

    //get instance
    public static synchronized SharedPrefManager getInstance(Context context) {
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //let user login and hold data in the sharedPrefManager
    public void userLogin(MemberData memberData){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, memberData.getMember_id());
        editor.putString(KEY_NAME, memberData.getMember_name());
        editor.putString(KEY_NATIONAL_ID, memberData.getMember_national_id());
        editor.putString(KEY_PHONE, memberData.getMember_phone());
        editor.apply();
    }

    //check if user is logged IN
    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences  = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(KEY_NAME, null) != null;
    }

    //get logged in user
    public MemberData getUser(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  new MemberData(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_NATIONAL_ID, null),
                sharedPreferences.getString(KEY_PHONE, null)
        );
    }

    //logout user
    public void logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, Login.class));
    }
}
