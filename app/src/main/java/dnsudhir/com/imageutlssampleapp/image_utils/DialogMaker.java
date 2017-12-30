package dnsudhir.com.imageutlssampleapp.image_utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ImageView;

public class DialogMaker {

  private String[] builderMenu = { "Select Picture", "Capture Photo" };

  private AlertDialog.Builder builder;
  ChooseImage chooseImage;
  CaptureCamera captureCamera;
  private ImageView profilePic;

  DialogMaker(Context context, String prefString, ImageView profilePic) {
    builder = new AlertDialog.Builder(context);
    chooseImage = new ChooseImage(context, prefString);
    captureCamera = new CaptureCamera(context, prefString);
    this.profilePic = profilePic;
  }

  void makeDialog() {
    builder.setItems(builderMenu, new DialogInterface.OnClickListener() {

      @Override public void onClick(DialogInterface dialog, int which) {
        switch (which) {
          case 0:
            chooseImage.choose(profilePic);
            break;
          case 1:
            captureCamera.capture(profilePic);
            break;
        }
      }
    });
    builder.show();
  }

  public void setDialogItems(String[] builderMenu) {
    this.builderMenu = builderMenu;
  }
}
