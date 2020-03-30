import Flutter
import UIKit

public class SwiftImageSelectorPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "com.travelunion/flutter_image_selector", binaryMessenger: registrar.messenger())
    let instance = SwiftImageSelectorPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("Not Implemented")
  }
}
