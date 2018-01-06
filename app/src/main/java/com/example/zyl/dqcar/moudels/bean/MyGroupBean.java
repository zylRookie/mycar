package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/7/10 10:56
 * Description: 类名
 * PackageName: Activity
 * Copyright: 端趣网络
 **/

public class MyGroupBean {

    /**
     * errorCode : 0000
     * errorMsg :
     * imGroupModel : null
     * items : [{"id":"5","createdTime":null,"createdBy":null,"creatorUserId":null,"imgKey":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502865167142&di=31c86f6085a908800419f1b2375f55e2&imgtype=0&src=http%3A%2F%2Fimg07.huishangbao.com%2Ffile%2Fupload%2F201509%2F09%2F15%2F15-19-33-56-335479.jpg","isNameEdit":null,"name":"车汇报--群6","groupNum":4,"isStop":null},{"id":"2","createdTime":null,"createdBy":null,"creatorUserId":null,"imgKey":null,"isNameEdit":null,"name":null,"groupNum":7,"isStop":null},{"id":"687ac9db-bea0-4514-bb7c-27634af6c1fc","createdTime":null,"createdBy":null,"creatorUserId":null,"imgKey":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502865167142&di=31c86f6085a908800419f1b2375f55e2&imgtype=0&src=http%3A%2F%2Fimg07.huishangbao.com%2Ffile%2Fupload%2F201509%2F09%2F15%2F15-19-33-56-335479.jpg","isNameEdit":null,"name":"车汇报--群7","groupNum":15,"isStop":null},{"id":"40c4a49d-263f-4a44-bbfb-0d7904e2ba18","createdTime":null,"createdBy":null,"creatorUserId":null,"imgKey":"http://sjhoutai.oss-cn-hangzhou.aliyuncs.com/upload-file//20170827170514.jpg","isNameEdit":null,"name":"车汇报--群5","groupNum":1,"isStop":null}]
     * total : 0
     */
    public String errorCode;
    public String errorMsg;
    public Object imGroupModel;
    public int total;
    public List<ItemsBean> items;

    public static class ItemsBean {
        /**
         * id : 5
         * createdTime : null
         * createdBy : null
         * creatorUserId : null
         * imgKey : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1502865167142&di=31c86f6085a908800419f1b2375f55e2&imgtype=0&src=http%3A%2F%2Fimg07.huishangbao.com%2Ffile%2Fupload%2F201509%2F09%2F15%2F15-19-33-56-335479.jpg
         * isNameEdit : null
         * name : 车汇报--群6
         * groupNum : 4
         * isStop : null
         */
        public String id;
        public Object createdTime;
        public Object createdBy;
        public Object creatorUserId;
        public String imgKey;
        public Object isNameEdit;
        public String name;
        public String nameStr;
        public String type;
        public int groupNum;
        public Object isStop;

    }
}
