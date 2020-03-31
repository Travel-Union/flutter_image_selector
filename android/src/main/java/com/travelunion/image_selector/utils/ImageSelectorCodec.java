package com.travelunion.image_selector.utils;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import io.flutter.plugin.common.StandardMessageCodec;

public class ImageSelectorCodec extends StandardMessageCodec {
    public static final ImageSelectorCodec INSTANCE = new ImageSelectorCodec();
    private static final Charset UTF8 = Charset.forName("UTF8");
    private static final byte IMAGE_RESULT = (byte) 128;

    @Override
    protected void writeValue(ByteArrayOutputStream stream, Object value) {
        if (value instanceof GalleryImageResult) {
            stream.write(IMAGE_RESULT);

            ArrayList<GalleryImage> images = ((GalleryImageResult) value).images;

            String json = new Gson().toJson(images);
            writeBytes(stream, json.getBytes(UTF8));
        } else {
            super.writeValue(stream, value);
        }
    }

    @Override
    protected Object readValueOfType(byte type, ByteBuffer buffer) {
        switch (type) {
            default:
                return super.readValueOfType(type, buffer);
        }
    }
}
