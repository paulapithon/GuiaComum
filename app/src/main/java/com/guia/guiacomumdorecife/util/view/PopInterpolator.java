package com.guia.guiacomumdorecife.util.view;

import android.view.animation.Interpolator;

/**
 * Created by paula on 28/11/17.
 */

public class PopInterpolator implements Interpolator {

    private double mAmplitude = 1;
    private double mFrequency = 10;

    public PopInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }

}
