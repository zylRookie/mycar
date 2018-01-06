package com.example.zyl.dqcar.getui;

import android.content.Context;
import android.util.Log;

import com.example.zyl.dqcar.common.BaseSharedPreferences;
import com.example.zyl.dqcar.common.EventBusConst;
import com.example.zyl.dqcar.utils.EventCenter;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yufuhao on 2017/5/11.
 */

/**
 * 继承 GTIntentService 接收来⾃自个推的消息,
 * 所有消息在线程中回调, 如果注册了了该服务,
 * 则务必要在 AndroidManifest中声明,
 * 否则⽆无法接受消息<br> *
 * onReceiveMessageData 处理理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理理回执 <br>
 */
public class DqIntentService extends GTIntentService {
    public DqIntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskId = msg.getTaskId();
        String messageId = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String clientId = msg.getClientId();
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskId, messageId, 90002);
//        Log.e("AAA", "onReceiveMessageData: ---appid-->" + appid);
//        Log.e("AAA", "onReceiveMessageData: ---taskId-->" + taskId);
//        Log.e("AAA", "onReceiveMessageData: ---messageId-->" + messageId);
//        Log.e("AAA", "onReceiveMessageData: --pkg-->" + pkg);
//        Log.e("AAA", "onReceiveMessageData: ---clientId--->" + clientId);
//        Log.e("AAA", "onReceiveMessageData: ---result--->" + result);
//        Log.e("AAA", "onReceiveMessageData: ---payload[]--->" + payload);
        Log.e("AAA", "onReceiveMessageData: ----------------个推消息----------------------->");
        if (payload == null) {
            Log.e("AAA", "payload=null");
        } else {
            String data = new String(payload);
            Log.e("AAA", "payload:-----接受------> " + data);
            String[] b = data.split("\\|");
            switch (b[0]) {
                case "ImPerMsg"://个聊
                    EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_MSG));
                    break;
                case "ImGroupMsg"://群聊
                    EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_GROUPMSG));
                    break;
                case "msg"://设置
                    EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_SETMSG));
                    break;
                case "RolePush"://禁言
                    EventBus.getDefault().post(new EventCenter(EventBusConst.UPDATA_ROLES));
                    break;
            }
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        BaseSharedPreferences.setClientId(clientid);
        Log.e("AAA", "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.e("AAA", "onReceiveOnlineState: ---online-->" + online);
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.e("AAA", "onReceiveCommandResult: ---cmdMessage-->" + cmdMessage);
    }
}
