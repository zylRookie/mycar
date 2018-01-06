package com.example.zyl.dqcar.moudels.bean;

/**
 * Created by yongliangzhao on 2017/9/7.
 */

public class SendPhoneCodeBean {


    /**
     * response : {"verificationCode":"2525","respCode":"000000","failure":null,"templateSMS":{"createDate":20170907203047,"smsId":"f2e0f1144e9a9d6daa45d61b972b7b96"}}
     */

    public ResponseBean response;

    public static class ResponseBean {
        /**
         * verificationCode : 2525
         * respCode : 000000
         * failure : null
         * templateSMS : {"createDate":20170907203047,"smsId":"f2e0f1144e9a9d6daa45d61b972b7b96"}
         */

        public String verificationCode;
        public String respCode;
        public Object failure;
        public TemplateSMSBean templateSMS;

        public static class TemplateSMSBean {
            /**
             * createDate : 20170907203047
             * smsId : f2e0f1144e9a9d6daa45d61b972b7b96
             */

            public long createDate;
            public String smsId;

        }
    }
}
