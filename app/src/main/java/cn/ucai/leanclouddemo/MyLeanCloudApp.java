package cn.ucai.leanclouddemo;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * Created by clawpo on 2016/11/22.
 */

public class MyLeanCloudApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"5FAl4q4NdF3OhFy2Vibg7IVa-gzGzoHsz","6QpR4hJnFdLu6QOQDVf9q0t9");
    }
}
