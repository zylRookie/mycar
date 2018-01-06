package com.example.zyl.dqcar.moudels.bean;

/**
 * Author: Zhaoyl
 * Date: 2017/7/24 14:56
 * Description: 登录成功
 * PackageName: LoginBean
 * Copyright: 端趣网络
 **/

public class LoginBean {


    /**
     * errorCode : 0000
     * errorMsg :
     * smUserModel : {"id":"1","version":0,"createdTime":"2017-07-24 00:17:05","createdBy":"sys","updatedTime":null,"updatedBy":null,"orgVersion":0,"imgKey":null,"areaS":"浙","name":"123123","pwd":"111111","phone":"151","number":"A","sex":"男","lastLoginTime":"2017-07-24 14:51:11","rihgt":0,"bak":"1223","money":0,"sessionId":"9C3EF7EC7F19449F839FD6812547E42A"}
     */

    public String errorCode;
    public String errorMsg;
    public String returnValue;
    public String cirleImages;
    public SmUserModelBean smUserModel;


    @Override
    public String toString() {
        return "LoginBean{" +
                "errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", returnValue='" + returnValue + '\'' +
                ", smUserModel=" + smUserModel +
                '}';
    }

    public static class SmUserModelBean {
        /**
         * id : 1
         * version : 0
         * createdTime : 2017-07-24 00:17:05
         * createdBy : sys
         * updatedTime : null
         * updatedBy : null
         * orgVersion : 0
         * imgKey : null
         * areaS : 浙
         * name : 123123
         * pwd : 111111
         * phone : 151
         * number : A
         * sex : 男
         * lastLoginTime : 2017-07-24 14:51:11
         * rihgt : 0
         * bak : 1223
         * money : 0
         * sessionId : 9C3EF7EC7F19449F839FD6812547E42A
         */

        public String id;
        public int version;
        public String createdTime;
        public String createdBy;
        public Object updatedTime;
        public Object updatedBy;
        public int orgVersion;
        public String imgKey;
        public String areaS;
        public String name;
        public String pwd;
        public String phone;
        public String number;
        public String sex;
        public String lastLoginTime;
        public int rihgt;
        public int roleType;
        public String roleStr;
        public String bak;
        public double money;
        public String sessionId;


        @Override
        public String toString() {
            return "SmUserModelBean{" +
                    "id='" + id + '\'' +
                    ", version=" + version +
                    ", createdTime='" + createdTime + '\'' +
                    ", createdBy='" + createdBy + '\'' +
                    ", updatedTime=" + updatedTime +
                    ", updatedBy=" + updatedBy +
                    ", orgVersion=" + orgVersion +
                    ", imgKey='" + imgKey + '\'' +
                    ", areaS='" + areaS + '\'' +
                    ", name='" + name + '\'' +
                    ", pwd='" + pwd + '\'' +
                    ", phone='" + phone + '\'' +
                    ", number='" + number + '\'' +
                    ", sex='" + sex + '\'' +
                    ", lastLoginTime='" + lastLoginTime + '\'' +
                    ", rihgt=" + rihgt +
                    ", roleType=" + roleType +
                    ", roleStr='" + roleStr + '\'' +
                    ", bak='" + bak + '\'' +
                    ", money=" + money +
                    ", sessionId='" + sessionId + '\'' +
                    '}';
        }
    }
}
