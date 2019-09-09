package com.example.androidxflutter;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import io.flutter.facade.Flutter;
import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.view.FlutterView;

public class MainActivity extends AppCompatActivity {

    private ViewGroup.LayoutParams layoutParams;
    private Button btn;
    private EventChannelPlugin mEventChannelPlugin;
    private int count;
    private Timer mTimer;
    private TimerTask mTimertask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. 通过Flutter.createView()创建FlutterView组件方式
        FlutterView flutterView = Flutter.createView(this, getLifecycle(), "flutterView");
        // 2. 关联通道
        mEventChannelPlugin = EventChannelPlugin.registerWith(flutterView);

        // 3. 将Flutter视图添加到原生布局中的Fragment中(为了方便显示，此处采用按钮触发形式)
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                addContentView(flutterView, layoutParams); // 将flutter添加到布局中

                // 4. 为了方便展示，采用计时器Timer发送一系列数据到Flutter
                count = 0;
                mTimer = new Timer(true);
                mTimertask = new TimerTask() {
                    public void run() {

                        // 回到主线程后Native发送数据
                        Handler mainHandler1 = new Handler(Looper.getMainLooper());
                        mainHandler1.post(new Runnable() {
                            @Override
                            public void run() {
                                mEventChannelPlugin.send(count++);
                            }
                        });

                        // 数到5时停止
                        while (count == 5) {

                            // 回到主线程后Native停止发送数据
                            Handler mainHandler2 = new Handler(Looper.getMainLooper());
                            mainHandler2.post(new Runnable() {
                                @Override
                                public void run() {
                                    mEventChannelPlugin.cancel();
                                }
                            });
                            // 关闭计时器
                            mTimer.cancel();
                            mTimer = null;
                            mTimertask.cancel();
                            mTimertask = null;
                        }
                    }
                };
                // 开启计时器（发送数据）
                mTimer.schedule(mTimertask, 1, 1000);
            }
        });
    }
}

