package dnsudhir.com.imageutlssampleapp.image_utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static dnsudhir.com.imageutlssampleapp.image_utils.ProfilePicSetter.TAKE_PICTURE;

public class CaptureCamera implements ActivityResultObserver {

  private SaveImage saveImage;
  private GetImage getImage;
  private Context context;
  private String prefString;
  private ImageView profilePic;
  private String fileLocation;

  public CaptureCamera(Context context,String prefString) {
    this.context = context;
    this.prefString = prefString;
    saveImage = new SaveImage(context,prefString);
    getImage = new GetImage();
  }

  public void capture(ImageView profilePic) {
    this.profilePic = profilePic;
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
      ((AppCompatActivity) context).startActivityForResult(takePictureIntent, TAKE_PICTURE);
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == TAKE_PICTURE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
      Bitmap bitmap = (Bitmap) data.getExtras().get("data");
      String fileName =
          "photo_" + new SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(new Date()) + ".jpg";

      ContextWrapper contextWrapper = new ContextWrapper(context);
      File file = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
      fileLocation = file.toString();
      fileLocation = fileLocation + "/" + fileName;
      saveImage.save(bitmap, fileName);
      saveImage.saveLocationInPrefs(fileLocation);

      profilePic.setImageBitmap(getImage.getFromFileName(fileLocation));
    }
  }
}
