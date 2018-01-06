package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/8/28 21:56
 * Description: 查找好友
 * PackageName: FindBean
 * Copyright: 端趣网络
 **/

public class FindBean {

    /**
     * errorCode : 0000
     * errorMsg :
     * smUserModel : null
     * items : [{"id":"1","createdTime":"2017/07/24","createdBy":"sys","manageRight":null,"imgKey":"http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170905142210560_head/ylh_shipper_1504592530.jpg","areaS":"浙","name":"晨瑶","pwd":"111111","phone":"18297609581","number":"A","sex":"女","lastLoginTime":"2017-07-24 14:51:11","right":0,"bak":"刚果共和国","money":199.16,"sessionId":null,"goodsNum":0,"carNum":4,"areasConName":"浙A_晨瑶","expiredTime":"2017/09/16"}]
     * total : 1
     */

    public String errorCode;
    public String errorMsg;
    public Object smUserModel;
    public int total;
    public List<ItemsBean> items;

    public static class ItemsBean {
        /**
         * id : 1
         * createdTime : 2017/07/24
         * createdBy : sys
         * manageRight : null
         * imgKey : http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170905142210560_head/ylh_shipper_1504592530.jpg
         * areaS : 浙
         * name : 晨瑶
         * pwd : 111111
         * phone : 18297609581
         * number : A
         * sex : 女
         * lastLoginTime : 2017-07-24 14:51:11
         * right : 0
         * bak : 刚果共和国
         * money : 199.16
         * sessionId : null
         * goodsNum : 0
         * carNum : 4
         * areasConName : 浙A_晨瑶
         * expiredTime : 2017/09/16
         */

        public String id;
        public String createdTime;
        public String createdBy;
        public Object manageRight;
        public String imgKey;
        public String areaS;
        public String name;
        public String pwd;
        public String phone;
        public String number;
        public String sex;
        public String lastLoginTime;
        public int right;
        public String bak;
        public double money;
        public Object sessionId;
        public int goodsNum;
        public int carNum;
        public String areasConName;
        public String expiredTime;

    }
}
