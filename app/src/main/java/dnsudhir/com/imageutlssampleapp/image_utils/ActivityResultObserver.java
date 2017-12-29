package dnsudhir.com.imageutlssampleapp.image_utils;

import android.content.Intent;

public interface ActivityResultObserver {
  void onActivityResult(int requestCode, int resultCode, Intent data);
}
