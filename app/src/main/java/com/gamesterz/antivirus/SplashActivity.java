package com.gamesterz.antivirus;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        Thread timerThread = new Thread(){
//            public void run(){
//                try{
//                    sleep(3000);
//                }catch(InterruptedException e){
//                    e.printStackTrace();
//                }finally{
//
//                    showDeleteCachePermissions();
//                }
//            }
//        };
//        timerThread.start();

        showDeleteCachePermissions();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveFromSplash();
    }


    private void saveFromSplash(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
//        tabINdex = viewPager.getCurrentItem();
        Boolean fromSplash = true;
        editor.putBoolean("fromSplash",fromSplash);
        editor.apply();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String permissionNAme = permissions[0];
        switch (permissionNAme){
            case android.Manifest.permission.DELETE_CACHE_FILES:
            case Manifest.permission.CLEAR_APP_CACHE:
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            default:
                finish();

        }
    }


    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
    }


    private void showDeleteCachePermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.DELETE_CACHE_FILES);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.DELETE_CACHE_FILES)) {
                showExplanation("Permission Needed", "Rationale",  android.Manifest.permission.DELETE_CACHE_FILES, 1);
            } else {
                Log.d("ask","ELSE BROOOO");
                requestPermission( android.Manifest.permission.DELETE_CACHE_FILES, 1);
            }
        } else {
            Toast.makeText(this, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }
}
