package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Md5Util;
import com.wj.mobilesafe.R;
import com.wj.mobilesafe.utils.ConstantValue;
import com.wj.mobilesafe.utils.SpUtil;

public class HomeActivity extends Activity {

    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mDrawableIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);

        initUi();
        initData();
    }

    private void initData() {
        mTitleStr = new String[]{"手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计",
                "手机杀毒", "缓存清理", "高级工具", "设置中心"};

        mDrawableIds = new int[]{R.drawable.home_safe, R.drawable.home_callmsgsafe,
                R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools,
                R.drawable.home_settings};

        gv_home.setAdapter(new MyAdapter());

        //给九宫格的条目设置点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                switch (position) {
                    case 0:
                        //开启对话框
                        showDialog();
                        break;
                    case 8:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        break;
                }
            }
        });

    }

    private void showDialog() {
        //判断本地是否有存储密码
        String pwd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PWD, "");
        if (TextUtils.isEmpty(pwd)) {
            //弹出设置密码的对话框
            showSetPwdDialog();

        } else {
            //弹出设置密码的对话框
            showConfirmDialog();

        }
    }

    /**
     * 确认密码对话框
     */
    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //创建一个view布局
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_pwd, null);
        dialog.setView(view);

        //记得show出来
        dialog.show();

        //找到按钮
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        //点击确定按钮
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找到edit控件
                EditText et_confirm_pwd = (EditText) view.
                        findViewById(R.id.et_confirm_pwd);

                //获取用户输入信息
                String confirmPwd = et_confirm_pwd.getText().toString().trim();

                //判断输入信息
                if (!TextUtils.isEmpty(confirmPwd)) {

                    String pwd = SpUtil.getString(getApplicationContext(),
                            ConstantValue.MOBILE_SAFE_PWD, "");

                    if (pwd.equals(Md5Util.encoder(confirmPwd))) {
                        //进入手机防盗页面
                        startActivity(new Intent(getApplicationContext(),TextActivity.class));
                        dialog.dismiss();


                    }else{
                        Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击取消按钮
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 设置密码对话框
     */
    private void showSetPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //创建一个view布局
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_pwd, null);
        dialog.setView(view);

        //记得show出来
        dialog.show();

        //找到按钮
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        //点击确定按钮
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //找到edit控件
                EditText et_set_pwd = (EditText) view.findViewById(R.id.et_set_pwd);
                EditText et_confirm_pwd = (EditText) view.
                        findViewById(R.id.et_confirm_pwd);

                //获取用户输入信息
                String pwd = et_set_pwd.getText().toString().trim();
                String confirmPwd = et_confirm_pwd.getText().toString().trim();

                //判断输入信息
                if (!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(confirmPwd)) {
                    if (pwd.equals(confirmPwd)) {
                        //进入手机防盗页面
                        startActivity(new Intent(getApplicationContext(),TextActivity.class));
                        dialog.dismiss();

                        SpUtil.putString(getApplicationContext(),ConstantValue.
                                MOBILE_SAFE_PWD, Md5Util.encoder(pwd));

                    }else{
                        Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"用户名和密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击取消按钮
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    private void initUi() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

            tv_title.setText(mTitleStr[position]);
            iv_icon.setBackgroundResource(mDrawableIds[position]);
            return view;
        }
    }
}
