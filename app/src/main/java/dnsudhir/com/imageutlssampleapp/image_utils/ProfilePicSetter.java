package dnsudhir.com.imageutlssampleapp.image_utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.FileNotFoundException;

public class ProfilePicSetter implements ActivityResultObserver {

  static final int TAKE_PICTURE = 16;
  static final int SET_IMAGE = 17;
  private Context context;
  private String prefString;
  static String TAG_IMAGE_PREF = "image_pref";

  private DialogMaker dialogMaker;

  public ProfilePicSetter(Context context, String prefString) {
    this.context = context;
    this.prefString = prefString;
  }

  public ProfilePicSetter(Context context, String prefString, ImageView profilePic) throws FileNotFoundException{
    this.context = context;
    this.prefString = prefString;
    setImageView(profilePic);
  }

  public void setImageView(ImageView profilePic) throws FileNotFoundException{
    LoadPic.load(context, prefString, profilePic);
    dialogMaker = new DialogMaker(context, prefString, profilePic);
    profilePic.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        dialogMaker.makeDialog();
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SET_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
      dialogMaker.chooseImage.onActivityResult(requestCode, resultCode, data);
    } else if (requestCode == TAKE_PICTURE
        && resultCode == AppCompatActivity.RESULT_OK
        && data != null) {
      dialogMaker.captureCamera.onActivityResult(requestCode, resultCode, data);
    } else if ((requestCode == SET_IMAGE || requestCode == TAKE_PICTURE)
        && resultCode == AppCompatActivity.RESULT_CANCELED) {
      Toast.makeText(context, "Operation Cancelled, Please Try Again", Toast.LENGTH_SHORT).show();
    } else if ((requestCode == SET_IMAGE || requestCode == TAKE_PICTURE) && data == null) {
      Toast.makeText(context, "Failed To Retrieve Data, Please Try Again", Toast.LENGTH_SHORT)
          .show();
    }
  }
}



