package com.trekcoders.safar.utils;

/**
 * Created by akrmhrjn on 1/5/15.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "safar";

    // All Shared Preferences Keys
    private static final String FIVE_FRIEND = "FIVE_FRIEND";
    private static final String THREE_FRIEND = "THREE_FRIEND";


    // Constructor
    public Pref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setFriendsNumberFive(Boolean value) {
        editor.putBoolean(FIVE_FRIEND, value);

        // commit changes
        editor.commit();
    }

    public void setFriendsNumberThree(Boolean value) {
        editor.putBoolean(THREE_FRIEND, value);

        // commit changes
        editor.commit();
    }

    public Boolean getFriendsNumberThree() {

        return pref.getBoolean(THREE_FRIEND, false);
    }

    public Boolean getFriendsNumberFive() {

        return pref.getBoolean(FIVE_FRIEND, false);
    }


    public void clearData() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }
}