package dnsudhir.com.imageutlssampleapp.image_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.FileNotFoundException;

import static dnsudhir.com.imageutlssampleapp.image_utils.ProfilePicSetter.SET_IMAGE;

public class ChooseImage implements ActivityResultObserver {

  private Context context;
  private String prefString;
  private Path path;
  private ImageView profilePic;

  ChooseImage(Context context, String prefString) {
    this.context = context;
    this.prefString = prefString;
    path = new Path();
  }

  void choose(ImageView profilePic) {
    this.profilePic = profilePic;
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

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SET_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
      String fileLocation = path.getPath(context, data.getData());
      savePrefString(ProfilePicSetter.TAG_IMAGE_PREF, fileLocation);
      try {
        Bitmap bitmap = GetImage.getFromFileName(fileLocation);
        if (bitmap != null) {
          profilePic.setImageBitmap(bitmap);
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    } else if (requestCode == SET_IMAGE && resultCode == AppCompatActivity.RESULT_CANCELED) {
      Toast.makeText(context, "Operation Cancelled, Please Try Again", Toast.LENGTH_SHORT).show();
    } else if (requestCode == SET_IMAGE && data == null) {
      Toast.makeText(context, "Failed To Retrieve Data, Please Try Again", Toast.LENGTH_SHORT)
          .show();
    }
  }

  private void savePrefString(String key, String value) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences(prefString, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key, value).apply();
  }
}
