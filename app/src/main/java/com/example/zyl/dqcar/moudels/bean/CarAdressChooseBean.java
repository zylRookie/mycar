package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Created by yongliangzhao on 2017/9/8.
 */

public class CarAdressChooseBean {


    public String errorCode;
    public String errorMsg;
    public List<ItemsBeanX> items;


    public static class ItemsBeanX {

        public String name;
        public Object id;
        public List<ItemsBean> items;

        public static class ItemsBean {
            /**
             * name : A
             * id : null
             * items : []
             */

            public String name;
            public Object id;
            public List<?> items;

        }
    }
}
