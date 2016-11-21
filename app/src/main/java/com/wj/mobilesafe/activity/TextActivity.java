package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TextActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText("TextActivity");
        setContentView(view);
    }
}
