package dnsudhir.com.imageutlssampleapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import dnsudhir.com.imageutlssampleapp.image_utils.ImageUtils;

public class MainActivity extends AppCompatActivity {

  private ImageView iv;
  private ImageUtils imageUtils;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    iv = (ImageView) findViewById(R.id.iv);

    imageUtils = new ImageUtils(this);

    iv.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        String[] builderMenu = new String[] {"Select Picture","Capture Photo"};
        builder.setItems(builderMenu, new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialogInterface, int i) {
            imageUtils.setActivityObserver();
            switch (i) {
              case 0:
                imageUtils.chooseImage();
                break;
              case 1:
                imageUtils.captureCamera();
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

    if (requestCode == ImageUtils.SET_IMAGE && resultCode == RESULT_OK && data != null) {
      imageUtils.onActivityResult(requestCode,resultCode,data,null,null,"prefs",MODE_PRIVATE);
    } else if (requestCode == ImageUtils.TAKE_PICTURE && resultCode == RESULT_OK && data != null) {
      imageUtils.onActivityResult(requestCode,resultCode,data,"","","prefs",MODE_PRIVATE);
    }

  }
}






