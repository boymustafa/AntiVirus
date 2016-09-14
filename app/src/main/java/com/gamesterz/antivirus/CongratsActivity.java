package com.gamesterz.antivirus;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.gamesterz.antivirus.Utilities.FasterAnimationsContainer;

public class CongratsActivity extends AppCompatActivity {
    ImageView ivRocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);
//        ivRocket = (ImageView) findViewById(R.id.ivRocket);
//        ivRocket.setBackgroundResource(R.drawable.rocket);
//        AnimationDrawable animationDrawable = (AnimationDrawable) ivRocket.getBackground();
//        animationDrawable.start();

//        fasterAnimationsContainer = FasterAnimationsContainer.getInstance(ivRocket);
//        fasterAnimationsContainer.addAllFrames(IMAGE_RESOURCES,
//                ANIMATION_INTERVAL);
//        fasterAnimationsContainer.start();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
////        fasterAnimationsContainer.stop();
//    }
}
