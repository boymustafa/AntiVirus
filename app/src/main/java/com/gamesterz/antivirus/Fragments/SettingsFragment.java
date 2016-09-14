package com.gamesterz.antivirus.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.gamesterz.antivirus.R;

/**
 * Created by Mustafa.Gamesterz on 16/05/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fragment_preference);

        final Preference aboutPref = (Preference) findPreference("aboutKey");

        aboutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gamesterz.co/en/"));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().getApplicationContext().startActivity(browserIntent);

                return false;
            }
        });
    }
}
