package cn.ucai.leanclouddemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int SDK_PERMISSION_REQUEST = 127;
    private ListView FunctionList;
    private String permissionInfo;
    private LocationService locationService;
    Button btnShow;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnShow = (Button) findViewById(R.id.btn_show);
        btnLogout = (Button) findViewById(R.id.btn_logout);

//        // 测试 SDK 是否正常工作的代码
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("words","Hello World!");
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    Log.d("saved","success!");
//                }
//            }
//        });
        // after andrioid m,must request Permiision on runtime
        getPersimmions();
        setListener();
    }

    private void setListener() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logOut();// 清除缓存用户对象
                finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLocation();
            }
        });
    }

    private void initLocation() {
        // -----------location config ------------
        locationService = ((MyLeanCloudApp) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
        locationService.stop();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			/*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                final AVGeoPoint point = new AVGeoPoint(location.getLatitude(),location.getLongitude());
                String updateTime = location.getTime();
                User currentUser = User.getCurrentUser(User.class);
                Log.e("main","currentUser="+currentUser);
                currentUser.setUserPoint(point);
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        Log.e("main","setUserPoint e="+e);
                        if(e==null){
                            Log.e("main","save point success"+point);
                            locationService.stop();
                            startActivity(new Intent(MainActivity.this,ListActivity.class));
                        }
                    }
                });
            }
        }

    };
}
