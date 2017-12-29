package dnsudhir.com.imageutlssampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import dnsudhir.com.imageutlssampleapp.image_utils.ProfilePicSetter;

public class MainActivity extends AppCompatActivity {

  private ProfilePicSetter profilePicSetter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ImageView iv = findViewById(R.id.iv);
    profilePicSetter = new ProfilePicSetter(this, "prefs");
    profilePicSetter.setImageView(iv);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    profilePicSetter.onActivityResult(requestCode, resultCode, data);
  }
}






