package dnsudhir.com.imageutlssampleapp.image_utils;

import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class Base64Image {

  /**
   * By Default in this method the input bitmap will be converted into Base64String with Default
   * Encoding flag , with JPEG Compress Format , with 100% quality
   *
   * @param bitmap the bitmap to convert into Base64 String
   * @return if given bitmap is null this method will return empty string ""
   */
  public static String getCompressedString(Bitmap bitmap) {
    if (bitmap != null) {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
      byte[] byteFormat = stream.toByteArray();
      stream.reset();
      return Base64.encodeToString(byteFormat, Base64.DEFAULT);
    } else {
      return "";
    }
  }

  /**
   * In this method the input bitmap will be converted into Base64String with Default Encoding flag
   * with the given input compress format and quality.
   *
   * @param bitmap the bitmap to convert into Base64 String
   * @param compressFormat the bitmap will be compressed with the given format like JPEG,PNG and
   * WEBP.
   * @param quality the bitmap will be compressed with the given quality
   * @return if given bitmap is null this method will return empty string ""
   */
  public static String getCompressedString(Bitmap bitmap, Bitmap.CompressFormat compressFormat,
      int quality) {
    if (bitmap != null) {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      bitmap.compress(compressFormat, quality, stream);
      byte[] byteFormat = stream.toByteArray();
      stream.reset();
      return Base64.encodeToString(byteFormat, Base64.DEFAULT);
    } else {
      return "";
    }
  }

  /**
   * This Method will return Base64String with Default Encoding without compressing the Bitmap
   * @param bitmap the bitmap to convert into Base64String
   * @return if given bitmap is null this method will return empty string ""
   */

  public static String getUnCompressedString(Bitmap bitmap) {
    if (bitmap != null) {
      ByteBuffer byteBuffer = ByteBuffer.allocate(bitmap.getByteCount());
      bitmap.copyPixelsToBuffer(byteBuffer);
      byte[] byteFormat = byteBuffer.array();
      return Base64.encodeToString(byteFormat, Base64.DEFAULT);
    } else {
      return "";
    }
  }
}
