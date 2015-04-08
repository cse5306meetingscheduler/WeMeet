package com.cse5306.wemeet.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sathvik on 08/04/15.
 */
public class UserPreferences {

    SharedPreferences prefs;
    Context context;

    public UserPreferences(Context context) {
         prefs = context.getSharedPreferences(
                 "com.sathvik1709.wemeet", Context.MODE_PRIVATE);
    }

    public boolean getUserPrefKeepLogin(){
        return prefs.getBoolean("KeepMeLoggedIn",false);
    }

    public void setUserPrefKeepLogin(boolean s){
        prefs.edit().putBoolean("KeepMeLoggedIn",s).commit();
    }

    public String getUserPrefUsername(){
        return prefs.getString("UserName",null);
    }

    public void setUserPrefUsername(String un){
        prefs.edit().putString("UserName",un).commit();
    }

    public void setUserPrefPassword(String pw){
        prefs.edit().putString("PassWord",pw).commit();
    }

    public String getUserPrefPassword(){
        return prefs.getString("PassWord",null);
    }

}
