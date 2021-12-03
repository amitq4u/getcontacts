import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:getcontacts/getcontacts.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  List<dynamic>? mContacts = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    List<dynamic>? cList;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await Getcontacts.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    try {
      cList = await Getcontacts.getCList;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
    setState(() {
      _platformVersion = platformVersion;
      mContacts = cList;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Get Contacts'),
          centerTitle: true,
        ),
        body: ListView(
          children: [
            Center(
              child: Text('Running on: $_platformVersion\n'),
            ),
            ListView.builder(
              shrinkWrap: true,
                itemCount: mContacts!.length,
                itemBuilder: (context,index){
                return Padding(
                  padding: const EdgeInsets.symmetric(vertical: 10,horizontal: 10),
                  child: Column(
                    children: [
                      Text(mContacts![index]["Name"]),
                      Text(mContacts![index]["Phone"]),
                    ],
                  ),
                );
                })
          ],
        )
      ),
    );
  }
}
