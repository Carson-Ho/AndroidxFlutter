package com.example.androidxflutter;

import android.app.Activity;

import io.flutter.plugin.common.EventChannel;
import io.flutter.view.FlutterView;

public class EventChannelPlugin implements EventChannel.StreamHandler {

    private EventChannel.EventSink eventSink;
    private Activity activity;

    // 1. 创建 & 注册EventChannel
    static EventChannelPlugin registerWith(FlutterView flutterView) {
        EventChannel channel = new EventChannel(flutterView, "EventChannelPlugin");
        EventChannelPlugin plugin = new EventChannelPlugin(flutterView);
        channel.setStreamHandler(plugin);//设置对应Handler
        return plugin;
    }

    private EventChannelPlugin(FlutterView flutterView) {
        this.activity = (Activity) flutterView.getContext();
    }

    // Native端开始发送数据
    void send(Object params) {
        if (eventSink != null) {
            eventSink.success(params);
            System.out.println("sink success");
        }
    }
    // Native端停止发送数据
    void cancel() {
        if (eventSink != null) {
            eventSink.endOfStream();
        }
    }

    // Native端发送数据失败
    void sendError(String str1, String str2, Object params) {
        if (eventSink != null) {
            eventSink.error(str1, str2, params);
        }
    }

    // 回调时机：Flutter端开始监听该channel时
    // 说明通道已经建立好，Native可以开始发送数据了
    // 参数1 = Flutter端初始化EventChannel时返回的值，仅此一次
    // 参数2 = 传数据的载体
    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        this.eventSink = eventSink; //此处注意时序，必须得该方法回调后，Native端才允许发送数据
        System.out.println( "onListen()：eventSink = " + eventSink);
    }

    // Flutter端不再接收数据时回调
    @Override
    public void onCancel(Object o) {
        System.out.println("onCancel()");
        this.eventSink = null;
    }
}