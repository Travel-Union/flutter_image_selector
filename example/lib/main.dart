import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:image_selector/image_selector.dart';
import 'package:image_selector/gallery_image.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<GalleryImage> _images = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    List<GalleryImage> images;
    try {
      images = await FlutterImageSelector.getImages;
    } on PlatformException {
      images = [];
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _images = images;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: _images.length == 0
              ? Text('No images selected')
              : ListView.builder(
                  itemCount: _images.length,
                  itemBuilder: (ctx, index) => Image.file(File(_images[index].data)),
                ),
        ),
      ),
    );
  }
}
