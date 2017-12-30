package dnsudhir.com.imageutlssampleapp.image_utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveImage {

  private Context context;
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;

  public SaveImage(Context context) {
    //sharedPreferences = context.getSharedPreferences(prefString, Context.MODE_PRIVATE);
  }

  public void save(Bitmap bitmap, String fileName) {
    ContextWrapper cw = new ContextWrapper(context);
    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
    File outputFile = new File(directory, fileName);
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
      fileOutputStream.flush();
      fileOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void saveLocationInPrefs(String location) {
    savePrefString(ProfilePicSetter.TAG_IMAGE_PREF, location);
  }

  private void savePrefString(String key, String value) {
    editor = sharedPreferences.edit();
    editor.putString(key, value).apply();
  }
}
