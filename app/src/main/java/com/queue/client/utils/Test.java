package com.queue.client.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class Test {
    void text(){
        ImageView imageView=null;
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
    }
}
