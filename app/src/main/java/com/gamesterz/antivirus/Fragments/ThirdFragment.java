package com.gamesterz.antivirus.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.cardiomood.android.controls.progress.CircularProgressBar;
import com.gamesterz.antivirus.AppTaskActivity;
import com.gamesterz.antivirus.BounceInterpolator;
import com.gamesterz.antivirus.R;
import com.gamesterz.antivirus.ScanningActivity;

/**
 * Created by Mustafa.Gamesterz on 13/05/16.
 */
public class ThirdFragment extends Fragment {


    private Button btnRepair;
    private CircularProgressBar circ;

    public ThirdFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.third_fragment,container,false);

        initUI(v);

        btnRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation bouceAnim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.bounce);

                bouceAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent myIntent = new Intent(getActivity(), AppTaskActivity.class);
                        getActivity().startActivity(myIntent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
                bouceAnim.setInterpolator(interpolator);

                btnRepair.startAnimation(bouceAnim);
            }
        });

        return v;
    }

    private void initUI(View v){
        btnRepair = (Button) v.findViewById(R.id.btnRepair);
        circ = (CircularProgressBar) v.findViewById(R.id.circularprogress);
    }
}
