package com.gamesterz.antivirus;

import android.view.animation.Interpolator;

/**
 * Created by Boy Mustafa on 14/06/16.
 */
public class BounceInterpolator implements Interpolator {
    double mAmplitude = 1;
    double mFrequency = 10;

    public BounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
