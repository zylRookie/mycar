package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/8/28 14:56
 * Description: 通讯录列表
 * PackageName: MailMyBean
 * Copyright: 端趣网络
 **/

public class MailMyBean {

    /**
     * errorCode : 0000
     * errorMsg :
     * items : [{"isInGroup":2,"userId":"be867f25-3058-4d02-a65f-cfc2f6849258","userName":"张三38","imgKey":"http://mpic.tiankong.com/ecc/3e3/ecc3e349338dbe58603cf270d9cd7c9c/640.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/watermark,image_cXVhbmppbmcucG5n,t_90,g_ne,x_5,y_5"},{"isInGroup":4,"userId":"27225644-fbe9-4f67-8cd8-da105a8c1700","userName":"王桂德","imgKey":"https://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170827191658648_head/ylh_shipper_1503832618.jpg"},{"isInGroup":2,"userId":"1c8a5ed2-7729-4b0a-8413-4d51f839b9ca","userName":"张三7","imgKey":"http://mpic.tiankong.com/ecc/3e3/ecc3e349338dbe58603cf270d9cd7c9c/640.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/watermark,image_cXVhbmppbmcucG5n,t_90,g_ne,x_5,y_5"},{"isInGroup":6,"userId":"1","userName":"胡晨瑶","imgKey":"https://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170827191448370_head/ylh_shipper_1503832488.jpg"}]
     */

    public String errorCode;
    public String errorMsg;
    public List<ItemsBean> items;


    public static class ItemsBean {
        /**
         * isInGroup : 2
         * userId : be867f25-3058-4d02-a65f-cfc2f6849258
         * userName : 张三38
         * imgKey : http://mpic.tiankong.com/ecc/3e3/ecc3e349338dbe58603cf270d9cd7c9c/640.jpg?x-oss-process=image/resize,m_lfit,h_600,w_600/watermark,image_cXVhbmppbmcucG5n,t_90,g_ne,x_5,y_5
         */

        public int isInGroup;
        public String userId;
        public String userName;
        public String imgKey;

    }
}
