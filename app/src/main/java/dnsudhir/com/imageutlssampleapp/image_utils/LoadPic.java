package dnsudhir.com.imageutlssampleapp.image_utils;

import android.content.Context;
import android.content.SharedPreferences;
import dnsudhir.com.imageutlssampleapp.R;

public class LoadPic extends ProfilePicSetter {

  private SharedPreferences sharedPreferences;
  private GetImage getImage;

  public LoadPic(Context context, String prefString) {
    super(context, prefString);
    sharedPreferences = context.getSharedPreferences(prefString, Context.MODE_PRIVATE);
    getImage = new GetImage();
  }

  public void load() {
    if (!sharedPreferences.getString(ProfilePicSetter.TAG_IMAGE_PREF, "").contentEquals("")) {
      profilePic.setImageBitmap(getImage.getFromFileName(this.fileLocation));
    } else {
      profilePic.setImageResource(R.mipmap.ic_launcher_round);
    }
  }
}
