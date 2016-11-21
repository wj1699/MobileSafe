package com.wj.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wj.mobilesafe.R;


public class SettingItemView extends RelativeLayout {

    public static final String NAMESPACE = "http://schemas.android.com/apk/res/com.wj.mobilesafe";
    private CheckBox cb_box;
    private TextView tv_des;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_item_view, this);

        //自定义组合控件的标题描述
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        //获取自定义属性
        initAttrs(attrs);
        //设置标题
        tv_title.setText(mDestitle);
    }

    private void initAttrs(AttributeSet attrs) {
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
    }

    /**
     * 判断是否开启的方法
     *
     * @return 返回当前SettingItemView是否选中状态
     */
    public boolean isCheck() {
        return cb_box.isChecked();
    }

    /**
     * @param isCheck 是否开启的变量，由点击过程中做传递
     */
    public void setCheck(boolean isCheck) {
        cb_box.setChecked(isCheck);
        if (isCheck) {
            tv_des.setText(mDeson);
        }else{
            tv_des.setText(mDesoff);
        }
    }

}
