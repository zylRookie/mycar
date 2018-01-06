package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/7/26 18:19
 * Description: 发布列表
 * PackageName: ReleaseListBean
 * Copyright: 端趣网络
 **/

public class ReleaseListBean {

    public String errorCode;
    public String errorMsg;
    public Object items;
    public int total;
    public List<PtPtoductModelLstBean> ptPtoductModelLst;

    public static class PtPtoductModelLstBean {
        /**
         * id : 9eea0969-a5a3-4f28-aa1e-414ae6ec888a
         * createdTime : 2017-07-27
         * createdBy : null
         * userName : null
         * imgLst : ["kk","ll","ll","ll","ll","ll"]
         * num : 1
         * contentImage1 : ll
         * contentImage2 : ll
         * contentImage3 : ll
         * contentImage4 : ll
         * contentImage5 : ll
         * title : 奔驰
         * state : 2
         * price : 2.2
         * saleType : 3
         * areasConName : nullnull-null
         * phone : null
         * carNorm : s20
         * carBrand :
         * carYear : 2017/07
         * carDisplacement : 1.5l
         * updatedTime : 2017-08-08
         */

        public String id;
        public String createdTime;
        public Object createdBy;
        public String userName;
        public int num;
        public String contentImage1;
        public String contentImage2;
        public String contentImage3;
        public String contentImage4;
        public String contentImage5;
        public String title;
        public int state;
        public double price;
        public String priceStr;
        public int saleType;
        public String areasConName;
        public String phone;
        public String carNorm;
        public String carBrand;
        public String carYear;
        public String carDisplacement;
        public String updatedTime;
        public List<String> imgLst;
    }

}
