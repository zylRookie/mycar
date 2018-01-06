package com.example.zyl.dqcar.moudels.bean;

/**
 * Author: Zhaoyl
 * Date: 2017/7/28 15:56
 * Description:  基类
 * PackageName: BaseBackBean
 * Copyright: 端趣网络
 **/

public class BaseBackBean {

    /**
     * errorCode : 0000
     * errorMsg : 操作成功
     * returnValue :
     */

    public String errorCode;
    public String errorMsg;
    public String returnValue;

    @Override
    public String toString() {
        return "BaseBackBean{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", returnValue='" + returnValue + '\'' +
                '}';
    }
}
