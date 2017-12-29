package dnsudhir.com.imageutlssampleapp.image_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GetImage {

  public Bitmap getFromFileName(String fileName) {
    Bitmap bitmap = null;
    File imageFile = new File(fileName);
    if (imageFile.exists()) {
      try {
        FileInputStream fis = new FileInputStream(imageFile);
        bitmap = BitmapFactory.decodeStream(fis);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    return bitmap;
  }
}
