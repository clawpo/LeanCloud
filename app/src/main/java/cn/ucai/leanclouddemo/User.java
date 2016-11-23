package cn.ucai.leanclouddemo;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;

/**
 * Created by clawpo on 2016/11/22.
 */

public class User extends AVUser {
    public static final String USER_NICK = "user_nick";
    public static final String USER_SEX = "user_sex";
    public static final String USER_POINT = "user_point";

    public String getUserNick(){
        return getString(USER_NICK);
    }

    public void setUserNick(String nick){
        put(USER_NICK,nick);
    }

    public int getUserSex(){
        return getInt(USER_SEX);
    }

    public void setUserSex(int sex){
        put(USER_SEX,sex);
    }

    public AVGeoPoint getUserPoint(){
        return getAVGeoPoint(USER_POINT);
    }

    public void setUserPoint(AVGeoPoint point){
        put(USER_POINT,point);
    }
}
