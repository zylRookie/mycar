package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/8/29 10:26
 * Description: 好友详细信息
 * PackageName: MailDetailBean
 * Copyright: 端趣网络
 **/

public class MailDetailBean {


    /**
     * errorCode : 0000
     * errorMsg :
     * smUserModel : {"id":"1","createdTime":"2017-07-24","createdBy":"sys","imgKey":"https://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170827191448370_head/ylh_shipper_1503832488.jpg","areaS":"豫-A","name":"胡晨瑶","pwd":"111111","phone":"18297609581","number":null,"sex":"女","lastLoginTime":"2017-07-24 14:51:11","rihgt":1,"bak":"刚果共和国","money":718.5,"sessionId":null,"manageRight":null}
     * cirleImages : ["https://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issuimgd/3_20170827193355692_head/ylh_shipper_1503833635.mp4","https://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issuimgd/3_20170827193335651_head/ylh_shipper_1503833615.mp4","http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issuimgd/3_20170820192652046_head/ylh_shipper_1503228412.jpg"]
     */

    public String errorCode;
    public String errorMsg;
    public SmUserModelBean smUserModel;
    public List<String> cirleImages;

    public static class SmUserModelBean {
        /**
         * id : 1
         * createdTime : 2017-07-24
         * createdBy : sys
         * imgKey : https://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170827191448370_head/ylh_shipper_1503832488.jpg
         * areaS : 豫-A
         * name : 胡晨瑶
         * pwd : 111111
         * phone : 18297609581
         * number : null
         * sex : 女
         * lastLoginTime : 2017-07-24 14:51:11
         * rihgt : 1
         * bak : 刚果共和国
         * money : 718.5
         * sessionId : null
         * manageRight : null
         */

        public String id;
        public String createdTime;
        public String createdBy;
        public String imgKey;
        public String areaS;
        public String name;
        public String pwd;
        public String phone;
        public Object number;
        public String sex;
        public String lastLoginTime;
        public int rihgt;
        public String bak;
        public double money;
        public Object sessionId;
        public Object manageRight;
    }
}
