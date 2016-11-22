package cn.ucai.leanclouddemo;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.avos.avoscloud.AVOSCloud;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by clawpo on 2016/11/22.
 */

public class MyLeanCloudApp extends Application {
    public LocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"5FAl4q4NdF3OhFy2Vibg7IVa-gzGzoHsz","6QpR4hJnFdLu6QOQDVf9q0t9");
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());

    }
}
