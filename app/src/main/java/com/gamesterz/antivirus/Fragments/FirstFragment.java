package com.gamesterz.antivirus.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.cardiomood.android.controls.progress.CircularProgressBar;
import com.filippudak.ProgressPieView.ProgressPieView;
import com.gamesterz.antivirus.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Mustafa.Gamesterz on 13/05/16.
 */
public class FirstFragment extends Fragment {

    Button btnBosst;
    CircularProgressBar circ;


    public FirstFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.first_fragment,container,false);


        initUI(v); //initialization at one place

        btnBosst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(circ.getProgress()<100){
                    circ.setProgress(100,1000);
                }
                else {
                    circ.setProgress(0);

                    final android.os.Handler handler = new android.os.Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            circ.setProgress(100,1000);
                        }
                    },1000);

                    circ.setProgress(100,100);
                }
            }
        });

        return v;
    }


    private void initUI(View v){
        btnBosst = (Button) v.findViewById(R.id.btnBoost);
        circ = (CircularProgressBar) v.findViewById(R.id.circularprogress);
    }

}
