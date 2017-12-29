package dnsudhir.com.imageutlssampleapp.image_utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogMaker extends ProfilePicSetter {

  private String[] builderMenu = { "Select Picture", "Capture Photo" };

  private AlertDialog.Builder builder;
  protected ChooseImage chooseImage;
  protected CaptureCamera captureCamera;

  public DialogMaker(Context context, String prefString) {
    super(context, prefString);
    builder = new AlertDialog.Builder(context);
    chooseImage = new ChooseImage(context, prefString);
    captureCamera = new CaptureCamera(context, prefString);
  }

  public void makeDialog() {
    builder.setItems(builderMenu, new DialogInterface.OnClickListener() {

      @Override public void onClick(DialogInterface dialog, int which) {
        switch (which) {
          case 0:
            chooseImage.choose();
            break;
          case 1:
            captureCamera.capture();
            break;
        }
      }
    });
  }

  public void setDialogItems(String[] builderMenu) {
    this.builderMenu = builderMenu;
  }

  public void setDialogItem(String item, int itemPosition) {
    builderMenu[itemPosition] = item;
  }
}
