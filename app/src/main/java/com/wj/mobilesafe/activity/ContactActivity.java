package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wj.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactActivity extends Activity {

    private ListView lv_contacts;
    private List<HashMap<String, String>> contactList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mAdapter = new MyAdapter();
            lv_contacts.setAdapter(mAdapter);
        }
    };
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initUI();

        initData();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),
                    R.layout.listview_contact_item, null);

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);

            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("phone"));
            return view;
        }
    }

    /**
     * 读取联系人
     */
    private void initData() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                //获取内容解析者
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(
                        Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"}, null, null, null);

                contactList.clear();

                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    // Log.i(TAG,"id="+id);
                    Cursor indexCursor = contentResolver.query(
                            Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"}, "raw_contact_id = ?",
                            new String[]{id}, null);

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    while (indexCursor.moveToNext()) {
                        String data1 = indexCursor.getString(0);
                        String mimetype = indexCursor.getString(1);

                        if (mimetype.equals("vnd.android.cursor.item/name")) {
                            if (!TextUtils.isEmpty(data1)) {
                                hashMap.put("name", data1);
                            }
                        } else if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
                            if (!TextUtils.isEmpty(data1)) {
                                hashMap.put("phone", data1);
                            }
                        }
                    }

                    indexCursor.close();
                    contactList.add(hashMap);
                }

                cursor.close();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
        lv_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取联系人电话
                HashMap<String, String> hashMap = mAdapter.getItem(position);
                String phone = hashMap.get("phone");

                //联系人电话带回选择联系人页面
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(0,intent);

                finish();
            }
        });
    }
}
