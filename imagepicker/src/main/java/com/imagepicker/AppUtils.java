package com.imagepicker;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HIMANGI--- Patel on 31/1/18.
 */

public class AppUtils {

  static File getWorkingDirectory(Activity activity) {
//    File directory =
//        new File(Environment.getExternalStorageDirectory(), activity.getApplicationContext().getPackageName());
    File directory = new File(activity.getFilesDir(), "external_files");
    if (!directory.exists()) {
      directory.mkdir();
    }
    return directory;
  }

  static FileUri createImageFile(Activity activity, String prefix) {
    FileUri fileUri = new FileUri();

    File storageDir = new File(activity.getFilesDir(), "external_files");
    if(!storageDir.exists())
      storageDir.mkdir();

    File image = null;
//    File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    try {
      image = File.createTempFile(timeStamp, ".jpg", storageDir);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (image != null) {
      fileUri.setFile(image);
      //
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        fileUri.setImageUrl(FileProvider.getUriForFile(activity,
            activity.getApplicationContext().getPackageName() + ".provider", image));
      } else {
        fileUri.setImageUrl(Uri.parse("file:" + image.getAbsolutePath()));
      }
    }
    return fileUri;
  }
}
