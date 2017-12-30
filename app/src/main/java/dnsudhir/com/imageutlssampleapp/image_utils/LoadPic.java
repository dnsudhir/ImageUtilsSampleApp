package dnsudhir.com.imageutlssampleapp.image_utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import dnsudhir.com.imageutlssampleapp.R;

public class LoadPic   {

  private SharedPreferences sharedPreferences;
  private GetImage getImage;
  private String fileLocation;

  public LoadPic(Context context, String prefString) {
    sharedPreferences = context.getSharedPreferences(prefString, Context.MODE_PRIVATE);
    getImage = new GetImage();
  }

  public void load(ImageView profilePic) {
    fileLocation = sharedPreferences.getString(ProfilePicSetter.TAG_IMAGE_PREF, "");
    if (!fileLocation.contentEquals("")) {
      profilePic.setImageBitmap(getImage.getFromFileName(this.fileLocation));
    } else {
      profilePic.setImageResource(R.mipmap.ic_launcher_round);
    }
  }
}
