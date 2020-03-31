import 'dart:async';
import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/services.dart';

import 'gallery_image.dart';
import 'image_selector_codec.dart';

class FlutterImageSelector {
  static const MethodChannel _channel = const MethodChannel(
      'com.travelunion/flutter_image_selector',
      StandardMethodCodec(ImageSelectorMessageCodec()));

  static Future<List<GalleryImage>> get getImages async {
    final String result = await _channel.invokeMethod('pickImages');
    Iterable images = json.decode(result);
    return images.map((image) => GalleryImage.fromJson(image)).toList();
  }

  static Future<Uint8List> getBitmap(String path) async {
    final List<int> result = await _channel.invokeMethod('getBitmap', { "path": path });
    return result;
  }
}