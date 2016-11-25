package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wj.mobilesafe.R;
import com.wj.mobilesafe.utils.ConstantValue;
import com.wj.mobilesafe.utils.SpUtil;

public class Setup4Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
    }

    //跳转到下一个页面
   public void nextPage(View view){
       Intent intent = new Intent(this,SetupOverActivity.class);
       startActivity(intent);
       finish();
       SpUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);
   }

    //跳转到上一个页面
    public void previousPage(View view){
        startActivity(new Intent(this,Setup3Activity.class));
        finish();
    }
}
