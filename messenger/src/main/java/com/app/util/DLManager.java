package com.app.util;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

public class DLManager {

	@SuppressLint("NewApi")
	public static void useDownloadManager(String url, String name, Context c) {
		DownloadManager dm = (DownloadManager) c
				.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request dlrequest = new DownloadManager.Request(
				Uri.parse(url));
		dlrequest
				.setAllowedNetworkTypes(
						DownloadManager.Request.NETWORK_WIFI
								| DownloadManager.Request.NETWORK_MOBILE)
				.setTitle("Get-groupy")
				.setDescription("Downloading in Progress..")
				.setDestinationInExternalPublicDir("Get-groupy", name + ".jpg")
				.allowScanningByMediaScanner();

		dm.enqueue(dlrequest);

	}
}
