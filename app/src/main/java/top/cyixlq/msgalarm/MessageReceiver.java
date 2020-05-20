package top.cyixlq.msgalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import top.cyixlq.msgalarm.bean.Msg;
import top.cyixlq.msgalarm.bus.Bus;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1、接收短信协议
        Bundle bundle = intent.getExtras();
        // 2.通过Bundle取值 ，短信以pdu形式存储数据，s表示很多的意思，以键值对的形式保存数据
        Object[] objs = (Object[]) bundle.get("pdus");
        for (Object object : objs) {
            // 3.获得短信对象
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
            final String pusher = sms.getOriginatingAddress();
            final String content = sms.getDisplayMessageBody();
            Msg msg = new Msg();
            msg.setContent(content);
            msg.setPusher(pusher);
            // 4.获得短信对象后进行输出
            final int type = Config.getType();
            switch (type) {
                case Config.TYPE_AS_PHONE:
                    if (Config.getPhone() == null) {
                        showToast(context, "您设置了仅根据电话号码监听，但是您却未设置要监听的电话号码！");
                    } else {
                        if (pusher == null) {
                            showToast(context, "程序似乎有点错误：未监听到发送者的号码！");
                        } else if (pusher.contains(Config.getPhone())) {
                            Bus.INSTANCE.with(Config.TAG).post(msg);
                        }
                    }
                    break;
                case Config.TYPE_AS_CONTENT:
                    if (Config.getKeyword() == null) {
                        showToast(context, "您设置了仅根据内容关键字监听，但是您却未设置要监听的内容关键字！");
                    } else {
                        if (content == null) {
                            showToast(context, "程序似乎有点错误：未监听到短信的内容！");
                        } else if (content.contains(Config.getKeyword())) {
                            Bus.INSTANCE.with(Config.TAG).post(msg);
                        }
                    }
                    break;
                case Config.TYPE_AS_PHONE_AND_CONTENT:
                    if (Config.getKeyword() == null || Config.getPhone() == null) {
                        showToast(context, "您设置了根据电话号码和内容关键字同时符合的方式进行监听，但是您却未设置要监听的电话号码或者内容关键字！");
                    } else {
                        if (content == null) {
                            showToast(context, "2程序似乎有点错误：未监听到短信的内容！");
                        } else if (pusher == null) {
                            showToast(context, "2程序似乎有点错误：未监听到发送者的号码！");
                        } else if (pusher.contains(Config.getPhone()) && content.contains(Config.getKeyword())) {
                            Bus.INSTANCE.with(Config.TAG).post(msg);
                        }
                    }
                    break;
            }
        }
    }

    private void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
