package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wj.mobilesafe.R;
import com.wj.mobilesafe.utils.ConstantValue;
import com.wj.mobilesafe.utils.SpUtil;

public class SetupOverActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setup_over = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setup_over) {
            //密码输入成功，并且4个设置页面完成--->停留在设置完成功能列表页面。
            setContentView(R.layout.activity_setup_over);
        }else{
            //密码输入成功，4个设置页面未完成则跳转到设置页面一。
            Intent intent = new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }
}
