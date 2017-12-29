package dnsudhir.com.imageutlssampleapp.image_utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;
import dnsudhir.com.imageutlssampleapp.R;
import dnsudhir.com.imageutlssampleapp.interfaces.ActivityResultObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfilePicSetter implements ActivityResultObserver {

  public static final int TAKE_PICTURE = 16;
  public static final int SET_IMAGE = 17;
  private Context context;
  private String prefString;
  private String fileLocation;
  private String imageDir = "imageDir";
  public static String TAG_IMAGE_PREF = "image_pref";
  private ImageView profilePic;

  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;

  public ProfilePicSetter(Context context, String prefString) {
    this.context = context;
    this.prefString = prefString;
    ContextWrapper contextWrapper = new ContextWrapper(context);
    File file = contextWrapper.getDir(imageDir, Context.MODE_PRIVATE);
    fileLocation = file.toString();

    sharedPreferences = context.getSharedPreferences(prefString, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  private void load() {
    if (!sharedPreferences.getString(ProfilePicSetter.TAG_IMAGE_PREF, "").contentEquals("")) {
      profilePic.setImageBitmap(getFromFileName(fileLocation));
    } else {
      profilePic.setImageResource(R.mipmap.ic_launcher_round);
    }
  }

  public void setImageView(ImageView profilePic) {
    this.profilePic = profilePic;
    load();
    setOnClick();
  }

  private void setOnClick() {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    String[] builderMenu = new String[] { "Select Picture", "Capture Photo" };
    builder.setItems(builderMenu, new DialogInterface.OnClickListener() {
      @Override public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
          case 0:
            chooseImage();
            break;
          case 1:
            captureCamera();
            break;
        }
      }
    });
    builder.show();
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

  public void chooseImage() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_PICK);
      ((AppCompatActivity) context).startActivityForResult(
          Intent.createChooser(intent, "Select Picture:"), SET_IMAGE);
    } else {
      Toast.makeText(context, "Please Mount Any Storage And Try Again", Toast.LENGTH_SHORT).show();
    }
  }

  public void captureCamera() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
      ((Activity) context).startActivityForResult(takePictureIntent, TAKE_PICTURE);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data, String prefs) {
    if (requestCode == SET_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
      this.fileLocation = getPath(context, data.getData());
      saveImgLocInPrefs(this.fileLocation, prefs);
      Bitmap bitmap = getFromFileName(fileLocation);
    } else if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK && data != null) {
      Bitmap bitmap = (Bitmap) data.getExtras().get("data");
      String fileName =
          "photo_" + new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(new Date()) + ".jpg";
      saveImage(bitmap, fileName);
      saveImgLocInPrefs(this.fileLocation + "/" + fileName, prefs);
    }

    profilePic.setImageBitmap(getFromFileName(this.fileLocation));
  }

  private void saveImage(Bitmap bitmap, String fileName) {
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

  private void saveImgLocInPrefs(String fileLocation, String preferences) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(preferences, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(TAG_IMAGE_PREF, fileLocation).apply();
  }

  public Bitmap getImgFromPrefs(String preferences) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(preferences, Context.MODE_PRIVATE);
    return getFromFileName(sharedPreferences.getString(TAG_IMAGE_PREF, ""));
  }

  private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
    byte[] byteFormat = stream.toByteArray();
    return Base64.encodeToString(byteFormat, Base64.DEFAULT);
  }
}



