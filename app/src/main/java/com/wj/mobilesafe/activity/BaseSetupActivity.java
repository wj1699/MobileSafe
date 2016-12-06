package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSetupActivity extends Activity{

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {

                if (e1.getX() - e2.getX() > 0) {
                    showNextPage();
                }

                if (e1.getX() - e2.getX() < 0) {
                    showPreviousPage();
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    protected abstract void showPreviousPage();

    public abstract void showNextPage();

    //跳转到下一页
    public void nextPage(View view){

        showNextPage();
    }

    //跳转到上一页
    public void previousPage(View view){

        showPreviousPage();
    }
}
