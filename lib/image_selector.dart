import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

import 'gallery_image.dart';
import 'image_selector_codec.dart';

class FlutterImageSelector {
  static const MethodChannel _channel = const MethodChannel(
      'com.travelunion/flutter_image_selector',
      StandardMethodCodec(ImageSelectorMessageCodec()));

  static Future<List<GalleryImage>> get getImages async {
    final String result = await _channel.invokeMethod('showGallery');
    Iterable folders = json.decode(result);
    return folders.map((folder) => GalleryImage.fromJson(folder)).toList();
  }
}