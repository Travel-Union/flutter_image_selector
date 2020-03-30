import 'dart:convert';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

class ImageSelectorMessageCodec extends StandardMessageCodec {
  const ImageSelectorMessageCodec();

  static const int _kImageResult = 128;

  @override
  void writeValue(WriteBuffer buffer, dynamic value) {
    super.writeValue(buffer, value);
  }

  @override
  dynamic readValueOfType(int type, ReadBuffer buffer) {
    switch (type) {
      case _kImageResult:
        final int jsonLength = readSize(buffer);
        final String json = utf8.decoder.convert(buffer.getUint8List(jsonLength));
        return json;
      default:
        return super.readValueOfType(type, buffer);
    }
  }
}