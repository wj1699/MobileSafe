package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wj.mobilesafe.R;

public class Setup3Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    private void initUI() {
        EditText et_phone_number = (EditText) findViewById(R.id.et_phone_number);

        Button bt_select_contacts = (Button) findViewById(R.id.bt_select_contacts);
        bt_select_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ContactActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    //跳转到下一页
    public void nextPage(View view){
        Intent intent = new Intent(this,Setup4Activity.class);
        startActivity(intent);
        finish();
    }

    //跳转到上一页
    public void previousPage(View view){
        startActivity(new Intent(this,Setup2Activity.class));
        finish();
    }
}
