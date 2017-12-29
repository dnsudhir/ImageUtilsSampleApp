package dnsudhir.com.imageutlssampleapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import dnsudhir.com.imageutlssampleapp.image_utils.ProfilePicSetter;

public class MainActivity extends AppCompatActivity {

  private ImageView iv;
  private ProfilePicSetter profilePicSetter;
  private SharedPreferences sharedPreferences;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    iv = (ImageView) findViewById(R.id.iv);
    profilePicSetter = new ProfilePicSetter(this);
    sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);

    profilePicSetter.setImageView(iv);

    if (!sharedPreferences.getString(ProfilePicSetter.TAG_IMAGE_PREF, "").contentEquals("")) {
      iv.setImageBitmap(profilePicSetter.getImgFromPrefs("prefs"));
    } else {
      iv.setImageResource(R.mipmap.ic_launcher_round);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    profilePicSetter.onActivityResult(requestCode, resultCode, data, "prefs");
  }
}






