package com.wj.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wj.mobilesafe.R;
import com.wj.mobilesafe.utils.ConstantValue;
import com.wj.mobilesafe.utils.SpUtil;

public class Setup3Activity extends BaseSetupActivity {

    private EditText et_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    private void initUI() {
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);

        //获取sp中的联系人号码显示到控件
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        et_phone_number.setText(phone);

        Button bt_select_contacts = (Button) findViewById(R.id.bt_select_contacts);
        bt_select_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //联系人页面带回的结果不为空就获取联系人号码，设置给控件
        if (data != null) {
            String phone = data.getStringExtra("phone");
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_phone_number.setText(phone);

            //存储联系人到SP
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //跳转到上一页
    @Override
    protected void showPreviousPage() {
        startActivity(new Intent(this, Setup2Activity.class));
        finish();

        overridePendingTransition(R.anim.previous_in_anim,R.anim.previous_out_anim);
    }

    //跳转到下一页
    @Override
    public void showNextPage() {
        String phone = et_phone_number.getText().toString().trim();

        // String contact_phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();

            //如果是输入的号码则需要保存
            SpUtil.putString(this,ConstantValue.CONTACT_PHONE,phone);

            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);

        }else{
            Toast.makeText(this,"请输入电话号码",Toast.LENGTH_SHORT).show();
        }
    }
}
