package dnsudhir.com.imageutlssampleapp.image_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class GetImage {

  static Bitmap getFromFileName(String fileName) throws FileNotFoundException {
    Bitmap bitmap = null;
    File imageFile = new File(fileName);
    if (imageFile.exists()) {
      FileInputStream fis = new FileInputStream(imageFile);
      bitmap = BitmapFactory.decodeStream(fis);
    }
    return bitmap;
  }
}
