package cn.ucai.leanclouddemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import java.text.DecimalFormat;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    User user = null;
    UserAdapter mAdapter;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        user = User.getCurrentUser(User.class);
        if(user == null){
            finish();
        }
        mListView = (ListView) findViewById(R.id.list);
        findNear();
    }

    private void findNear() {
        AVQuery<User> query = new AVQuery<>("_User");
        query.limit(10);
        query.whereNear(User.USER_POINT, user.getUserPoint());
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> list, AVException e) {
                Log.e("list","e="+e);
                Log.e("list","list="+list);
                if(e==null && list!=null) {
                    if(list.contains(user)){
                        list.remove(user);
                    }
                    List<User> nearList = list;
                    mAdapter = new UserAdapter(ListActivity.this,nearList);
                    mListView.setAdapter(mAdapter);
                }
            }
        });
    }

    private class UserAdapter extends BaseAdapter {

        Context mContext;
        List<User> mUserList;
        public UserAdapter(Context context,List<User> list) {
            mContext = context;
            mUserList = list;
        }

        @Override
        public int getCount() {
            return mUserList.size();
        }

        @Override
        public User getItem(int i) {
            return mUserList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.simple_adapter, null);
                holder.name = (TextView) convertView.findViewById(R.id.tv_username);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            User u = getItem(position);
            if(u!=null){
                holder.name.setText(u.getUsername()+","
                        +getDistance(u.getUserPoint()));
            }
            return convertView;
        }

        String getDistance(AVGeoPoint point){
            if(point!=null) {
                if(point.getLatitude()!=0&&point.getLongitude()!=0) {
                    Double l = user.getUserPoint().distanceInMilesTo(point);
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    return "距离你 " + decimalFormat.format(l) + " km";//format 返回的是字符串
                }
            }
            return "对方暂未开通定位";//format 返回的是字符串
        }
    }

    private static class ViewHolder {
        TextView name;
    }

}
