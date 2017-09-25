package dnsudhir.com.imageutlssampleapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

    if (!sharedPreferences.getString(ProfilePicSetter.TAG_IMAGE_PREF, "").contentEquals("")) {
      iv.setImageBitmap(profilePicSetter.getImgFromPrefs("prefs"));
    }else {
      iv.setImageResource(R.mipmap.ic_launcher_round);
    }


    iv.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        String[] builderMenu = new String[] {"Select Picture","Capture Photo"};
        builder.setItems(builderMenu, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
              case 0:
                profilePicSetter.chooseImage();
                break;
              case 1:
                profilePicSetter.captureCamera();
                break;
            }
          }
        });
        builder.show();
      }
    });
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT) @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == ProfilePicSetter.SET_IMAGE && resultCode == RESULT_OK && data != null) {
      profilePicSetter.onActivityResult(requestCode,resultCode,data,"prefs");
      iv.setImageBitmap(profilePicSetter.getImageBitMap());
    } else if (requestCode == ProfilePicSetter.TAKE_PICTURE && resultCode == RESULT_OK && data != null) {
      profilePicSetter.onActivityResult(requestCode,resultCode,data,"prefs");
      iv.setImageBitmap(profilePicSetter.getImageBitMap());
    }

  }
}






