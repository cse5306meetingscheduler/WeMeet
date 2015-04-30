package com.cse5306.wemeet.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * User preferences holds information of the session username and location
 */
public class UserPreferences {

    SharedPreferences prefs;
    Context context;

    public UserPreferences(Context context) {
         prefs = context.getSharedPreferences(
                 "com.sathvik1709.wemeet", Context.MODE_PRIVATE);
    }

    public String getSessionUserPrefUsername(){
        return prefs.getString("SessionUserName",null);
    }

    public void setSessionUserPrefUsername(String un){
        prefs.edit().putString("SessionUserName",un).apply();
    }

    public boolean getUserPrefKeepLogin(){
        return prefs.getBoolean("KeepMeLoggedIn",false);
    }

    public void setUserPrefKeepLogin(boolean s){ prefs.edit().putBoolean("KeepMeLoggedIn",s).apply(); }

    public String getUserPrefUsername(){
        return prefs.getString("UserName",null);
    }

    public void setUserPrefUsername(String un){
        prefs.edit().putString("UserName",un).apply();
    }

    public void setUserPrefPassword(String pw){
        prefs.edit().putString("PassWord",pw).apply();
    }

    public String getUserPrefPassword(){
        return prefs.getString("PassWord",null);
    }

    public void setUserPrefHomeLocation(String hl) { prefs.edit().putString("UserHomeLocation",hl).apply(); }

    public String getUserPrefHomeLocation(){ return prefs.getString("UserHomeLocation",null);}

}
