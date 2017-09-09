package dnsudhir.com.imageutlssampleapp.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import dnsudhir.com.imageutlssampleapp.interfaces.OnCallComplete;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebCall extends AsyncTask<Void, Void, String> {

  private ProgressDialog progressDialog;
  private Context context;
  private RequestBody requestBody;
  private String URL;
  private Request request;
  private OnCallComplete onCallComplete;

  public WebCall(Context context, RequestBody requestBody, String URL) {
    this.context = context;
    this.requestBody = requestBody;
    this.URL = URL;
  }

  @Override protected void onPreExecute() {
    super.onPreExecute();
    progressDialog = ProgressDialog.show(context, "Please Wait", "Loading");
    if (requestBody != null) {
      request = new Request.Builder().url(Config.URL_ROOT + URL).post(requestBody).build();
    } else {
      request = new Request.Builder().url(Config.URL_ROOT + URL).build();
    }
  }

  @Override protected String doInBackground(Void... voids) {
    String strResponse = "";
    OkHttpClient okHttpClient = new OkHttpClient();
    try {
      Response response = okHttpClient.newCall(request).execute();
      strResponse = response.body().string();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return strResponse;
  }

  @Override protected void onPostExecute(String s) {
    super.onPostExecute(s);
    if (progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
    if (s != null && !s.contentEquals("")) {
      onCallComplete.CallCompleted(true, s);
    } else {
      onCallComplete.CallCompleted(false, s);
    }
  }

  public void checkSetExecute(WebCall webCall, OnCallComplete onCallComplete) {

    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
      this.onCallComplete = onCallComplete;
      webCall.execute();
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setTitle("No Internet Connection");
      builder.setMessage("Please Check Your Internet Connection");
      builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialogInterface, int i) {
          ((Activity) context).finish();
          context.startActivity((((Activity) context).getIntent()));
        }
      });
      builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialogInterface, int i) {
          ((Activity) context).finish();
        }
      });
      builder.setCancelable(false);
      builder.show();
    }
  }
}
