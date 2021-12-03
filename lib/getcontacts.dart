
import 'dart:async';

import 'package:flutter/services.dart';

class Getcontacts {
  static const MethodChannel _channel = MethodChannel('getcontactsAmit');
  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<List<dynamic>?> get getCList async {
    print(await _channel.invokeMethod('getContactList'));
    List<dynamic>? mlist = await _channel.invokeMethod('getContactList');
    return mlist;
  }
}
