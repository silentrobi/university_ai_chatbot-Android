package controller.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setUserEmail(String userEmail) {
        prefs.edit().putString("user_email", userEmail).commit();
    }

    public String getUserEmail() {
        return prefs.getString("user_email","");

    }
    public void setPasswordVerifyEmail(String userEmail) {
        prefs.edit().putString("password_verify_email", userEmail).commit();
    }

    public String getPasswordVerifyEmail() {
        return prefs.getString("password_verify_email","");

    }

    public void sessionDestroy(){
        prefs.edit().clear().commit();
    }
    public String deleteSessionArrtibute(String key){
        prefs.edit().remove(key).commit();
        return key;
    }
}