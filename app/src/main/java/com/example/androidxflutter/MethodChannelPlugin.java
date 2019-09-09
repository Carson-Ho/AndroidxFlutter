package com.example.androidxflutter;

import android.app.Activity;
import android.widget.Toast;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.FlutterView;

public class MethodChannelPlugin implements MethodChannel.MethodCallHandler {

    private Activity activity;
    private MethodChannel channel;

    // 1. 创建MethodChannel实例（传入channel name）
    public static MethodChannelPlugin registerWith(FlutterView flutterView) {
        MethodChannel channel = new MethodChannel(flutterView, "MethodChannelPlugin");
        MethodChannelPlugin methodChannelPlugin = new MethodChannelPlugin((Activity) flutterView.getContext(), channel);
        channel.setMethodCallHandler(methodChannelPlugin);// 注册处理的Handler
        return methodChannelPlugin;
    }

    private MethodChannelPlugin(Activity activity, MethodChannel channel) {
        this.activity = activity;
        this.channel = channel;

    }
    // 2. 函数1：用于调用Flutter端方法，无返回值
    // method为需调用的方法名
    public void invokeMethod(String method, Object o) {
        channel.invokeMethod(method, o);
    }

    // 3. 函数2：用于调用Flutter端方法，有返回值
    // method为需调用的方法名、返回值在result内
    public void invokeMethod(String method, Object o, MethodChannel.Result result) {
        channel.invokeMethod(method, o, result);
    }

    // 4. 复写onMethodCall（）：根据Flutter的要求，调用Native方法
    @Override
    public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        switch (methodCall.method) {
            case "FlutterInvokeFlutter":// Flutter要求Native调用的方法是FlutterInvokeFlutter

                System.out.println("Native收到了Flutter的请求方式是："+methodCall.method);
                System.out.println("Native收到了Flutter的请求参数是："+methodCall.arguments);
                result.success("Native收到了Flutter的请求方法：" + methodCall.method);// 给flutter端的返回值
                break;
            default:
                result.notImplemented();
                break;
        }
    }
}

