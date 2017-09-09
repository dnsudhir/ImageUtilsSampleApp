package dnsudhir.com.imageutlssampleapp.image_utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.widget.Toast;
import dnsudhir.com.imageutlssampleapp.interfaces.ActivityResultObserver;
import dnsudhir.com.imageutlssampleapp.interfaces.OnCallComplete;
import dnsudhir.com.imageutlssampleapp.network.WebCall;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageUtils implements ActivityResultObserver {

  public static final int TAKE_PICTURE = 16;
  public static final int SET_IMAGE = 17;
  public ActivityResultObserver activityResultObserver;
  private Context context;
  private Bitmap bitmap;

  public ImageUtils(Context context) {
    this.context = context;
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private String getPath(final Context context, final Uri uri) {

    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageDirectory() + "/" + split[1];
        }
      }
      // DownloadsProvider
      else if (isDownloadsDocument(uri)) {

        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri =
            ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                Long.valueOf(id));

        return getDataColumn(context, contentUri, null, null);
      }
      // MediaProvider
      else if (isMediaDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[] {
            split[1]
        };

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {

      // Return the remote address
      if (isGooglePhotosUri(uri)) return uri.getLastPathSegment();

      return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }

    return null;
  }

  /**
   * Get the value of the data column for this Uri. This is useful for
   * MediaStore Uris, and other file-based ContentProviders.
   *
   * @param context The context.
   * @param uri The Uri to query.
   * @param selection (Optional) Filter used in the query.
   * @param selectionArgs (Optional) Selection arguments used in the query.
   * @return The value of the _data column, which is typically a file path.
   */
  private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {
        column
    };

    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        final int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } finally {
      if (cursor != null) cursor.close();
    }
    return null;
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is ExternalStorageProvider.
   */
  private boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is DownloadsProvider.
   */
  private boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is MediaProvider.
   */
  private boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri.getAuthority());
  }

  /**
   * @param uri The Uri to check.
   * @return Whether the Uri authority is Google Photos.
   */
  private boolean isGooglePhotosUri(Uri uri) {
    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
  }

  public void setActivityObserver() {
    activityResultObserver = this;
  }

  public void chooseImage() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_PICK);
      ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture:"),
          SET_IMAGE);
    } else {
      Toast.makeText(context, "Please Mount Any Media And Try Again", Toast.LENGTH_SHORT).show();
    }
  }

  public void captureCamera() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
      ((Activity) context).startActivityForResult(takePictureIntent, TAKE_PICTURE);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data, String fileName,
      String fileLocation, String prefs, int prefMode) {
    if (requestCode == SET_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
      fileLocation = getPath(context, data.getData());
      saveImgLocInPrefs(fileLocation, prefs, prefMode);
      bitmap = getFromFileName(fileLocation);
    } else if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK && data != null) {
      bitmap = (Bitmap) data.getExtras().get("data");
      saveImage(bitmap, fileLocation, fileName);
      saveImgLocInPrefs("/data/data/qubag.com.qubagdeliveryapp/app_data/imageDir/"+ fileName, prefs, prefMode);
    }
  }

  private void saveImage(Bitmap bitmap, String fileLocation, String fileName) {
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

  private Bitmap getFromFileName(String fileName) {
    Bitmap bitmap = null;
    File imageFile = new File(fileName);
    try {
      FileInputStream fis = new FileInputStream(imageFile);
      bitmap = BitmapFactory.decodeStream(fis);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return bitmap;
  }

  public Bitmap getImageBitMap() {
    return bitmap;
  }

  private void saveImgLocInPrefs(String fileLocation, String preferences, int mode) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(preferences, mode);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("", fileLocation).apply();
  }

  public Bitmap getImgFromPrefs(String IMAGE_NAME,String preferences, int mode) {
    SharedPreferences sharedPreferences = context.getSharedPreferences(preferences, mode);
    return getFromFileName(sharedPreferences.getString(IMAGE_NAME, ""));
  }

  public void sendImageToServer(String url,String cust_id) {
    String imgString = getEncoded64ImageStringFromBitmap(bitmap);
    RequestBody requestBody = new FormBody.Builder()
        .add("",cust_id).add("",imgString).build();
    WebCall webCall = new WebCall(context, requestBody, url);
    webCall.checkSetExecute(webCall, new OnCallComplete() {
      @Override public void CallCompleted(boolean b, String result) {
        try {
          JSONObject jsonObject = new JSONObject(result);
          Toast.makeText(context, jsonObject.optString(""), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    byte[] byteFormat = stream.toByteArray();
    return Base64.encodeToString(byteFormat, Base64.DEFAULT);
  }

  public Uri setImageUri() {
    ContextWrapper cw = new ContextWrapper(context);
    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
    File file = new File(directory, System.currentTimeMillis() + ".png");
    Uri imgUri = Uri.fromFile(file);
    return imgUri;
  }
}



