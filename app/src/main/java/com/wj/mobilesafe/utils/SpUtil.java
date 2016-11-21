package com.wj.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {

    private static SharedPreferences sp;

    /**
     * 写入boolean变量至sp中
     * @param context 上下文
     * @param key 存储节点的名称
     * @param value 存储节点的值
     */
    public static void putBoolean(Context context, String key, boolean value){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     *从sp中读取boolean标识
     * @param context 上下文
     * @param key 存储的节点
     * @param defValue 没有节点的默认值
     * @return 默认值或者读取到的结果
     */
    public  static boolean getBoolean(Context context,String key,boolean defValue){
        if (sp==null) {
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }


    /**
     * 写入boolean变量至sp中
     * @param context 上下文
     * @param key 存储节点的名称
     * @param value 存储节点的值
     */
    public static void putString(Context context, String key, String value){
        if (sp==null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }

    /**
     *从sp中读取boolean标识
     * @param context 上下文
     * @param key 存储的节点
     * @param defValue 没有节点的默认值
     * @return 默认值或者读取到的结果
     */
    public  static String getString(Context context,String key,String defValue){
        if (sp==null) {
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue);
    }
}
