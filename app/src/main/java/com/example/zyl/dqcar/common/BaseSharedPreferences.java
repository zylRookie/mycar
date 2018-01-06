package com.example.zyl.dqcar.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Author: Zhaoyl
 * Date: 2017/6/27 10:56
 * Description: 全局SharedPreferences
 * PackageName: BaseSharedPreferences
 * Copyright: 端趣网络
 **/

public class BaseSharedPreferences {


    public final static String SHARED_PRE_KEY = "dq_car";


    /**
     * 获取登录软件标识
     *
     * @param context
     * @return
     */
    public static boolean getIsFirst(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getBoolean("IS_FIRST", true);
    }

    /**
     * 设置是否第一次登录软件
     *
     * @param context
     * @param val
     */
    public static void setIsFirst(Context context, boolean val) {
        SharedPreferences.Editor data = context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putBoolean("IS_FIRST", val);
        data.commit();
    }

    public static void setSessionId(@Nullable Context context, String val) {
        SharedPreferences.Editor data = DqCarApplication.context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putString("SessionId", val);
        data.commit();
    }

    public static String getSessionId(@Nullable Context context) {
        SharedPreferences spf = DqCarApplication.context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getString("SessionId", "");
    }

    public static void setId(@Nullable Context context, String val) {
        SharedPreferences.Editor data = context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putString("Id", val);
        data.commit();
    }

    public static String getId(@Nullable Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getString("Id", "");
    }

    public static void setUserName(Context context, String val) {
        SharedPreferences.Editor data = context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putString("UserName", val);
        data.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getString("UserName", "");
    }

    public static void setUserHeadUrl(Context context, String val) {
        SharedPreferences.Editor data = context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putString("UserHeadUrl", val);
        data.commit();
    }

    public static String getUserHeadUrl(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getString("UserHeadUrl", "");
    }

    public static void setPhoneNumber(Context context, String val) {
        SharedPreferences.Editor data = context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putString("PhoneNumber", val);
        data.commit();
    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getString("PhoneNumber", "");
    }

    public static void setClientId(String val) {
        SharedPreferences.Editor data = DqCarApplication.context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putString("ClientId", val);
        data.commit();
    }

    public static String getClientId() {
        SharedPreferences spf = DqCarApplication.context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getString("ClientId", "");
    }

    public static void setNumber(Context context, String val) {
        SharedPreferences.Editor data = context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putString("Number", val);
        data.commit();
    }

    public static String getNumber(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getString("Number", "");
    }

    public static void setRoleType(Context context, String val) {
        SharedPreferences.Editor data = context.getSharedPreferences(SHARED_PRE_KEY, 0).edit();
        data.putString("RoleType", val);
        data.commit();
    }

    public static String getRoleType(Context context) {
        SharedPreferences spf = context.getSharedPreferences(SHARED_PRE_KEY, 0);
        return spf.getString("RoleType", "");
    }

}



