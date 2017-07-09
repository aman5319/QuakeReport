package com.example.amidezcod.quakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class EarthQuakePreferencePragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference listPreference = findPreference(getString(R.string.list_key_orderby));
            bindPrefernceSummaryToValue(listPreference);

            Preference editPreference = findPreference(getString(R.string.min_mag_key));
            bindPrefernceSummaryToValue(editPreference);

        }

        private void bindPrefernceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String s = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, s);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefindex = listPreference.findIndexOfValue(o.toString());
                if (prefindex >= 0) {
                    CharSequence[] charSequence = listPreference.getEntries();
                    listPreference.setSummary(charSequence[prefindex]);
                }
            } else {
                preference.setSummary(String.valueOf(o));
            }
            return true;
        }
    }
}

