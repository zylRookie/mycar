package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Created by yongliangzhao on 2017/10/9.
 */

public class Try {

    /**
     * msgcode : 1
     * msg : 成功
     * data : [{"id":2745971,"pao_name":"刘炜达","money":132,"Pao_Image":"05dc141889e049d2bf1501d903aa80a1.jpg"},{"id":2746339,"pao_name":"侯开兵","money":124,"Pao_Image":"2b1c127caaf348d6b457b873af1fe4fd.jpg"},{"id":2746527,"pao_name":"梁凤贤","money":104,"Pao_Image":"d4efb7b4d3e942da9e2891314c9d69ac.jpg"},{"id":2746360,"pao_name":"黄百坤","money":78,"Pao_Image":"fbdebc3b0e944e61b7a6e799047660e5.jpg"},{"id":2746550,"pao_name":"周振宏","money":22,"Pao_Image":"662acd40ab0d456bab99e62c6a903ec6.jpg"},{"id":2745918,"pao_name":"马金卫","money":14,"Pao_Image":"47fef472b88446868f2157ac42fc207c.jpg"}]
     */

    private String msgcode;
    private String msg;
    private List<DataBean> data;

    public String getMsgcode() {
        return msgcode;
    }

    public void setMsgcode(String msgcode) {
        this.msgcode = msgcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2745971
         * pao_name : 刘炜达
         * money : 132
         * Pao_Image : 05dc141889e049d2bf1501d903aa80a1.jpg
         */

        private int id;
        private String pao_name;
        private int money;
        private String Pao_Image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPao_name() {
            return pao_name;
        }

        public void setPao_name(String pao_name) {
            this.pao_name = pao_name;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getPao_Image() {
            return Pao_Image;
        }

        public void setPao_Image(String Pao_Image) {
            this.Pao_Image = Pao_Image;
        }
    }
}
