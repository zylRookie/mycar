package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Created by yongliangzhao on 2017/9/13.
 */

public class StarCarBean {

    /**
     * errorCode : 0000
     * errorMsg :
     * smAreaModel : null
     * items : [{"id":"10048","createdTime":"2016-03-25","createdBy":"系统","type":3,"code":"0","name":"广汽","areaPreId":"0","enName":"https://sjhoutai.oss-cn-hangzhou.aliyuncs.com/upload-file//20170912115123.png","simpleName":"0"},{"id":"10049","createdTime":"2016-03-25","createdBy":"系统","type":3,"code":"0","name":"东风风神","areaPreId":"0","enName":"https://sjhoutai.oss-cn-hangzhou.aliyuncs.com/upload-file//20170912115123.png","simpleName":"0"}]
     */

    public String errorCode;
    public String errorMsg;
    public Object smAreaModel;
    public List<ItemsBean> items;

    public static class ItemsBean {
        /**
         * id : 10048
         * createdTime : 2016-03-25
         * createdBy : 系统
         * type : 3
         * code : 0
         * name : 广汽
         * areaPreId : 0
         * enName : https://sjhoutai.oss-cn-hangzhou.aliyuncs.com/upload-file//20170912115123.png
         * simpleName : 0
         */

        public String id;
        public String createdTime;
        public String createdBy;
        public int type;
        public String code;
        public String name;
        public String areaPreId;
        public String enName;
        public String simpleName;
    }
}
