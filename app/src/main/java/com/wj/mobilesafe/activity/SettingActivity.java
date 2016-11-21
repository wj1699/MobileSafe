package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.wj.mobilesafe.R;
import com.wj.mobilesafe.utils.ConstantValue;
import com.wj.mobilesafe.utils.SpUtil;
import com.wj.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);

        //获取已有的开关状态
        boolean open_update = SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        siv_update.setCheck(open_update);

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中的，点击过后变成未选中
                //如果之前是未选中的，点击过后变成选中

                //获取之前的状态
                boolean isCheck = siv_update.isCheck();
                //把状态取反
                siv_update.setCheck(!isCheck);
                //存储状态到sp中
                SpUtil.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isCheck);
            }
        });
    }
}
