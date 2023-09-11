package com.clj.blesample.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.clj.blesample.LoginActivity;

public class SessionManager {
    private Context _context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ID = "id";
    public static final String NIK = "nik";
    public static final String NAMA = "nama";
    public static final String BIRTH_DATE = "birth_date";
    public static final String BPJS_NUMBER = "bpjs_number";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String ADDRESS = "address";
    public static final String HASH_PASSWORD = "hash_password";
    public static final String GENDER = "gender";

    public static final String EWS_SCORE = "ews_score";
    public static final String EWS_LABEL = "ews_label";
    public static final String EWS_LASTDATE = "ews_lastdate";


    public SessionManager (Context context ){
        this._context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }
    public void createLoginSession(String username, String password) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(EMAIL, username);
        editor.putString(PASSWORD, password);
        editor.commit();
    }
    public void createUserSession(ModelPasien user) {
        editor.putString(ID, user.getId());
        editor.putString(NAMA, user.getName());
        editor.putString(BIRTH_DATE, user.getId());
        editor.putString(BPJS_NUMBER, user.getBpjs_number());
        editor.putString(ADDRESS, user.getAddress());
        editor.putString(HASH_PASSWORD, user.getPassword());
        editor.putString(PHONE_NUMBER, user.getPhone_number());
        editor.putString(GENDER, user.getDm_gender_id());
        editor.commit();
    }
    public void createMonitoringSession(ModelMonitoring mon) {
        editor.putString(EWS_SCORE, mon.getNews_score());
        editor.putString(EWS_LABEL, mon.getRisk());
        editor.putString(EWS_LASTDATE, mon.getTime_acquired());
        editor.commit();
    }
    public String getID(){
        return sharedPreferences.getString(ID, null);
    }
    public String getEmail(){
        return sharedPreferences.getString(EMAIL, null);
    }
    public String getPassword(){
        return sharedPreferences.getString(PASSWORD, null);
    }
    public String getName(){
        return sharedPreferences.getString(NAMA, null);
    }
    public String getBirthDate(){
        return sharedPreferences.getString(BIRTH_DATE, null);
    }
    public String getBPJS(){
        return sharedPreferences.getString(BPJS_NUMBER, null);
    }
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public String getEWS_SCORE(){
        return sharedPreferences.getString(EWS_SCORE, null);
    }
    public String getEWS_LABEL(){
        return sharedPreferences.getString(EWS_LABEL, null);
    }
    public String getEWS_LASTDATE(){
        return sharedPreferences.getString(EWS_LASTDATE, null);
    }
    public void logoutSession(){
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.clear();
        prefsEditor.apply();
        this.goToLogin();
    }
    private void goToLogin() {
        Intent i = new Intent(_context, LoginActivity.class);
        Toast.makeText(_context, "Berhasil Logout", Toast.LENGTH_LONG).show();

        // Close all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);

    }
}
