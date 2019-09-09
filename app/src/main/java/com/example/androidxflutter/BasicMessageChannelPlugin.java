package com.example.androidxflutter;

import android.app.Activity;
import android.widget.Toast;

import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.StringCodec;
import io.flutter.view.FlutterView;

// 此处支持的数据类型是String
public class BasicMessageChannelPlugin implements BasicMessageChannel.MessageHandler<String> {

    private Activity activity;

    private BasicMessageChannel<String> messageChannel;

    static BasicMessageChannelPlugin registerWith(FlutterView flutterView) {
        return new BasicMessageChannelPlugin(flutterView);
    }

    private BasicMessageChannelPlugin(FlutterView flutterView) {
        this.activity = (Activity) flutterView.getContext();
        // 创建BasicMessageChannel需传入FlutterView、channel name和codec
        this.messageChannel = new BasicMessageChannel<String>(flutterView, "BasicMessageChannelPlugin", StringCodec.INSTANCE);
        // 注册处理的Handler
        messageChannel.setMessageHandler(this);
    }

    // 向Flutter发送消息
    void send(String str, BasicMessageChannel.Reply<String> reply) {
        messageChannel.send(str, reply);
    }

    // 接受Flutter的消息
    @Override
    public void onMessage(String s, BasicMessageChannel.Reply<String> reply) {
        // s即为Flutter发送过来的消息
        System.out.println("Native：收到了"+s);

        // 接受到Flutter信息后，采用reply实例返回值到Flutter层
        reply.reply("Native确认了" + s);
    }
}