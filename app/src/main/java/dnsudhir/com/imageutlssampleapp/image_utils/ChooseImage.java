package dnsudhir.com.imageutlssampleapp.image_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ChooseImage extends ProfilePicSetter implements ActivityResultObserver {

  private Context context;
  private Path path;
  private GetImage getImage;
  private SaveImage saveImage;

  public ChooseImage(Context context, String prefString) {
    super(context, prefString);
    this.context = context;
    path = new Path(context, prefString);
    saveImage = new SaveImage(context, prefString);
    getImage = new GetImage();
  }

  public void choose() {
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_PICK);
      ((AppCompatActivity) context).startActivityForResult(
          Intent.createChooser(intent, "Select Picture:"), SET_IMAGE);
    } else {
      Toast.makeText(context, "Please Mount Any Storage And Try Again", Toast.LENGTH_SHORT).show();
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == SET_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
      fileLocation = path.getPath(context, data.getData());
      saveImage.saveLocationInPrefs(fileLocation);
      profilePic.setImageBitmap(getImage.getFromFileName(this.fileLocation));
    }
  }
}
