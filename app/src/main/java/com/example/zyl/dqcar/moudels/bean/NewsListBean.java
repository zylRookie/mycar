package com.example.zyl.dqcar.moudels.bean;

import java.util.List;

/**
 * Author: Zhaoyl
 * Date: 2017/8/25 3:36
 * Description: 新闻列表
 * PackageName: NewsListBean
 * Copyright: 端趣网络
 **/

public class NewsListBean {

    /**
     * errorCode : 0000
     * errorMsg :
     * smNewsModel : null
     * items : [{"id":"b596298e-488f-4ed6-98b7-13fa14703d2e","createdTime":"2017-08-20","createdBy":null,"imgLst":["http://img06.tooopen.com/images/20160712/tooopen_sy_170083325566.jpg","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379"],"title":"二手车开卖啦1","detail":"卖卖卖卖卖卖卖6666","content":"<p>kkkkkk<\/p><p style=\"text-align: center;\"><span style=\"background-color: rgb(139, 170, 74);\">vvv<\/span><\/p><p>...<\/p>"},{"id":"af108a60-f679-4a02-930a-72f56a41e9de","createdTime":"2017-08-20","createdBy":null,"imgLst":["http://img06.tooopen.com/images/20160712/tooopen_sy_170083325566.jpg","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379"],"title":"22","detail":"2dsd","content":"<p>ssssssssss<\/p>"},{"id":"c1831e53-3325-4527-865f-62eebbf11b27","createdTime":"2017-08-20","createdBy":null,"imgLst":["http://img06.tooopen.com/images/20160712/tooopen_sy_170083325566.jpg","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379"],"title":"车汇宝第一次","detail":"内测--img","content":"<p>testtttttttttt1！<\/p><p>&nbsp; &nbsp; &nbsp; &nbsp; 测试啦吐吐吐吐吐吐吐吐吐<br><\/p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 哦哦哦哦<br><\/p>"}]
     * total : 3
     */

    public String errorCode;
    public String errorMsg;
    public Object smNewsModel;
    public int total;
    public List<ItemsBean> items;

    public static class ItemsBean {
        /**
         * id : b596298e-488f-4ed6-98b7-13fa14703d2e
         * createdTime : 2017-08-20
         * createdBy : null
         * imgLst : ["http://img06.tooopen.com/images/20160712/tooopen_sy_170083325566.jpg","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379","1&st=undefined&cs=967939789%2C509329843&os=2564600308%2C1989046684&simid=3379"]
         * title : 二手车开卖啦1
         * detail : 卖卖卖卖卖卖卖6666
         * content : <p>kkkkkk</p><p style="text-align: center;"><span style="background-color: rgb(139, 170, 74);">vvv</span></p><p>...</p>
         */

        public String id;
        public String createdTime;
        public Object createdBy;
        public String title;
        public String detail;
        public String content;
        public List<String> imgLst;

    }
}
