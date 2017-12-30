package dnsudhir.com.imageutlssampleapp.image_utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;

public class ProfilePicSetter implements ActivityResultObserver {

  public static final int TAKE_PICTURE = 16;
  public static final int SET_IMAGE = 17;
  private Context context;
  protected String prefString;
  protected String fileLocation;
  private String imageDir = "imageDir";
  public static String TAG_IMAGE_PREF = "image_pref";
  protected ImageView profilePic;

  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;
  private DialogMaker dialogMaker;
  private LoadPic loadPic;

  public ProfilePicSetter(Context context, String prefString) {
    this.context = context;
    this.prefString = prefString;
    ContextWrapper contextWrapper = new ContextWrapper(context);
    File file = contextWrapper.getDir(imageDir, Context.MODE_PRIVATE);
    fileLocation = file.toString();

    sharedPreferences = context.getSharedPreferences(prefString, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  public ProfilePicSetter(Context context, String prefString, ImageView profilePic) {
    this.context = context;
    this.prefString = prefString;
    this.profilePic = profilePic;
    ContextWrapper contextWrapper = new ContextWrapper(context);
    File file = contextWrapper.getDir(imageDir, Context.MODE_PRIVATE);
    fileLocation = file.toString();

    sharedPreferences = context.getSharedPreferences(prefString, Context.MODE_PRIVATE);
    editor = sharedPreferences.edit();
  }

  public void setImageView(ImageView profilePic) {
    this.profilePic = profilePic;
    dialogMaker = new DialogMaker(context, profilePic);
    loadPic = new LoadPic(context, prefString);
    loadPic.load(profilePic);
    this.profilePic.setOnClickListener(new View.OnClickListener() {
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



