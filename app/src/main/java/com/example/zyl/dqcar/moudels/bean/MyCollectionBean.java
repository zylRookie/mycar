package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/9/15 16:56
 * Description: 收藏
 * PackageName: MyCollectionBean
 * Copyright: 端趣网络
 **/

public class MyCollectionBean {

    /**
     * errorCode : 0000
     * errorMsg :
     * ptProductCollectModel : null
     * items : [{"id":"5f4c9e73-ed43-40bc-ac74-43c4abd5dd1e","createdTime":"2017-09-15","createdBy":null,"name":"王桂德","imgKey":"https://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170827191658648_head/ylh_shipper_1503832618.jpg","title":"Dddddd","saleType":1,"imgLst":["http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issueImg/3_20170905100835901_head/ylh_shipper_1504577315.jpg","http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issueImg/3_20170905100835938_head/ylh_shipper_1504577315.jpg"],"userId":"27225644-fbe9-4f67-8cd8-da105a8c1700","productId":"72449a0b-9183-4c1a-b11e-a7920e247133"}]
     * total : 6
     */

    public String errorCode;
    public String errorMsg;
    public Object ptProductCollectModel;
    public int total;
    public List<ItemsBean> items;

    public static class ItemsBean {
        /**
         * id : 5f4c9e73-ed43-40bc-ac74-43c4abd5dd1e
         * createdTime : 2017-09-15
         * createdBy : null
         * name : 王桂德
         * imgKey : https://sj123data.oss-cn-hangzhou.aliyuncs.com/app/brandImg/3_20170827191658648_head/ylh_shipper_1503832618.jpg
         * title : Dddddd
         * saleType : 1
         * imgLst : ["http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issueImg/3_20170905100835901_head/ylh_shipper_1504577315.jpg","http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issueImg/3_20170905100835938_head/ylh_shipper_1504577315.jpg"]
         * userId : 27225644-fbe9-4f67-8cd8-da105a8c1700
         * productId : 72449a0b-9183-4c1a-b11e-a7920e247133
         */

        public String id;
        public String createdTime;
        public Object createdBy;
        public String name;
        public String imgKey;
        public String title;
        public int saleType;
        public String userId;
        public String productId;
        public List<String> imgLst;

    }
}
