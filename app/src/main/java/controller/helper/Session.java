package controller.helper;

import android.content.Context;
import android.content.SharedPreferences;


public class Session {

    private static  SharedPreferences prefs; // static because it should global to class Session

    public Session(String PREFERENCE,Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = cntx.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
    }

    public void setUserEmail(String userEmail) {
        Session.prefs.edit().putString("user_email", userEmail).commit();
    }

    public String getUserEmail() {
        return Session.prefs.getString("user_email","");

    }
    public void setPasswordVerifyEmail(String userEmail) {
        Session.prefs.edit().putString("password_verify_email", userEmail).commit();
    }

    public String getPasswordVerifyEmail() {
        return prefs.getString("password_verify_email","");

    }

    public void sessionDestroy(){
        Session.prefs.edit().clear().commit();
    }
    public String deleteSessionArrtibute(String key){
        Session.prefs.edit().remove(key).commit();
        return key;
    }
}