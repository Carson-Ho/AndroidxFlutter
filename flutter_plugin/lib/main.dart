/**
 *  导入库
 **/
import 'package:flutter/material.dart'; // Material UI组件库
import 'dart:ui';
import 'dart:async';
import 'package:flutter/services.dart'; // 引入后可以使用window对象

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: _buildWidgetForNativeRoute(window.defaultRouteName),
      // Native传来的route = window.defaultRouteName
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
    );
  }
}

// 该方法用于判断原生界面传递过来的路由值，加载不同的页面
Widget _buildWidgetForNativeRoute(String route) {
  switch (route) {
    case 'flutterView': // 当route值为flutterView时显示
      return FlutterContactPage();

    default: // 默认的路由值为 '/'，所以在default情况也需返回页面，否则dart会报错
      return Container(
        child: Center(
            child: Text(
          '路由值 = deafult',
          style: TextStyle(fontSize: 20.0, color: Colors.black),
        )),
        color: Colors.red,
      );
  }
}

class FlutterContactPage extends StatefulWidget {
  @override
  _FlutterContactPageState createState() => _FlutterContactPageState();
}

class _FlutterContactPageState extends State<FlutterContactPage> {
  // 注册对应的MethodChannel
  // 注：要保证channel name、codec与原生层一致
  EventChannel _eventChannelPlugin = EventChannel("EventChannelPlugin");
  StreamSubscription _streamSubscription;

  // 在initState状态下设置监听Native端发送
  @override
  void initState() {
    _streamSubscription = _eventChannelPlugin
        .receiveBroadcastStream() // 对应Native端onListen（）的第一个参数，可不传
        .listen(_onToDart, onError: _onToDartError, onDone: _onDone);
    // 开启监听，并分别传入：
    // _onToDart方法：正常接收到Native数据时调用
    // _onToDartError方法：接收Native数据异常时调用
    // _onDone方法：发送数据完成时调用
    super.initState();
  }

  // Native端发送正常数据回调方法，每一次发送都会调用
  void _onToDart(message) {
    print('正常接收：$message');
  }
  // Native出错时回调方法
  void _onToDartError(error) {
    print('错误接收：$error');
  }
  // 当native发送数据完成时调用的方法
  void _onDone() {
    print("消息传递完毕");
  }

  @override
  Widget build(BuildContext context) {
    // 2. 通知Native端要调用哪个方法
    return Scaffold(
      appBar: AppBar(
        title: Text('Flutter Page'),
      ),
      body: RaisedButton(
        child: Text('begin counting'),
      ),
    );
  }
}
