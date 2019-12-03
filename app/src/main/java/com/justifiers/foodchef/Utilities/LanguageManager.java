package com.justifiers.foodchef.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class LanguageManager {
    private static final String TAG = "LanguageManager";

    Context cn;
    String enText, frText, hiText, uaText;

    public LanguageManager(Context cn, String enText, String frText, String hiText, String uaText) {
        this.cn = cn;
        this.enText = enText;
        this.frText = frText;
        this.hiText = hiText;
        this.uaText = uaText;
    }

    public String getTranslatedText() {

        SharedPreferences prefs = cn.getApplicationContext().getSharedPreferences("SettingsActivity", 0);
        String language = prefs.getString("Language", "en");
        Log.d(TAG, "getTranslatedText: current language is " + language);
        switch (language) {
            case "en":
                return enText;
            case "fr":
                return frText;
            case "hi":
                return hiText;
            case "uk":
                return uaText;
            default:
                return enText;
        }
    }
}
