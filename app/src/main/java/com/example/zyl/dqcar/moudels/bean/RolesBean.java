package com.example.zyl.dqcar.moudels.bean;

/**
 * Created by yongliangzhao on 2017/9/21.
 */

public class RolesBean {

    /**
     * total : 0
     * errorCode : 0000
     * errorMsg :
     * imGroupModel : {"id":"96bab1f8-e1a0-4a08-b3a4-c40a0f9bc05e","creatorUserId":"27225644-fbe9-4f67-8cd8-da105a8c1700","imgKey":"http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/titleImg/3_20170920165304982_head/ylh_shipper_1505897584.jpg","createdBy":null,"createdTime":null,"nameStr":"官方活动_林俊杰","isStop":false,"name":"林俊杰","roles":0,"groupNum":0,"type":"2","isNameEdit":false}
     * items : null
     */

    public int total;
    public String errorCode;
    public String errorMsg;
    public ImGroupModelBean imGroupModel;
    public Object items;


    public static class ImGroupModelBean {
        /**
         * id : 96bab1f8-e1a0-4a08-b3a4-c40a0f9bc05e
         * creatorUserId : 27225644-fbe9-4f67-8cd8-da105a8c1700
         * imgKey : http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/titleImg/3_20170920165304982_head/ylh_shipper_1505897584.jpg
         * createdBy : null
         * createdTime : null
         * nameStr : 官方活动_林俊杰
         * isStop : false
         * name : 林俊杰
         * roles : 0
         * groupNum : 0
         * type : 2
         * isNameEdit : false
         */

        public String id;
        public String creatorUserId;
        public String imgKey;
        public Object createdBy;
        public Object createdTime;
        public String nameStr;
        public boolean isStop;
        public String name;
        public int roles;
        public int groupNum;
        public String type;
        public boolean isNameEdit;

    }
}
