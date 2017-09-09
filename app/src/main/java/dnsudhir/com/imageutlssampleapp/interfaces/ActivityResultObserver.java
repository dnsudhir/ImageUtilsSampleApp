package dnsudhir.com.imageutlssampleapp.interfaces;

import android.content.Intent;

public interface ActivityResultObserver {
  void onActivityResult(int requestCode, int resultCode, Intent data, String fileName,
      String fileLocation, String prefs, int prefMode);
}
