package com.example.zyl.dqcar.moudels.bean;

/**
 * Created by yongliangzhao on 2017/9/20.
 */

public class OnCreateGroupBean {

    /**
     * errorCode : 0000
     * errorMsg :
     * imGroupModel : {"id":"5","createdTime":null,"createdBy":null,"roles":1,"type":"2","creatorUserId":"1","imgKey":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502865167142&di=31c86f6085a908800419f1b2375f55e2&imgtype=0&src=http%3A%2F%2Fimg07.huishangbao.com%2Ffile%2Fupload%2F201509%2F09%2F15%2F15-19-33-56-335479.jpg","isNameEdit":true,"name":"1号大厅","nameStr":"官方活动_1号大厅","groupNum":0,"isStop":false}
     * items : null
     * total : 0
     */

    public String errorCode;
    public String errorMsg;
    public ImGroupModelBean imGroupModel;
    public Object items;
    public int total;

    public static class ImGroupModelBean {
        /**
         * id : 5
         * createdTime : null
         * createdBy : null
         * roles : 1
         * type : 2
         * creatorUserId : 1
         * imgKey : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502865167142&di=31c86f6085a908800419f1b2375f55e2&imgtype=0&src=http%3A%2F%2Fimg07.huishangbao.com%2Ffile%2Fupload%2F201509%2F09%2F15%2F15-19-33-56-335479.jpg
         * isNameEdit : true
         * name : 1号大厅
         * nameStr : 官方活动_1号大厅
         * groupNum : 0
         * isStop : false
         */

        public String id;
        public Object createdTime;
        public Object createdBy;
        public int roles;
        public String type;
        public String creatorUserId;
        public String imgKey;
        public boolean isNameEdit;
        public String name;
        public String nameStr;
        public int groupNum;
        public boolean isStop;

    }
}
