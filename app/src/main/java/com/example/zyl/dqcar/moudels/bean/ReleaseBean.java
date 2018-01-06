package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/7/26 4:56
 * Description: 发布
 * PackageName: ReleaseBean
 * Copyright: 端趣网络
 **/

public class ReleaseBean {

    /**
     * errorCode : 0000
     * errorMsg :
     * ptPtoductModel : {"id":"72449a0b-9183-4c1a-b11e-a7920e247133","createdTime":"2017-09-05","createdBy":null,"shareUrl":null,"imgKey":null,"updatedTime":"2017-09-11","areasConName":"nullnull_null","userId":"27225644-fbe9-4f67-8cd8-da105a8c1700","imgLst":["http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issueImg/3_20170905100835901_head/ylh_shipper_1504577315.jpg","http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issueImg/3_20170905100835938_head/ylh_shipper_1504577315.jpg"],"title":"Dddddd","carNorm":"Ddddd","carYear":"2017-07-26","carDisplacement":"Ffff","state":2,"priceStr":"1111.0元","price":1111,"num":1,"aNum":1,"detial":"11","userName":"王桂德","userNum":0,"saleType":1}
     */

    public String errorCode;
    public String errorMsg;
    public PtPtoductModelBean ptPtoductModel;

    public static class PtPtoductModelBean {
        /**
         * id : 72449a0b-9183-4c1a-b11e-a7920e247133
         * createdTime : 2017-09-05
         * createdBy : null
         * shareUrl : null
         * imgKey : null
         * updatedTime : 2017-09-11
         * areasConName : nullnull_null
         * userId : 27225644-fbe9-4f67-8cd8-da105a8c1700
         * imgLst : ["http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issueImg/3_20170905100835901_head/ylh_shipper_1504577315.jpg","http://sj123data.oss-cn-hangzhou.aliyuncs.com/app/issueImg/3_20170905100835938_head/ylh_shipper_1504577315.jpg"]
         * title : Dddddd
         * carNorm : Ddddd
         * carYear : 2017-07-26
         * carDisplacement : Ffff
         * state : 2
         * priceStr : 1111.0元
         * price : 1111
         * num : 1
         * aNum : 1
         * detial : 11
         * userName : 王桂德
         * userNum : 0
         * saleType : 1
         */

        public String id;
        public String createdTime;
        public Object createdBy;
        public Object shareUrl;
        public Object imgKey;
        public String updatedTime;
        public String areasConName;
        public String userId;
        public String title;
        public String carNorm;
        public String carYear;
        public String carDisplacement;
        public int state;
        public String priceStr;
        public int price;
        public int num;
        public int aNum;
        public String detial;
        public String userName;
        public String carBrand;
        public int userNum;
        public int saleType;
        public List<String> imgLst;

    }
}
