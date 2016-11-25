package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.wj.mobilesafe.R;
import com.wj.mobilesafe.utils.ConstantValue;
import com.wj.mobilesafe.utils.SpUtil;
import com.wj.mobilesafe.view.SettingItemView;

public class Setup2Activity extends Activity{

    private SettingItemView siv_sim_bound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initUI();
    }

    private void initUI() {
        siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);

        String sim_number = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) {
            siv_sim_bound.setCheck(false);
        }else{
            siv_sim_bound.setCheck(true);
        }

        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取原有状态
                boolean isCheck = siv_sim_bound.isCheck();
                //将原有状态取反,并设置给当前条目
                siv_sim_bound.setCheck(!isCheck);
                if (!isCheck) {
                    //存储序列卡号
                    TelephonyManager manager = (TelephonyManager)
                            getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = manager.getSimSerialNumber();
                    SpUtil.putString(getApplicationContext(),
                            ConstantValue.SIM_NUMBER,simSerialNumber);

                }else{
                    //将存储sim卡号的节点从sp中删除
                    SpUtil.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
            }
        });
    }

    //跳转到下一页
    public void nextPage(View view){

        String serialNumber = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(serialNumber)) {
            Toast.makeText(this,"请绑定sim卡",Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(this,Setup3Activity.class);
            startActivity(intent);
            finish();
        }
    }

    //跳转到上一页
    public void previousPage(View view){
        startActivity(new Intent(this,Setup1Activity.class));
        finish();
    }
}
