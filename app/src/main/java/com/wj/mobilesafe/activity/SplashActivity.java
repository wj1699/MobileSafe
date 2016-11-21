package com.wj.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.wj.mobilesafe.R;
import com.wj.mobilesafe.utils.ConstantValue;
import com.wj.mobilesafe.utils.SpUtil;
import com.wj.mobilesafe.utils.StreamUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    protected static final String tag = "SplashActivity";
    private static final int UPDATE_VERSION = 100;
    private static final int ENTER_HOME = 101;
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR = 103;
    private static final int JSON_ERROR = 104;
    private TextView tv_version_name;
    private int mLocalVersionCode;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case UPDATE_VERSION:
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;

                case ENTER_HOME:
                    enterHome();
                    break;

                case URL_ERROR:
                    Toast.makeText(SplashActivity.this, "url异常",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;

                case IO_ERROR:
                    Toast.makeText(SplashActivity.this, "io异常",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;

                case JSON_ERROR:
                    Toast.makeText(SplashActivity.this, "json解析异常",
                            Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
            }
        }
    };
    private String mVersionDes;
    private String mDownloadUrl;
    private RelativeLayout rl_root;

    /**
     * 提示用户更新版本
     */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新设置");
        builder.setIcon(R.drawable.home_tools);
        builder.setMessage(mVersionDes);

        //设置立即更新按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk
                downloadApk();

            }
        });

        //设置稍后更新按钮
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
                dialog.dismiss();
            }
        });

        //取消事件的监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });

        builder.show();
    }

    /**
     * 从服务器下载新版本的apk
     */
    private void downloadApk() {
        //判断sd卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取sd卡的路径
            String path = Environment.getExternalStorageDirectory().getPath()
                    + File.separator + "mobilesafe.apk";

            //使用xUtils发送请求获取apk
            HttpUtils utils = new HttpUtils();
            utils.download(mDownloadUrl, path, new RequestCallBack<File>() {

                //下载成功
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.i(tag, "下载成功.....");
                    File file = responseInfo.result;
                    //提示用户安装更新
                    installApk(file);
                }


                //下载失败
                @Override
                public void onFailure(HttpException e, String s) {
                    Log.i(tag, "下载失败.....");
                }

                //刚刚开始下载
                @Override
                public void onStart() {
                    Log.i(tag, "刚刚开始下载.....");
                    super.onStart();
                }


                //下载中
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.i(tag, "下载中 .....");
                    Log.i(tag, "total： " + total);
                    Log.i(tag, "current: " + current);
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    private void installApk(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        //startActivity(intent);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splsh);

        //初始化Ui
        initUi();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();

    }

    /**
     * 添加淡入的动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setFillAfter(true);
        //alphaAnimation.start();//考虑要让什么来执行这个动画
        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //获取版本名称
        tv_version_name.setText("版本名称：" + getVersionName());
        //获取本地版本号
        mLocalVersionCode = getVersionCode();

        //根据设置页面的更新设置状态，做出相应的跳转
        if (SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,false)) {
            //获取服务器版本号，和本地版本比较。
            checkVersion();
        }else{
            //4秒后跳转到主页面
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);
        }

    }

    /**
     * 检测版本号
     * 联网需要权限，别再被坑了。
     */
    private void checkVersion() {

        //从服务器获取数据，记得要给网络权限
        new Thread() {
            public void run() {

                //初始化msg对象
                Message msg = Message.obtain();

                //记录Splash页面开始时间
                long startTime = System.currentTimeMillis();

                try {
                    URL url = new URL("http://10.0.2.2:8080/updata.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(2000);
                    if (conn.getResponseCode() == 200) {
                        InputStream in = conn.getInputStream();
                        String json = StreamUtil.StreamToString(in);

                        Log.i(tag, json);

                        //解析数据
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        Log.i(tag, versionName);
                        Log.i(tag, mVersionDes);
                        Log.i(tag, versionCode);
                        Log.i(tag, mDownloadUrl);

                        //判断版本号
                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            //弹出对话框，提示用户更新
                            msg.what = UPDATE_VERSION;

                        } else {
                            //跳转到主页面
                            msg.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;

                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;

                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                } finally {
                    //记录Splash页面的结束时间。
                    long endTime = System.currentTimeMillis();
                    //让Splash页面在4秒后才跳转到主页面
                    if (endTime - startTime < 4000) {
                        SystemClock.sleep(4000 - (endTime - startTime));
                    }
                    mHandler.sendMessage(msg);
                }

            }
        }.start();
    }

    /**
     * 获取版本号
     *
     * @return 返回0表示异常
     */
    private int getVersionCode() {

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取版本名称
     *
     * @return 返回null表示有异常
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化ui
     */
    private void initUi() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
    }

}
