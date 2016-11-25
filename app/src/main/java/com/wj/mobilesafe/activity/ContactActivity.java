package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.wj.mobilesafe.R;

import static android.content.ContentValues.TAG;

public class ContactActivity extends Activity{

    private ListView lv_contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initUI();

        initData();
    }

    /**
     * 读取联系人
     */
    private void initData() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                //1.获取内容解析者
                ContentResolver resolver = getContentResolver();
                //2.查询联系人（注意：读取联系人的权限，耗时的操作要放在子线程完成）
                Cursor cursor = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"}, null, null, null);
                if (cursor!=null) {
                    while (cursor.moveToNext()) {
                        String id = cursor.getString(0);
                        //Log.i(TAG,"id= "+id);
                        Cursor indexCursor = resolver.query(Uri.parse("content://com.android.contacts/data"),
                                new String[]{"data1", "mimetype"}, "raw_contact_id = ?",
                                new String[]{id}, null);
                        while (indexCursor.moveToNext()) {
                            String data1 = indexCursor.getString(0);
                            String mimetype = indexCursor.getString(1);

                            Log.i(TAG,"data1:"+data1);
                            Log.i(TAG,"mimetype"+mimetype);
                            // TODO: 2016/11/25 保存数据到集合
                        }

                        indexCursor.close();
                    }
                    cursor.close();
                }

            }
        }.start();
    }

    private void initUI() {
        lv_contacts = (ListView) findViewById(R.id.lv_contacts);
    }
}
