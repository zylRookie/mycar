package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/8/28 10:56
 * Description: 消息列表
 * PackageName: MsgAllBean
 * Copyright: 端趣网络
 **/

public class MsgAllBean {

    /**
     * errorCode : 0000
     * errorMsg : 操作成功
     * returnValue :
     * items1 : [{"id":"afaaa8de-0812-4e4a-b1ca-a607a923b413","title":"车汇报--群6","isTop":false,"isSilence":null,"content":"胡晨瑶:恭喜发财,大吉大利","logoUrl":"http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170824205121302_head/ylh_shipper_1503579081.jpg","updatedTime":"2017-08-25","sourceType":"ImGroupMsg","sourceId":"5","sourceUserId":"1","userId":"1","unReadCount":0},{"id":"6cc7acb7-bf98-40b7-9d49-91af03ff8d2d","title":"车汇报--群2","isTop":false,"isSilence":null,"content":"胡晨瑶:jjdndj急景凋年大家","logoUrl":"http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170824205121302_head/ylh_shipper_1503579081.jpg","updatedTime":"2017-08-25","sourceType":"ImGroupMsg","sourceId":"2","sourceUserId":"1","userId":"1","unReadCount":0},{"id":"a9b2dadc-2f13-4d58-b27f-a3b7e1f85fe8","title":"群5","isTop":false,"isSilence":null,"content":"胡晨瑶:恭喜发财,大吉大利","logoUrl":"http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170824205121302_head/ylh_shipper_1503579081.jpg","updatedTime":"2017-08-23","sourceType":"ImGroupMsg","sourceId":"687ac9db-bea0-4514-bb7c-27634af6c1fc","sourceUserId":"1","userId":"1","unReadCount":0}]
     * items : [{"id":"e60b31b6-fe03-4e61-99f9-b80226ca1854","title":"王桂德","isTop":false,"isSilence":null,"content":"gghb刚刚好吧","logoUrl":"http://mpic.tiankong.com/ecc/3e3/ecc3e349338dbe58603cf270d9cd7c9c/640.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/watermark,image_cXVhbmppbmcucG5n,t_90,g_ne,x_5,y_5","updatedTime":"2017-08-27","sourceType":"ImPerMsg","sourceId":"27225644-fbe9-4f67-8cd8-da105a8c1700|1","sourceUserId":"27225644-fbe9-4f67-8cd8-da105a8c1700","userId":"1","unReadCount":0},{"id":"c7191e49-9f44-48b5-8236-6a94502398be","title":"张三9","isTop":false,"isSilence":null,"content":"jjnnn九记牛腩呢","logoUrl":"http://mpic.tiankong.com/ecc/3e3/ecc3e349338dbe58603cf270d9cd7c9c/640.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/watermark,image_cXVhbmppbmcucG5n,t_90,g_ne,x_5,y_5","updatedTime":"2017-08-25","sourceType":"ImPerMsg","sourceId":"2|1","sourceUserId":"2","userId":"1","unReadCount":0},{"id":"4d0cc453-b204-40fa-9071-d22cde3dbf9b","title":"胡晨瑶","isTop":false,"isSilence":null,"content":"恭喜发财,大吉大利","logoUrl":"http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170824205121302_head/ylh_shipper_1503579081.jpg","updatedTime":"2017-08-25","sourceType":"ImPerMsg","sourceId":"1|1","sourceUserId":"1","userId":"1","unReadCount":0},{"id":"900b330c-63a7-4943-8435-e2f90d8b0e9f","title":"磊哥","isTop":false,"isSilence":null,"content":"测试红包","logoUrl":"http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170821190207827_head/ylh_shipper_1503313327.jpg","updatedTime":"2017-08-25","sourceType":"ImPerMsg","sourceId":"c546e37b-c22c-494b-b656-b383a102226c|1","sourceUserId":"c546e37b-c22c-494b-b656-b383a102226c","userId":"1","unReadCount":0},{"id":"0c6409de-73ad-4400-a352-f7359181c33c","title":"张三38","isTop":false,"isSilence":null,"content":"djjjd大家积极的","logoUrl":"http://mpic.tiankong.com/ecc/3e3/ecc3e349338dbe58603cf270d9cd7c9c/640.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/watermark,image_cXVhbmppbmcucG5n,t_90,g_ne,x_5,y_5","updatedTime":"2017-08-25","sourceType":"ImPerMsg","sourceId":"be867f25-3058-4d02-a65f-cfc2f6849258|1","sourceUserId":"be867f25-3058-4d02-a65f-cfc2f6849258","userId":"1","unReadCount":0},{"id":"e9c01185-2432-4fea-bdbe-0a329c09656e","title":"娜娜","isTop":false,"isSilence":null,"content":"你好","logoUrl":null,"updatedTime":"2017-08-25","sourceType":"ImPerMsg","sourceId":"fde599ce-b25a-48b8-a489-4ecf7dc65781|1","sourceUserId":"fde599ce-b25a-48b8-a489-4ecf7dc65781","userId":"1","unReadCount":0}]
     */

    public String errorCode;
    public String errorMsg;
    public String returnValue;
    public List<Items1Bean> items1;
    public List<ItemsBean> items;


    public static class Items1Bean {
        /**
         * id : afaaa8de-0812-4e4a-b1ca-a607a923b413
         * title : 车汇报--群6
         * isTop : false
         * isSilence : null
         * content : 胡晨瑶:恭喜发财,大吉大利
         * logoUrl : http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170824205121302_head/ylh_shipper_1503579081.jpg
         * updatedTime : 2017-08-25
         * sourceType : ImGroupMsg
         * sourceId : 5
         * sourceUserId : 1
         * userId : 1
         * unReadCount : 0
         */

        public String id;
        public String title;
        public boolean isTop;
        public Object isSilence;
        public String content;
        public String logoUrl;
        public String updatedTime;
        public String sourceType;
        public String sourceId;
        public String sourceUserId;
        public String userId;
        public int unReadCount;

    }

    public static class ItemsBean {
        /**
         * id : e60b31b6-fe03-4e61-99f9-b80226ca1854
         * title : 王桂德
         * isTop : false
         * isSilence : null
         * content : gghb刚刚好吧
         * logoUrl : http://mpic.tiankong.com/ecc/3e3/ecc3e349338dbe58603cf270d9cd7c9c/640.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/watermark,image_cXVhbmppbmcucG5n,t_90,g_ne,x_5,y_5
         * updatedTime : 2017-08-27
         * sourceType : ImPerMsg
         * sourceId : 27225644-fbe9-4f67-8cd8-da105a8c1700|1
         * sourceUserId : 27225644-fbe9-4f67-8cd8-da105a8c1700
         * userId : 1
         * unReadCount : 0
         */

        public String id;
        public String title;
        public boolean isTop;
        public Object isSilence;
        public String content;
        public String logoUrl;
        public String updatedTime;
        public String sourceType;
        public String sourceId;
        public String sourceUserId;
        public String userId;
        public int unReadCount;

    }
}
