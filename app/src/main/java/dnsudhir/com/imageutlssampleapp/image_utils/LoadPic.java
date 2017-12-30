package dnsudhir.com.imageutlssampleapp.image_utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.ImageView;
import dnsudhir.com.imageutlssampleapp.R;
import java.io.FileNotFoundException;

class LoadPic {

  static void load(Context context, String prefString, ImageView profilePic)
      throws FileNotFoundException {
    SharedPreferences sharedPreferences;
    sharedPreferences = context.getSharedPreferences(prefString, Context.MODE_PRIVATE);
    String fileLocation = sharedPreferences.getString(ProfilePicSetter.TAG_IMAGE_PREF, "");
    if (!fileLocation.contentEquals("")) {
      Bitmap bitmap = GetImage.getFromFileName(fileLocation);
      if (bitmap != null) {
        profilePic.setImageBitmap(bitmap);
      } else {
        profilePic.setImageResource(R.mipmap.ic_launcher_round);
      }
    } else {
      profilePic.setImageResource(R.mipmap.ic_launcher_round);
    }
  }
}
