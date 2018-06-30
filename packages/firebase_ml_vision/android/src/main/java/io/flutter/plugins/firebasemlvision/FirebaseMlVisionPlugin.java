package io.flutter.plugins.firebasemlvision;

import android.net.Uri;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import java.io.File;
import java.io.IOException;

/** FirebaseMlVisionPlugin */
public class FirebaseMlVisionPlugin implements MethodCallHandler {
  private Registrar registrar;

  private FirebaseMlVisionPlugin(Registrar registrar) {
    this.registrar = registrar;
  }

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel =
        new MethodChannel(registrar.messenger(), "plugins.flutter.io/firebase_ml_vision");
    channel.setMethodCallHandler(new FirebaseMlVisionPlugin(registrar));
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    FirebaseVisionImage image = filePathToVisionImage((String) call.arguments, result);
    switch (call.method) {
      case "BarcodeDetector#detectInImage":
        break;
      case "BarcodeDetector#close":
        break;
      case "FaceDetector#detectInImage":
        if (image != null) FaceDetector.instance.handleDetection(image, result);
        break;
      case "FaceDetector#close":
        FaceDetector.instance.close(result);
        break;
      case "LabelDetector#detectInImage":
        break;
      case "LabelDetector#close":
        break;
      case "TextDetector#detectInImage":
        if (image != null) TextDetector.instance.handleDetection(image, result);
        break;
      case "TextDetector#close":
        TextDetector.instance.close(result);
        break;
      default:
        result.notImplemented();
    }
  }

  private FirebaseVisionImage filePathToVisionImage(String path, Result result) {
    File file = new File(path);

    try {
      return FirebaseVisionImage.fromFilePath(registrar.context(), Uri.fromFile(file));
    } catch (IOException exception) {
      result.error("textDetectorIOError", exception.getLocalizedMessage(), null);
    }

    return null;
  }
}
