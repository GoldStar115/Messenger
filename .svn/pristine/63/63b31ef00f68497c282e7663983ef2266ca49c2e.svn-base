package com.app.util;


import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkCheck {
	//---------------InternetConnection------------
	static Context ctx;
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;
	// ---------------------Network Check ------------------------
	 public static boolean isNetworkConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo info_network = connectivityManager.getActiveNetworkInfo();
		if (info_network != null && info_network.isConnected()) {
			return true;
		}
		else{
		
		return false;
		}
	}


public static boolean checkInternetConnection(Context context) {
    ConnectivityManager cm = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (cm.getActiveNetworkInfo() != null
            && cm.getActiveNetworkInfo().isAvailable()
            && cm.getActiveNetworkInfo().isConnected()) {
    	Log.e("Network Testing for  calling webservice", "***  Network is Available***");
        return true;
    } else {
        
        return false;
    }

}
//-----------------------Internet Connection ----------------------
public static int getConnectivityStatus(Context context) {
	ConnectivityManager cm = (ConnectivityManager) context
			.getSystemService(Context.CONNECTIVITY_SERVICE);

	NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	if (null != activeNetwork) {
		if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
			return TYPE_WIFI;

		if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
			return TYPE_MOBILE;
	}
	return TYPE_NOT_CONNECTED;
}

public static String getConnectivityStatusString(Context context) {
	int conn = NetworkCheck.getConnectivityStatus(context);
	String status = null;
	if (conn == NetworkCheck.TYPE_WIFI) {
		status = "true";
	} else if (conn == NetworkCheck.TYPE_MOBILE) {
		status = "true";
	} else if (conn == NetworkCheck.TYPE_NOT_CONNECTED) {
		status = "false";
	}
	return status;
}

public static void openInternetDialog(Context c) {
	ctx = c;
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
	alertDialogBuilder.setTitle("Internet Alert!");
	alertDialogBuilder
			.setMessage(
					"You are not connected to Internet..Please Enable Internet!")
			.setCancelable(false)
			.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
							final Intent intent = new Intent(Intent.ACTION_MAIN, null);
							intent.addCategory(Intent.CATEGORY_LAUNCHER);
							final ComponentName cn = new ComponentName(
									"com.android.settings",
									"com.android.settings.wifi.WifiSettings");
							intent.setComponent(cn);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							ctx.startActivity(intent);
						}
					})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
	AlertDialog alertDialog = alertDialogBuilder.create();
	alertDialog.show();
}

}