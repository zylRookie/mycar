package com.example.zyl.dqcar.http;

/**
 * Author: Zhaoyl
 * Date: 2017/8/27 10:56
 * Description: api
 * PackageName: WebAPI
 * Copyright: 端趣网络
 **/
public interface WebAPI {

    //--------------------------------测试--开发----------------------------------------------------------------------------------------------------------------------------------------------------------------


    //测试环境

    //    String HOST = "http://114.55.85.197:8091/SJ/";
//    String HOST = "http://192.168.0.191:8080/SJ/";
    String HOST = "http://116.62.228.171:8090/SJ/";
//    String HOST = "http://pay.willowspace.cn/";


    interface Login {
        String LOGIN = HOST + "login/loginIn";//登录
        String LOGIN_LOGINBYWX = HOST + "login/loginByWX";//微信登录
        String LOGIN_OUT = HOST + "login/loginOut";//退出登录
        String LOGIN_REGISTER = HOST + "login/reg";//注册
        String LOGIN_GOUSERDETAIL = HOST + "user/modifyOther";//完善个人信息
        String LOGIN_RESETPWD = HOST + "login/resetPwd";//重置密码
        String LOGIN_SENDMSG = HOST + "msgbind/sendMsg";//个推绑定手机
        String LOGIN_FINDCARAREALIST = HOST + "user/findCarAreaList";//获取个人所在地
        String LOGIN_SENCODE = HOST + "sms/send/signUpCode";//注册发送验证码
        String LOGIN_RESETPASSWORDCODE = HOST + "sms/send/resetPasswordCode";//重置密码发送验证码
        String LOGIN_PHONEBIND = HOST + "sms/send/phoneBind";//绑定手机发送验证码
        String LOGIN_WECHABIND = HOST + "login/wechaBind";//绑定手机
        String LOGIN_MODIFYPWD = HOST + "user/modifyPwd";//修改密码
    }

    interface MsgApi {
        String MINE_FINDBYGOODS = HOST + "product/findByGoods";//查找闲置商品列表
        String MINE_FINDGOODSBYUSERID = HOST + "product/findGoodsByUserId";//查找个人闲置商品列表
        String MINE_FINDBYCAR = HOST + "product/findByCar";//查找二手车列表
        String MINE_FINDCARSBYUSERID = HOST + "product/findCarsByUserId";//查找个人二手车列表
        String MINE_FINDALLMYSESSION = HOST + "chatsession/findAllMySession";//会话列表
        String MINE_DELCHATSESSION = HOST + "chatsession/delChatSession";//删除列表
        String MINE_NEWGROUP = HOST + "manage/group/newGroup";//新建群
    }

    interface MailApi {
        String MAILAPI_FINDMYFRIENDS = HOST + "friend/findMyFriends";//通讯录列表
        String MAILAPI_FINDGROUPLISTBYUSERID = HOST + "manage/group/findGroupListByuserId";//群聊列表
        String MAILAPI_FINDGROUPLISTBYTYPE = HOST + "manage/group/findGroupListByType";//九大群
        String MAILAPI_JOINGROUP = HOST + "manage/group/joinGroup";//加入群聊
        //        String MAILAPI_FINDBYPHONE = HOST + "friend/findByPhone";//查找好友
        String MAILAPI_FINDBYNAMEORPHONE = HOST + "manage/user/findByNameOrPhone";//查找好友
        String MAILAPI_ADDFRIEND = HOST + "friend/addFriend";//新增好友
        String MAILAPI_DELETEFRIEND = HOST + "friend/deleteFriend";//删除好友
        String MAILAPI_FINDLISTUSER = HOST + "user/findListUser";//好友的详细信息
    }

    interface FriendsApi {
        String FRIENDSAPI_GETALLCANVISUALCIRCLE = HOST + "circle/getAllCanVisualCircle";//查找可见的车友圈
        String FRIENDSAPI_GETCIRCLEINFO = HOST + "circle/getCircleInfo";//查找指定车友圈信息
        String FRIENDSAPI_LIKECIRCLE = HOST + "circleop/likeCircle";//对该条车友圈点赞
        String FRIENDSAPI_UNLIKECIRCLE = HOST + "circleop/unLikeCircle";//对该条车友圈取消点赞
        String FRIENDSAPI_ADDCOMMENT = HOST + "circlecomment/addComment";//添加评论
        String FRIENDSAPI_DELETECOMMENT = HOST + "circlecomment/deleteComment";//删除该条评论
        String FRIENDSAPI_DELETECIRCLE = HOST + "circle/deleteCircle";//删除该条车友圈
        String FRIENDSAPI_ADDCIRCLE = HOST + "circle/addCircle";//添加新的车友圈

    }

    interface MineApi {
        String MINE_USERDETAIL = HOST + "user/findById";//完善个人信息
        String MINE_EDITUSER = HOST + "user/modifyCenter";//修改个人信息
        String MINE_MYRELEASE = HOST + "product/modifyProduct";//我的发布
        String MINE_MYRELEASELIST = HOST + "product/findByMyCar";//我的发布列表二手车
        String MINE_FINDCARBRANDLIST = HOST + "user/findCarBrandList";//选择品牌
        String MINE_MYRELEASELISTGOODS = HOST + "product/findByMyGoods";//我的发布列表闲置商品
        String MINE_FINDBYPRODUCTID = HOST + "product/findByProductId";//我的发布列表单个商品详情
        String MINE_MODIFYPRODUCTSTATE = HOST + "product/modifyProductState";//我的发布列表上下架
        String MINE_FINDBYPRODUCT = HOST + "product/findByProduct";//我的礼品商城列表
        String MINE_GETUSERCIRCLE = HOST + "circle/getUserCircle";//获取我的相册
        String MINE_FINDNEWSLIST = HOST + "news/findNewsList";//获取新闻列表
        String MINE_FINDCOLLECTLIST = HOST + "productCollect/findCollectList";//获取收藏列表
        String MINE_ADDCOLLECT = HOST + "productCollect/addCollect";//添加收藏
        String MINE_DELETECOLLECT = HOST + "productCollect/deleteCollect";//取消收藏
        String MINE_ISCOLLECT = HOST + "productCollect/isCollect";//判断收藏状态
    }

    interface PayApi {
        String PayApi_ = HOST + "news/findNewsList";//支付宝接口
    }

    interface ChatApi {
        String CHATAPI_FINDMSGLOG = HOST + "permsg/findMsgLog";//个聊查询聊天记录
        String CHATAPI_SENDMSG = HOST + "permsg/sendMsg";//个聊发送消息
        String CHATAPI_FECALLMSG = HOST + "permsg/fecallMsg";//撤回消息
        String CHATAPI_GROUPFINDMSGLOG = HOST + "groupMsg/findMsgLog";//群聊查询聊天记录
        String CHATAPI_GROUPSENDMSG = HOST + "groupMsg/sendMsg";//群聊发送消息
        String CHATAPI_FINDGROUPUSERLIST = HOST + "manage/group/findGroupUserList";//群聊添加
        String CHATAPI_ADDGPUSERSFORFRIENDLYENT = HOST + "manage/group/addGpUsersForFriendlyEnt";//添加群成员
        String CHATAPI_DELETEBYGROUPANDUSER = HOST + "manage/group/deleteByGroupAndUser";//删除群成员多个
        String CHATAPI_DELETEBYGROUPANDUSERSINGLE = HOST + "manage/group/deleteByGroupAndUserSingle";//删除群成员
        String CHATAPI_MODIFYROLES = HOST + "manage/group/modifyRoles";//禁言

    }


}