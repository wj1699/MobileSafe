package com.wj.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.wj.mobilesafe.R;
import com.wj.mobilesafe.utils.ConstantValue;
import com.wj.mobilesafe.utils.SpUtil;

public class Setup4Activity extends BaseSetupActivity{

    private CheckBox cb_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initUI();
    }



    private void initUI() {
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //是否选中状态的回显
        boolean open_security = SpUtil.getBoolean(this,
                ConstantValue.OPEN_SECURITY, false);

        //根据状态，显示cb_box后续的显示文字
        cb_box.setChecked(open_security);
        if (open_security) {
            cb_box.setText("安全设置已开启");
        }else{
            cb_box.setText("安全设置已关闭");
        }

        //cb_box的状态监听
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //存储cb_box被点击后的状态
                SpUtil.putBoolean(getApplicationContext(),
                        ConstantValue.OPEN_SECURITY,isChecked);

                //根据状态，修改显示文字
                if (isChecked) {
                    cb_box.setText("安全设置已开启");
                }else{
                    cb_box.setText("安全设置已关闭");
                }
            }
        });
    }

    //跳转到上一个页面
    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(this,Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.previous_in_anim,R.anim.previous_out_anim);
    }

    //跳转到下一个页面
    @Override
    public void showNextPage() {

        boolean open_security = SpUtil.getBoolean(this,
                ConstantValue.OPEN_SECURITY, false);

        if (open_security) {
            Intent intent = new Intent(this,SetupOverActivity.class);
            startActivity(intent);

            finish();
            SpUtil.putBoolean(this, ConstantValue.SETUP_OVER,true);

            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);

        }else{
            Toast.makeText(this,"请开启防盗保护",Toast.LENGTH_SHORT).show();
        }

    }
}
