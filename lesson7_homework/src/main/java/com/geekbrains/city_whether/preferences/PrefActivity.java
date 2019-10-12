package com.geekbrains.city_whether.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.geekbrains.city_whether.R;

public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
    }
}
