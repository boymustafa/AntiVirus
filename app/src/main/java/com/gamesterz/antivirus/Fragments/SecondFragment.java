package com.gamesterz.antivirus.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cardiomood.android.controls.progress.CircularProgressBar;
import com.gamesterz.antivirus.R;
import com.gamesterz.antivirus.ScanningActivity;
import com.gamesterz.antivirus.Utilities.Utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Mustafa.Gamesterz on 13/05/16.
 */
public class SecondFragment extends Fragment implements View.OnClickListener{

    private Button btnScan;
    private CircularProgressBar circ;
    private TextView freeSpaceText,occupiedSpaceText;
    private ProgressBar progressBar;

    public SecondFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.second_fragment,container,false);
        initUI(v);
        findMemorySpace();


        return v;
    }

    //initialization at one place
    private void initUI(View v){
        v.findViewById(R.id.btnScan).setOnClickListener(this);
        circ = (CircularProgressBar) v.findViewById(R.id.circularprogress);
        occupiedSpaceText = (TextView) v.findViewById(R.id.occupiedSpace);
        freeSpaceText = (TextView) v.findViewById(R.id.freeSpace);
        progressBar = (ProgressBar) v.findViewById(R.id.indicator);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnScan:
//                if(circ.getProgress()<100)
//                    circ.setProgress(100,1000);
//                else {
//                    circ.setProgress(0);
//
//                    final android.os.Handler handler = new android.os.Handler();
//
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            circ.setProgress(100,1000);
//                        }
//                    },1000);
//
//                    circ.setProgress(100,100);
//                }
                Intent myIntent = new Intent(getActivity(), ScanningActivity.class);
                getActivity().startActivity(myIntent);

                break;
            default:
                break;

        }
    }

    private void findMemorySpace(){
//        Utils.DeviceMemory deviceMemory = new Utils.DeviceMemory();
        final float totalSpace = Utils.DeviceMemory.getInternalStorageSpace();
        final float occupiedSpace = Utils.DeviceMemory.getInternalUsedSpace();
        final float freeSpace = Utils.DeviceMemory.getInternalFreeSpace();

        final DecimalFormat outputFormat = new DecimalFormat("#.##");

        Log.d("NOTE2SPACE","total = "+totalSpace+" used = "+occupiedSpace+" freespace = "+freeSpace);

        if(null != occupiedSpaceText){
//            occupiedSpaceText.setText(Utils.humanReadableByteCount(totalSpace,true)+" Used");
            occupiedSpaceText.setText(outputFormat.format(occupiedSpace) + " MB Used");
        }

        if(null != freeSpaceText){
            freeSpaceText.setText(outputFormat.format(freeSpace) + " MB Available");
//            freeSpaceText.setText(Utils.humanReadableByteCount(freeSpace,true)+" Available");
        }

        if(null != progressBar){
            progressBar.setMax((int) totalSpace);
            progressBar.setProgress((int)occupiedSpace);
        }
    }
}
