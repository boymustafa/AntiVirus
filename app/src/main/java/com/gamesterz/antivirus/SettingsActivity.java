package com.gamesterz.antivirus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.gamesterz.antivirus.Fragments.SettingsFragment;

import java.util.List;

/**
 * Created by Mustafa.Gamesterz on 16/05/16.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

        checkValues();
    }


    private void checkValues()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strUserName = sharedPrefs.getString("username", "NA");
        boolean bAppUpdates = sharedPrefs.getBoolean("applicationUpdates",false);
        String downloadType = sharedPrefs.getString("downloadType","1");

        String msg = "Cur Values: ";
        msg += "\n userName = " + strUserName;
        msg += "\n bAppUpdates = " + bAppUpdates;
        msg += "\n downloadType = " + downloadType;

        Toast.makeText(this,msg, Toast.LENGTH_SHORT);
    }
}
