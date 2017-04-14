package com.example.damkarlearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ASUS on 25/02/2017.
 */

public class SettingActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String APP_COLOR = "appColor";
    SharedPreferences myPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(APP_COLOR)) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));

            String value = sharedPreferences.getString(key, "");
           // setBackGroundColor(value);
            Log.i("TAG", value);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void setBackGroundColor(String value) {


        LinearLayout  linearLayout = (LinearLayout) findViewById(R.id.toolbar);
        switch (value) {
            case "Red" :
            {

                linearLayout.setBackgroundColor(Color.RED);
                break;
            }

            case "Blue" :
            {
                linearLayout.setBackgroundColor(0x7f01009b);
                break;
            }

            case "Black" :
            {
                linearLayout.setBackgroundColor(Color.BLACK);
                break;
            }

            case "Green" :
            {
                linearLayout.setBackgroundColor(Color.GREEN);
                break;
            }
        }

    }
}
