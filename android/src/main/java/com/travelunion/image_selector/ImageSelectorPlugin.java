package com.travelunion.image_selector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.travelunion.image_selector.utils.GalleryActivity;
import com.travelunion.image_selector.utils.GalleryImage;
import com.travelunion.image_selector.utils.GalleryImageResult;
import com.travelunion.image_selector.utils.ImageSelectorCodec;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.StandardMethodCodec;

/** ImageSelectorPlugin */
public class ImageSelectorPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener, PluginRegistry.RequestPermissionsResultListener {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private ActivityPluginBinding binding;
  private static Context context;
  private static Activity activity;
  private Result result;
  private static final String CHANNEL_NAME = "com.travelunion/flutter_image_selector";
  private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
  private static final int GALLERY_ACTIVITY = 1;

  private void showGallery() {
    try {
      if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
      {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        return;
      }

      startGalleryActivity(true);
    } catch (Exception e) {
      result.error("image_selector_fatal", "Image picker failed failed: " + e.getMessage(), null);
      e.printStackTrace();
    }
  }

  private void startGalleryActivity(boolean granted) {
    try {
      Intent intent = new Intent(activity, GalleryActivity.class);
      intent.putExtra("permissionGranted", granted);
      activity.startActivityForResult(intent, GALLERY_ACTIVITY);
    } catch (Exception e) {
      Log.e("image_selector_fatal", "startView: " + e.getLocalizedMessage());
    }
  }

  @Override
  public boolean onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
    if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission granted.
        startGalleryActivity(true);
        return true;
      } else {
        // User refused to grant permission.
        startGalleryActivity(false);
        return true;
      }
    }

    return false;
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL_NAME, new StandardMethodCodec(ImageSelectorCodec.INSTANCE));
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_NAME, new StandardMethodCodec(ImageSelectorCodec.INSTANCE));
    channel.setMethodCallHandler(new ImageSelectorPlugin());
    activity = registrar.activity();
    context = registrar.context();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    this.result = result;

    if(call.method.equals("pickImages")) {
      showGallery();
    } else if(call.method.equals("getBitmap")) {
      getBitmap((String) call.argument("path"));
    } else {
      result.notImplemented();
    }
  }

  void getBitmap(String path) {
    Bitmap image = BitmapFactory.decodeFile(path);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
    byte[] imageBytes = outputStream.toByteArray();
    image.recycle();

    result.success(imageBytes);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    result = null;
  }

  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding) {
    activity = binding.getActivity();
    this.binding = binding;
    binding.addRequestPermissionsResultListener(this);
    binding.addActivityResultListener(this);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    onDetachedFromActivity();
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    onAttachedToActivity(binding);
  }

  @Override
  public void onDetachedFromActivity() {
    activity = null;
    binding.removeRequestPermissionsResultListener(this);
    binding.removeActivityResultListener(this);
    binding = null;
  }

  @Override
  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == GALLERY_ACTIVITY) {
      if (resultCode == Activity.RESULT_OK) {
        if (data != null) {
          try {
            ArrayList<GalleryImage> images = data.getParcelableArrayListExtra(("images"));
            result.success(new GalleryImageResult(images));
          } catch (Exception e) {
            result.success(false);
          }
        } else {
          result.success(false);
        }
        result = null;
        return true;
      } else {
        result.success(false);
        return true;
      }
    }
    return false;
  }
}
