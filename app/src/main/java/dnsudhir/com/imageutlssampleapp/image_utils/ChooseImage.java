package dnsudhir.com.imageutlssampleapp.image_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import static dnsudhir.com.imageutlssampleapp.image_utils.ProfilePicSetter.SET_IMAGE;

public class ChooseImage implements ActivityResultObserver {

  private Context context;
  private Path path;
  private GetImage getImage;
  private SaveImage saveImage;
  private String fileLocation;
  private ImageView profilePic;

  public ChooseImage(Context context) {
    this.context = context;
    path = new Path(context);
    saveImage = new SaveImage(context);
    getImage = new GetImage();
  }

  public void choose(ImageView profilePic) {
    this.profilePic = profilePic;
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
