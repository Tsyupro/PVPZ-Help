package com.example.ukrposhtaboxing;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final int DOUBLE_TAP_TIMEOUT = 200;
    private long lastTapTime = 0;

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTapTime < DOUBLE_TAP_TIMEOUT) {
            return false;
        }
        lastTapTime = currentTime;
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }
}
