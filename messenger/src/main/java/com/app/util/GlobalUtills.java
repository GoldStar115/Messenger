package com.app.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.ParseException;

import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.app.messenger.BadgeView;
import com.app.messenger.R;
import com.app.model.CountryCodeDetail;
import com.app.model.FriendInfo;
import com.app.webserviceshandler.Group_visibility;

public class GlobalUtills
{


	public static Handler  responseSaveHandler = new Handler();
	public static boolean flag_onPausedToResume = false;


	public static boolean					worldGroup					= false;
	public static boolean					joinORadd_group				= true;
	public static String					user_id						= "";
	public static String					verification_code			= "";
	public String							facebook_id					= "";
	public static String					email_id					= "";
	public static String					country_iso_code			= "IN";
	public static  Toast						toast;
	public static  EditText					txt_verify_code;
	public static TabWidget					tabWidget;
	public static ListView					list_chat;
	public static List<CountryCodeDetail>	country_code_list			= new ArrayList<CountryCodeDetail>();

	public static BadgeView					badge1;
	public static BadgeView					badgeGroup;
	// public static List<BadgeView> badgeSenderSingleChat=new
	// ArrayList<BadgeView>();

	public static BadgeView					badgeSenderGroupChat;

	public static String					YouTube_URL					= "";
	public static String					YouTube_VIdeoID				= "";
	public static String					youtubeTitile				= "";

	public static boolean					SingleChat_noti				= true;
	public static String					SingleChat_Sender			= "";
	public static String					GroupChat_Sender			= "";

	public static boolean					groupChat_noti				= true;
	public static boolean					allNotification				= true;
	public static boolean					delete_chat_notification	= true;
	public static int						IntentWHeretoGo				= 0;

	public static String					msgCountSingle				= "";
	public static String					msgCountGroup				= "";

	public static List<String>				single_msg_sender			= new ArrayList<String>();
	public static List<String>				group_msg_sender			= new ArrayList<String>();

	// public static String temp_sender_single = "";
	public static String					temp_sender_group			= "";

	public static String					JsonGroupMembers			= "";
	public static ArrayList<FriendInfo>		list_of_GroupMembers		= new ArrayList<FriendInfo>();

	public static boolean					mygroups_savelocal			= true;

	public static void showToast(String msg, Context context)
	{
		if( toast != null )
		{
			toast.cancel();
		}
		toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

	}

	/*
	 * class ConnectionDetector { private Context _context;
	 * 
	 * public ConnectionDetector(Context context){ this._context = context; }
	 * 
	 * public boolean isConnectingToInternet(){ ConnectivityManager connectivity
	 * = (ConnectivityManager)
	 * _context.getSystemService(Context.CONNECTIVITY_SERVICE); if (connectivity
	 * != null) { NetworkInfo[] info = connectivity.getAllNetworkInfo(); if
	 * (info != null) for (int i = 0; i < info.length; i++) if
	 * (info[i].getState() == NetworkInfo.State.CONNECTED) { return true; }
	 * 
	 * } return false; } }
	 */

	public boolean haveNetworkConnection(Context con)
	{
		ConnectivityManager connectivity = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		if( connectivity != null )
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if( info != null )
				for( int i = 0; i < info.length; i++ )
					if( info[i].getState() == NetworkInfo.State.CONNECTED )
					{
						return true;
					}

		}
		return false;
	}

	public String BitMapToString(Bitmap bitmap)
	{

		String temp = "";
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

			byte[] b = baos.toByteArray();

			baos.close();

			baos = null;
			temp = Base64.encodeToString(b, Base64.DEFAULT);

		}
		catch(IOException e)
		{

			e.printStackTrace();
		}

		return temp;
	}

	public Dialog prepararDialog(Context contexto, int layout)
	{
		final Dialog dialog = new Dialog(contexto, R.style.Theme_Dialog);
		dialog.setContentView(layout);
		// dialog.setCancelable(false);

		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		if( layout == R.layout.profile_info_other )
		{
			window.setGravity(Gravity.TOP);
			// dialog.setCancelable(true);
		}
		else
		{
			window.setGravity(Gravity.CENTER);
		}

		return dialog;
	}

	public Bitmap StringToBitMap(String encodedString)
	{
		try
		{
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
			return bitmap;
		}
		catch(Exception e)
		{
			e.getMessage();
			return null;
		}
	}

	public String convertToString(InputStream inputStream) throws IOException
	{
		if( inputStream != null )
		{
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 1024);
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			}
			finally
			{
				inputStream.close();
			}
			return writer.toString();
		}
		else
		{
			return "";
		}
	}

	// veiw photo
	boolean	download	= true;

	public void ViewPhoto(final String URL, final Context con, final String Imagename)
	{
		final Dialog dialogLoader = new Dialog(con, R.style.Theme_Viewphoto);
		dialogLoader.setTitle("Image");
		dialogLoader.setContentView(R.layout.veiw_photo);
		dialogLoader.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		Button btnOK = (Button) dialogLoader.findViewById(R.id.btn_ok_ViewPhoto);
		Button btnCancel = (Button) dialogLoader.findViewById(R.id.btn_cancel_ViewPhoto);

		RoundedCornersGaganImageView imgVPhoto = (RoundedCornersGaganImageView) dialogLoader.findViewById(R.id.imgV_ViewPhoto);

		imgVPhoto.setImageUrl(con, URL);

		final File f = new File("mnt/sdcard/Get-groupy/" + Imagename + ".jpg");

		if( Imagename.equals("") || (f.length() != 0) )
		{
			btnOK.setText("Ok");
			download = false;
		}
		else
		{
			download = true;
			btnOK.setText("Save");
		}

		btnOK.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				if( download )
				{
					DLManager.useDownloadManager(URL, Imagename, con);
				}

				dialogLoader.dismiss();

			}
		});

		btnCancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				dialogLoader.dismiss();

			}
		});

		dialogLoader.show();
	}

	public String parseDateToSAVE(String time)
	{
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "yyyyMMdd_HHmmss";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try
		{
			date = inputFormat.parse(time);
			str = outputFormat.format(date);

		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch(java.text.ParseException e)
		{

			e.printStackTrace();
		}
		return str;
	}

	// end view photo

	// format to time

	public String parseDateToTime(String time)
	{
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "hh:mm a";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try
		{
			date = inputFormat.parse(time);
			str = outputFormat.format(date);

		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch(java.text.ParseException e)
		{

			e.printStackTrace();
		}
		return str;
	}

	// end formatt time
	// format to time

	public String parseDateMYgroups(String time)
	{
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "dd-MM-yyyy hh:mm a";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try
		{
			date = inputFormat.parse(time);
			str = outputFormat.format(date);

		}
		catch(ParseException e)
		{
			e.printStackTrace();
		}
		catch(java.text.ParseException e)
		{

			e.printStackTrace();
		}
		return str;
	}

	// end formatt time

	// get you tube video id
	public String getYoutubeVideoId(String youtubeUrl)
	{
		// String video_id = "";
		int endLength = 0;;
		// String url = "https://www.youtube.com/watch?v=wZZ7oFKsKzY";
		if( youtubeUrl.indexOf("v=") == -1 ) // there's no video id
			return "";
		String id = youtubeUrl.split("v=")[1]; // everything before the first
												// 'v=' will be the first
												// element, i.e. [0]
		int start_of_id = youtubeUrl.indexOf("=") + 1;
		int end_of_id = youtubeUrl.indexOf("&"); // if there are other
													// parameters in the url,
													// get only the id's value
		if( end_of_id > 2 )
		{
			endLength = end_of_id;
		}
		else
		{
			endLength = youtubeUrl.length();
		}

		if( youtubeUrl.length() != -1 )
			id = youtubeUrl.substring(start_of_id, endLength);
		return id;

	}

	// gallery imnage get
	public void copyStream(InputStream input, OutputStream output) throws IOException
	{

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1)
		{
			output.write(buffer, 0, bytesRead);
		}
	}

	//

	// one button dialog
	public void DialogOK(Context con, String heading, String subhead)
	{

		final Dialog dialog = prepararDialog(con, R.layout.dialog_three_options);

		TextView title = (TextView) dialog.findViewById(R.id.txtVmainTitle);
		title.setText("" + heading);

		TextView subheading = (TextView) dialog.findViewById(R.id.txtVsubheading);
		subheading.setTextSize(15);
		subheading.setText("" + subhead);

		Button btn_Chat = (Button) dialog.findViewById(R.id.btnChat);
		Button btn_call = (Button) dialog.findViewById(R.id.btncall);
		Button btn_groups = (Button) dialog.findViewById(R.id.btngroups);
		ImageButton btn_close = (ImageButton) dialog.findViewById(R.id.btnCloseDialog);

		btn_groups.setVisibility(View.GONE);
		btn_close.setVisibility(View.GONE);
		btn_call.setVisibility(View.GONE);
		btn_Chat.setText("Ok");

		btn_Chat.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	// end dialog

	// dialog visibilty show hide

	String	viisible	= "";

	public void groupVisiblty(final String gID, final String uID, final Context c, final ImageView img)
	{
		SharedPreferences preferences = c.getSharedPreferences("Chat", Context.MODE_PRIVATE);
		final boolean visibility = preferences.getBoolean("V" + gID, true);

		final Dialog dialog = prepararDialog(c, R.layout.dialog_three_options);

		TextView title = (TextView) dialog.findViewById(R.id.txtVmainTitle);
		title.setText("Set Visibility");

		TextView subheading = (TextView) dialog.findViewById(R.id.txtVsubheading);
		subheading.setTextSize(15);
		subheading.setText("Set Your Visibilty for this group..");

		Button btn_Chat = (Button) dialog.findViewById(R.id.btnChat);
		Button btn_call = (Button) dialog.findViewById(R.id.btncall);
		Button btn_groups = (Button) dialog.findViewById(R.id.btngroups);
		ImageButton btn_close = (ImageButton) dialog.findViewById(R.id.btnCloseDialog);

		btn_groups.setVisibility(View.GONE);
		btn_close.setVisibility(View.GONE);
		// btn_call.setVisibility(View.GONE);
		final int draw;
		if( visibility )
		{
			btn_call.setText("Set Hidden");
			draw = R.drawable.eyes_groups_close;
		}
		else
		{
			btn_call.setText("Set Visible");
			draw = R.drawable.eyes_groups;
		}

		btn_Chat.setText("Cancel");

		btn_call.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				img.setImageResource(draw);

				new Group_visibility(c, uID, gID, visibility).execute();
				dialog.dismiss();
			}
		});

		btn_Chat.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	// end dialog visibilty show hide

	// notification local after getunread

	public void generateNotification_local(final Context context, String message, final int flag)
	{

		try
		{

			int icon = R.drawable.app_icon_g;

			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			String title = "Get Groupy" + "";

			PendingIntent intent;

			Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
			context.sendBroadcast(it);

			intent = PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

			Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

			if( defaultSound == null )
			{
				defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
				if( defaultSound == null )
				{
					defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
				}
			}

			Builder builder = new Notification.Builder(context).setContentTitle(title).setContentText(message).setContentIntent(intent).setSmallIcon(icon).setLights(Color.GREEN, 1, 7)
					.setAutoCancel(true).setSound(defaultSound);

			Notification not = new Notification.BigTextStyle(builder).bigText(message).build();

			// builder.setStyle(new
			// Notification.BigTextStyle().bigText(message)) ;

			not.defaults |= Notification.DEFAULT_VIBRATE;
			not.defaults |= Notification.DEFAULT_SOUND;

			notificationManager.notify(flag, not);

			new Handler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					notifManager.cancel(flag);
				}
			}, 2000);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	// end notification

	// application is active or not
	public static boolean isApplicationRunningBackground(final Context context)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(am.getRunningAppProcesses().size());
		for( RunningTaskInfo runningTaskInfo : tasks )
		{
			if( runningTaskInfo.topActivity.getPackageName().equals(context.getPackageName()) )
			{

				return true;
			}
		}
		return false;
	}

	// end application active or not

	//
	// // slow network
	// public static Dialog slow_internetdialog;
	// public static void slow_internet_dialog(final Context con,
	// OnClickListener ret)
	// {
	//
	// slow_internetdialog = GlobalUtills.prepararDialog(con,
	// R.layout.dialog_three_options);
	//
	//
	//
	// TextView title = (TextView) slow_internetdialog
	// .findViewById(R.id.txtVmainTitle);
	// title.setText("Error..!");
	//
	// TextView subheading = (TextView) slow_internetdialog
	// .findViewById(R.id.txtVsubheading);
	// subheading.setTextSize(15);
	// subheading.setText("Unable to connect.....!");
	//
	//
	//
	// Button btn_Chat = (Button)
	// slow_internetdialog.findViewById(R.id.btnChat);
	// Button btn_call = (Button)
	// slow_internetdialog.findViewById(R.id.btncall);
	// Button btn_groups = (Button)
	// slow_internetdialog.findViewById(R.id.btngroups);
	// ImageButton btn_close = (ImageButton)
	// slow_internetdialog.findViewById(R.id.btnCloseDialog);
	//
	// btn_groups.setVisibility(View.GONE);
	// btn_close.setVisibility(View.GONE);
	//
	// btn_call.setText("Retry");
	// btn_Chat.setText("Cancel");
	//
	// btn_call.setOnClickListener(ret);
	//
	//
	// btn_Chat.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// slow_internetdialog.dismiss();
	// }
	// });
	//
	// slow_internetdialog.show();
	//
	//
	//
	// }
	// // slow network end
	//
	//

	public Intent getOpenFacebookIntent(Context context, String Fid)
	{

		try
		{

			// context.getPackageManager().getPackageInfo("com.facebook.katana",
			// 0);
			// return new Intent(Intent.ACTION_VIEW,
			// Uri.parse("fb://pages/"+Fid)).addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + Fid));

		}
		catch(Exception e)
		{
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + Fid));
		}
	}

	public static String countryCode_jsonString =
			"{\"Message\":\"Success\",\n" +
					"\"countries\":[\n" +
					"{\n" +
					"\"country_id\":\"1\",\n" +
					"\"name\":\"Afghanistan\",\n" +
					"\"country_code\":\"+93\",\n" +
					"\"iso_code\":\"AF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"2\",\n" +
					"\"name\":\"Albania\",\n" +
					"\"country_code\":\"+355\",\n" +
					"\"iso_code\":\"AL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"3\",\n" +
					"\"name\":\"Algeria\",\n" +
					"\"country_code\":\"+213\",\n" +
					"\"iso_code\":\"DZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"4\",\n" +
					"\"name\":\"American Samoa\",\n" +
					"\"country_code\":\"+685\",\n" +
					"\"iso_code\":\"AS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"5\",\n" +
					"\"name\":\"Andorra\",\n" +
					"\"country_code\":\"+376\",\n" +
					"\"iso_code\":\"AD\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"6\",\n" +
					"\"name\":\"Angola\",\n" +
					"\"country_code\":\"+244\",\n" +
					"\"iso_code\":\"AO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"7\",\n" +
					"\"name\":\"Anguilla\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"AI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"8\",\n" +
					"\"name\":\"Antarctica\",\n" +
					"\"country_code\":\"+672\",\n" +
					"\"iso_code\":\"AQ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"9\",\n" +
					"\"name\":\"Antigua and Barbuda\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"AG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"10\",\n" +
					"\"name\":\"Argentina\",\n" +
					"\"country_code\":\"+54\",\n" +
					"\"iso_code\":\"AR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"11\",\n" +
					"\"name\":\"Armenia\",\n" +
					"\"country_code\":\"+374\",\n" +
					"\"iso_code\":\"AM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"12\",\n" +
					"\"name\":\"Aruba\",\n" +
					"\"country_code\":\"+297\",\n" +
					"\"iso_code\":\"AW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"13\",\n" +
					"\"name\":\"Australia\",\n" +
					"\"country_code\":\"+61\",\n" +
					"\"iso_code\":\"AU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"14\",\n" +
					"\"name\":\"Austria\",\n" +
					"\"country_code\":\"+43\",\n" +
					"\"iso_code\":\"AT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"15\",\n" +
					"\"name\":\"Azerbaijan\",\n" +
					"\"country_code\":\"+994\",\n" +
					"\"iso_code\":\"AZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"16\",\n" +
					"\"name\":\"Bahamas\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"BS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"17\",\n" +
					"\"name\":\"Bahrain\",\n" +
					"\"country_code\":\"+973\",\n" +
					"\"iso_code\":\"BH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"18\",\n" +
					"\"name\":\"Bangladesh\",\n" +
					"\"country_code\":\"+880\",\n" +
					"\"iso_code\":\"BD\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"19\",\n" +
					"\"name\":\"Barbados\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"BB\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"20\",\n" +
					"\"name\":\"Belarus\",\n" +
					"\"country_code\":\"+375\",\n" +
					"\"iso_code\":\"BY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"21\",\n" +
					"\"name\":\"Belgium\",\n" +
					"\"country_code\":\"+32\",\n" +
					"\"iso_code\":\"BE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"22\",\n" +
					"\"name\":\"Belize\",\n" +
					"\"country_code\":\"+501\",\n" +
					"\"iso_code\":\"BZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"23\",\n" +
					"\"name\":\"Benin\",\n" +
					"\"country_code\":\"+229\",\n" +
					"\"iso_code\":\"BJ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"24\",\n" +
					"\"name\":\"Bermuda\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"BM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"25\",\n" +
					"\"name\":\"Bhutan\",\n" +
					"\"country_code\":\"+975\",\n" +
					"\"iso_code\":\"BT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"26\",\n" +
					"\"name\":\"Bolivia\",\n" +
					"\"country_code\":\"+591\",\n" +
					"\"iso_code\":\"BO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"27\",\n" +
					"\"name\":\"Bosnia and Herzegovina\",\n" +
					"\"country_code\":\"+387\",\n" +
					"\"iso_code\":\"BA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"28\",\n" +
					"\"name\":\"Botswana\",\n" +
					"\"country_code\":\"+267\",\n" +
					"\"iso_code\":\"BW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"29\",\n" +
					"\"name\":\"Bouvet Island\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"BV\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"30\",\n" +
					"\"name\":\"Brazil\",\n" +
					"\"country_code\":\"+55\",\n" +
					"\"iso_code\":\"BR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"31\",\n" +
					"\"name\":\"British Indian Ocean Territory\",\n" +
					"\"country_code\":\"+246\",\n" +
					"\"iso_code\":\"IO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"32\",\n" +
					"\"name\":\"Brunei Darussalam\",\n" +
					"\"country_code\":\"+673\",\n" +
					"\"iso_code\":\"BN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"33\",\n" +
					"\"name\":\"Bulgaria\",\n" +
					"\"country_code\":\"+359\",\n" +
					"\"iso_code\":\"BG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"34\",\n" +
					"\"name\":\"Burkina Faso\",\n" +
					"\"country_code\":\"+226\",\n" +
					"\"iso_code\":\"BF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"35\",\n" +
					"\"name\":\"Burundi\",\n" +
					"\"country_code\":\"+257\",\n" +
					"\"iso_code\":\"BI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"36\",\n" +
					"\"name\":\"Cambodia\",\n" +
					"\"country_code\":\"+855\",\n" +
					"\"iso_code\":\"KH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"37\",\n" +
					"\"name\":\"Cameroon\",\n" +
					"\"country_code\":\"+237\",\n" +
					"\"iso_code\":\"CM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"38\",\n" +
					"\"name\":\"Canada\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"CA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"39\",\n" +
					"\"name\":\"Cape Verde\",\n" +
					"\"country_code\":\"+238\",\n" +
					"\"iso_code\":\"CV\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"40\",\n" +
					"\"name\":\"Cayman Islands\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"KY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"41\",\n" +
					"\"name\":\"Central African Republic\",\n" +
					"\"country_code\":\"+236\",\n" +
					"\"iso_code\":\"CF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"42\",\n" +
					"\"name\":\"Chad\",\n" +
					"\"country_code\":\"+235\",\n" +
					"\"iso_code\":\"TD\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"43\",\n" +
					"\"name\":\"Chile\",\n" +
					"\"country_code\":\"+56\",\n" +
					"\"iso_code\":\"CL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"44\",\n" +
					"\"name\":\"China\",\n" +
					"\"country_code\":\"+86\",\n" +
					"\"iso_code\":\"CN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"45\",\n" +
					"\"name\":\"Christmas Island\",\n" +
					"\"country_code\":\"+61\",\n" +
					"\"iso_code\":\"CX\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"46\",\n" +
					"\"name\":\"Cocos (Keeling) Islands\",\n" +
					"\"country_code\":\"+61\",\n" +
					"\"iso_code\":\"CC\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"47\",\n" +
					"\"name\":\"Colombia\",\n" +
					"\"country_code\":\"+57\",\n" +
					"\"iso_code\":\"CO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"48\",\n" +
					"\"name\":\"Comoros\",\n" +
					"\"country_code\":\"+269\",\n" +
					"\"iso_code\":\"KM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"49\",\n" +
					"\"name\":\"Congo\",\n" +
					"\"country_code\":\"+242\",\n" +
					"\"iso_code\":\"CG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"50\",\n" +
					"\"name\":\"Cook Islands\",\n" +
					"\"country_code\":\"+682\",\n" +
					"\"iso_code\":\"CK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"51\",\n" +
					"\"name\":\"Costa Rica\",\n" +
					"\"country_code\":\"+506\",\n" +
					"\"iso_code\":\"CR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"52\",\n" +
					"\"name\":\"Cote D'Ivoire\",\n" +
					"\"country_code\":\"+225\",\n" +
					"\"iso_code\":\"CI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"53\",\n" +
					"\"name\":\"Croatia\",\n" +
					"\"country_code\":\"+385\",\n" +
					"\"iso_code\":\"HR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"54\",\n" +
					"\"name\":\"Cuba\",\n" +
					"\"country_code\":\"+53\",\n" +
					"\"iso_code\":\"CU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"55\",\n" +
					"\"name\":\"Cyprus\",\n" +
					"\"country_code\":\"+357\",\n" +
					"\"iso_code\":\"CY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"56\",\n" +
					"\"name\":\"Czech Republic\",\n" +
					"\"country_code\":\"+420\",\n" +
					"\"iso_code\":\"CZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"57\",\n" +
					"\"name\":\"Denmark\",\n" +
					"\"country_code\":\"+45\",\n" +
					"\"iso_code\":\"DK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"58\",\n" +
					"\"name\":\"Djibouti\",\n" +
					"\"country_code\":\"+253\",\n" +
					"\"iso_code\":\"DJ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"59\",\n" +
					"\"name\":\"Dominica\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"DM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"60\",\n" +
					"\"name\":\"Dominican Republic\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"DO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"61\",\n" +
					"\"name\":\"East Timor\",\n" +
					"\"country_code\":\"+670\",\n" +
					"\"iso_code\":\"TL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"62\",\n" +
					"\"name\":\"Ecuador\",\n" +
					"\"country_code\":\"+593\",\n" +
					"\"iso_code\":\"EC\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"63\",\n" +
					"\"name\":\"Egypt\",\n" +
					"\"country_code\":\"+20\",\n" +
					"\"iso_code\":\"EG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"64\",\n" +
					"\"name\":\"El Salvador\",\n" +
					"\"country_code\":\"+503\",\n" +
					"\"iso_code\":\"SV\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"65\",\n" +
					"\"name\":\"Equatorial Guinea\",\n" +
					"\"country_code\":\"+240\",\n" +
					"\"iso_code\":\"GQ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"66\",\n" +
					"\"name\":\"Eritrea\",\n" +
					"\"country_code\":\"+291\",\n" +
					"\"iso_code\":\"ER\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"67\",\n" +
					"\"name\":\"Estonia\",\n" +
					"\"country_code\":\"+372\",\n" +
					"\"iso_code\":\"EE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"68\",\n" +
					"\"name\":\"Ethiopia\",\n" +
					"\"country_code\":\"+251\",\n" +
					"\"iso_code\":\"ET\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"69\",\n" +
					"\"name\":\"Falkland Islands (Malvinas)\",\n" +
					"\"country_code\":\"+500\",\n" +
					"\"iso_code\":\"FK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"70\",\n" +
					"\"name\":\"Faroe Islands\",\n" +
					"\"country_code\":\"+298\",\n" +
					"\"iso_code\":\"FO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"71\",\n" +
					"\"name\":\"Fiji\",\n" +
					"\"country_code\":\"+679\",\n" +
					"\"iso_code\":\"FJ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"72\",\n" +
					"\"name\":\"Finland\",\n" +
					"\"country_code\":\"+358\",\n" +
					"\"iso_code\":\"FI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"74\",\n" +
					"\"name\":\"France, Metropolitan\",\n" +
					"\"country_code\":\"+33\",\n" +
					"\"iso_code\":\"FR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"75\",\n" +
					"\"name\":\"French Guiana\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"GF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"76\",\n" +
					"\"name\":\"French Polynesia\",\n" +
					"\"country_code\":\"+689\",\n" +
					"\"iso_code\":\"PF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"77\",\n" +
					"\"name\":\"French Southern Territories\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"TF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"78\",\n" +
					"\"name\":\"Gabon\",\n" +
					"\"country_code\":\"+241\",\n" +
					"\"iso_code\":\"GA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"79\",\n" +
					"\"name\":\"Gambia\",\n" +
					"\"country_code\":\"+220\",\n" +
					"\"iso_code\":\"GM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"80\",\n" +
					"\"name\":\"Georgia\",\n" +
					"\"country_code\":\"+995\",\n" +
					"\"iso_code\":\"GE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"81\",\n" +
					"\"name\":\"Germany\",\n" +
					"\"country_code\":\"+49\",\n" +
					"\"iso_code\":\"DE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"82\",\n" +
					"\"name\":\"Ghana\",\n" +
					"\"country_code\":\"+233\",\n" +
					"\"iso_code\":\"GH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"83\",\n" +
					"\"name\":\"Gibraltar\",\n" +
					"\"country_code\":\"+350\",\n" +
					"\"iso_code\":\"GI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"84\",\n" +
					"\"name\":\"Greece\",\n" +
					"\"country_code\":\"+30\",\n" +
					"\"iso_code\":\"GR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"85\",\n" +
					"\"name\":\"Greenland\",\n" +
					"\"country_code\":\"+299\",\n" +
					"\"iso_code\":\"GL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"86\",\n" +
					"\"name\":\"Grenada\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"GD\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"87\",\n" +
					"\"name\":\"Guadeloupe\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"GP\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"88\",\n" +
					"\"name\":\"Guam\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"GU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"89\",\n" +
					"\"name\":\"Guatemala\",\n" +
					"\"country_code\":\"+502\",\n" +
					"\"iso_code\":\"GT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"90\",\n" +
					"\"name\":\"Guinea\",\n" +
					"\"country_code\":\"+224\",\n" +
					"\"iso_code\":\"GN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"91\",\n" +
					"\"name\":\"Guinea-Bissau\",\n" +
					"\"country_code\":\"+245\",\n" +
					"\"iso_code\":\"GW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"92\",\n" +
					"\"name\":\"Guyana\",\n" +
					"\"country_code\":\"+592\",\n" +
					"\"iso_code\":\"GY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"93\",\n" +
					"\"name\":\"Haiti\",\n" +
					"\"country_code\":\"+509\",\n" +
					"\"iso_code\":\"HT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"94\",\n" +
					"\"name\":\"Heard and Mc Donald Islands\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"HM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"95\",\n" +
					"\"name\":\"Honduras\",\n" +
					"\"country_code\":\"+504\",\n" +
					"\"iso_code\":\"HN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"96\",\n" +
					"\"name\":\"Hong Kong\",\n" +
					"\"country_code\":\"+852\",\n" +
					"\"iso_code\":\"HK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"97\",\n" +
					"\"name\":\"Hungary\",\n" +
					"\"country_code\":\"+36\",\n" +
					"\"iso_code\":\"HU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"98\",\n" +
					"\"name\":\"Iceland\",\n" +
					"\"country_code\":\"+354\",\n" +
					"\"iso_code\":\"IS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"99\",\n" +
					"\"name\":\"India\",\n" +
					"\"country_code\":\"+91\",\n" +
					"\"iso_code\":\"IN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"100\",\n" +
					"\"name\":\"Indonesia\",\n" +
					"\"country_code\":\"+62\",\n" +
					"\"iso_code\":\"ID\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"101\",\n" +
					"\"name\":\"Iran (Islamic Republic of)\",\n" +
					"\"country_code\":\"+98\",\n" +
					"\"iso_code\":\"IR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"102\",\n" +
					"\"name\":\"Iraq\",\n" +
					"\"country_code\":\"+964\",\n" +
					"\"iso_code\":\"IQ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"103\",\n" +
					"\"name\":\"Ireland\",\n" +
					"\"country_code\":\"+353\",\n" +
					"\"iso_code\":\"IE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"104\",\n" +
					"\"name\":\"Israel\",\n" +
					"\"country_code\":\"+972\",\n" +
					"\"iso_code\":\"IL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"105\",\n" +
					"\"name\":\"Italy\",\n" +
					"\"country_code\":\"+39\",\n" +
					"\"iso_code\":\"IT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"106\",\n" +
					"\"name\":\"Jamaica\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"JM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"107\",\n" +
					"\"name\":\"Japan\",\n" +
					"\"country_code\":\"+81\",\n" +
					"\"iso_code\":\"JP\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"108\",\n" +
					"\"name\":\"Jordan\",\n" +
					"\"country_code\":\"+962\",\n" +
					"\"iso_code\":\"JO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"109\",\n" +
					"\"name\":\"Kazakhstan\",\n" +
					"\"country_code\":\"+7\",\n" +
					"\"iso_code\":\"KZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"110\",\n" +
					"\"name\":\"Kenya\",\n" +
					"\"country_code\":\"+254\",\n" +
					"\"iso_code\":\"KE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"111\",\n" +
					"\"name\":\"Kiribati\",\n" +
					"\"country_code\":\"+686\",\n" +
					"\"iso_code\":\"KI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"112\",\n" +
					"\"name\":\"North Korea\",\n" +
					"\"country_code\":\"+850\",\n" +
					"\"iso_code\":\"KP\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"113\",\n" +
					"\"name\":\"Korea, Republic of\",\n" +
					"\"country_code\":\"+850\",\n" +
					"\"iso_code\":\"KR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"114\",\n" +
					"\"name\":\"Kuwait\",\n" +
					"\"country_code\":\"+965\",\n" +
					"\"iso_code\":\"KW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"115\",\n" +
					"\"name\":\"Kyrgyzstan\",\n" +
					"\"country_code\":\"+996\",\n" +
					"\"iso_code\":\"KG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"116\",\n" +
					"\"name\":\"Lao People's Democratic Republic\",\n" +
					"\"country_code\":\"+856\",\n" +
					"\"iso_code\":\"LA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"117\",\n" +
					"\"name\":\"Latvia\",\n" +
					"\"country_code\":\"+371\",\n" +
					"\"iso_code\":\"LV\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"118\",\n" +
					"\"name\":\"Lebanon\",\n" +
					"\"country_code\":\"+961\",\n" +
					"\"iso_code\":\"LB\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"119\",\n" +
					"\"name\":\"Lesotho\",\n" +
					"\"country_code\":\"+266\",\n" +
					"\"iso_code\":\"LS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"120\",\n" +
					"\"name\":\"Liberia\",\n" +
					"\"country_code\":\"+231\",\n" +
					"\"iso_code\":\"LR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"121\",\n" +
					"\"name\":\"Libyan Arab Jamahiriya\",\n" +
					"\"country_code\":\"+218\",\n" +
					"\"iso_code\":\"LY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"122\",\n" +
					"\"name\":\"Liechtenstein\",\n" +
					"\"country_code\":\"+423\",\n" +
					"\"iso_code\":\"LI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"123\",\n" +
					"\"name\":\"Lithuania\",\n" +
					"\"country_code\":\"+370\",\n" +
					"\"iso_code\":\"LT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"124\",\n" +
					"\"name\":\"Luxembourg\",\n" +
					"\"country_code\":\"+352\",\n" +
					"\"iso_code\":\"LU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"125\",\n" +
					"\"name\":\"Macau\",\n" +
					"\"country_code\":\"+853\",\n" +
					"\"iso_code\":\"MO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"126\",\n" +
					"\"name\":\"FYROM\",\n" +
					"\"country_code\":\"+389\",\n" +
					"\"iso_code\":\"MK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"127\",\n" +
					"\"name\":\"Madagascar\",\n" +
					"\"country_code\":\"+261\",\n" +
					"\"iso_code\":\"MG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"128\",\n" +
					"\"name\":\"Malawi\",\n" +
					"\"country_code\":\"+265\",\n" +
					"\"iso_code\":\"MW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"129\",\n" +
					"\"name\":\"Malaysia\",\n" +
					"\"country_code\":\"+60\",\n" +
					"\"iso_code\":\"MY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"130\",\n" +
					"\"name\":\"Maldives\",\n" +
					"\"country_code\":\"+960\",\n" +
					"\"iso_code\":\"MV\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"131\",\n" +
					"\"name\":\"Mali\",\n" +
					"\"country_code\":\"+223\",\n" +
					"\"iso_code\":\"ML\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"132\",\n" +
					"\"name\":\"Malta\",\n" +
					"\"country_code\":\"+356\",\n" +
					"\"iso_code\":\"MT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"133\",\n" +
					"\"name\":\"Marshall Islands\",\n" +
					"\"country_code\":\"+692\",\n" +
					"\"iso_code\":\"MH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"134\",\n" +
					"\"name\":\"Martinique\",\n" +
					"\"country_code\":\"+596\",\n" +
					"\"iso_code\":\"MQ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"135\",\n" +
					"\"name\":\"Mauritania\",\n" +
					"\"country_code\":\"+222\",\n" +
					"\"iso_code\":\"MR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"136\",\n" +
					"\"name\":\"Mauritius\",\n" +
					"\"country_code\":\"+230\",\n" +
					"\"iso_code\":\"MU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"137\",\n" +
					"\"name\":\"Mayotte\",\n" +
					"\"country_code\":\"+262\",\n" +
					"\"iso_code\":\"YT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"138\",\n" +
					"\"name\":\"Mexico\",\n" +
					"\"country_code\":\"+52\",\n" +
					"\"iso_code\":\"MX\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"139\",\n" +
					"\"name\":\"Micronesia, Federated States of\",\n" +
					"\"country_code\":\"+691\",\n" +
					"\"iso_code\":\"FM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"140\",\n" +
					"\"name\":\"Moldova, Republic of\",\n" +
					"\"country_code\":\"+373\",\n" +
					"\"iso_code\":\"MD\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"141\",\n" +
					"\"name\":\"Monaco\",\n" +
					"\"country_code\":\"+377\",\n" +
					"\"iso_code\":\"MC\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"142\",\n" +
					"\"name\":\"Mongolia\",\n" +
					"\"country_code\":\"+976\",\n" +
					"\"iso_code\":\"MN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"143\",\n" +
					"\"name\":\"Montserrat\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"MS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"144\",\n" +
					"\"name\":\"Morocco\",\n" +
					"\"country_code\":\"+212\",\n" +
					"\"iso_code\":\"MA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"145\",\n" +
					"\"name\":\"Mozambique\",\n" +
					"\"country_code\":\"+258\",\n" +
					"\"iso_code\":\"MZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"146\",\n" +
					"\"name\":\"Myanmar\",\n" +
					"\"country_code\":\"+95\",\n" +
					"\"iso_code\":\"MM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"147\",\n" +
					"\"name\":\"Namibia\",\n" +
					"\"country_code\":\"+264\",\n" +
					"\"iso_code\":\"NA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"148\",\n" +
					"\"name\":\"Nauru\",\n" +
					"\"country_code\":\"+674\",\n" +
					"\"iso_code\":\"NR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"149\",\n" +
					"\"name\":\"Nepal\",\n" +
					"\"country_code\":\"+977\",\n" +
					"\"iso_code\":\"NP\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"150\",\n" +
					"\"name\":\"Netherlands\",\n" +
					"\"country_code\":\"+31\",\n" +
					"\"iso_code\":\"NL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"151\",\n" +
					"\"name\":\"Netherlands Antilles\",\n" +
					"\"country_code\":\"+599\",\n" +
					"\"iso_code\":\"AN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"152\",\n" +
					"\"name\":\"New Caledonia\",\n" +
					"\"country_code\":\"+687\",\n" +
					"\"iso_code\":\"NC\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"153\",\n" +
					"\"name\":\"New Zealand\",\n" +
					"\"country_code\":\"+64\",\n" +
					"\"iso_code\":\"NZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"154\",\n" +
					"\"name\":\"Nicaragua\",\n" +
					"\"country_code\":\"+505\",\n" +
					"\"iso_code\":\"NI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"155\",\n" +
					"\"name\":\"Niger\",\n" +
					"\"country_code\":\"+227\",\n" +
					"\"iso_code\":\"NE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"156\",\n" +
					"\"name\":\"Nigeria\",\n" +
					"\"country_code\":\"+234\",\n" +
					"\"iso_code\":\"NG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"157\",\n" +
					"\"name\":\"Niue\",\n" +
					"\"country_code\":\"+683\",\n" +
					"\"iso_code\":\"NU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"158\",\n" +
					"\"name\":\"Norfolk Island\",\n" +
					"\"country_code\":\"+672\",\n" +
					"\"iso_code\":\"NF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"159\",\n" +
					"\"name\":\"Northern Mariana Islands\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"MP\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"160\",\n" +
					"\"name\":\"Norway\",\n" +
					"\"country_code\":\"+47\",\n" +
					"\"iso_code\":\"NO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"161\",\n" +
					"\"name\":\"Oman\",\n" +
					"\"country_code\":\"+968\",\n" +
					"\"iso_code\":\"OM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"162\",\n" +
					"\"name\":\"Pakistan\",\n" +
					"\"country_code\":\"+92\",\n" +
					"\"iso_code\":\"PK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"163\",\n" +
					"\"name\":\"Palau\",\n" +
					"\"country_code\":\"+680\",\n" +
					"\"iso_code\":\"PW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"164\",\n" +
					"\"name\":\"Panama\",\n" +
					"\"country_code\":\"+507\",\n" +
					"\"iso_code\":\"PA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"165\",\n" +
					"\"name\":\"Papua New Guinea\",\n" +
					"\"country_code\":\"+675\",\n" +
					"\"iso_code\":\"PG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"166\",\n" +
					"\"name\":\"Paraguay\",\n" +
					"\"country_code\":\"+595\",\n" +
					"\"iso_code\":\"PY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"167\",\n" +
					"\"name\":\"Peru\",\n" +
					"\"country_code\":\"+51\",\n" +
					"\"iso_code\":\"PE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"168\",\n" +
					"\"name\":\"Philippines\",\n" +
					"\"country_code\":\"+63\",\n" +
					"\"iso_code\":\"PH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"169\",\n" +
					"\"name\":\"Pitcairn\",\n" +
					"\"country_code\":\"+870\",\n" +
					"\"iso_code\":\"PN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"170\",\n" +
					"\"name\":\"Poland\",\n" +
					"\"country_code\":\"+48\",\n" +
					"\"iso_code\":\"PL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"171\",\n" +
					"\"name\":\"Portugal\",\n" +
					"\"country_code\":\"+351\",\n" +
					"\"iso_code\":\"PT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"172\",\n" +
					"\"name\":\"Puerto Rico\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"PR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"173\",\n" +
					"\"name\":\"Qatar\",\n" +
					"\"country_code\":\"+974\",\n" +
					"\"iso_code\":\"QA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"174\",\n" +
					"\"name\":\"Reunion\",\n" +
					"\"country_code\":\"+262\",\n" +
					"\"iso_code\":\"RE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"175\",\n" +
					"\"name\":\"Romania\",\n" +
					"\"country_code\":\"+40\",\n" +
					"\"iso_code\":\"RO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"176\",\n" +
					"\"name\":\"Russian Federation\",\n" +
					"\"country_code\":\"+7\",\n" +
					"\"iso_code\":\"RU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"177\",\n" +
					"\"name\":\"Rwanda\",\n" +
					"\"country_code\":\"+250\",\n" +
					"\"iso_code\":\"RW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"178\",\n" +
					"\"name\":\"Saint Kitts and Nevis\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"KN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"179\",\n" +
					"\"name\":\"Saint Lucia\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"LC\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"180\",\n" +
					"\"name\":\"Saint Vincent and the Grenadines\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"VC\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"181\",\n" +
					"\"name\":\"Samoa\",\n" +
					"\"country_code\":\"+685\",\n" +
					"\"iso_code\":\"WS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"182\",\n" +
					"\"name\":\"San Marino\",\n" +
					"\"country_code\":\"+378\",\n" +
					"\"iso_code\":\"SM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"183\",\n" +
					"\"name\":\"Sao Tome and Principe\",\n" +
					"\"country_code\":\"+239\",\n" +
					"\"iso_code\":\"ST\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"184\",\n" +
					"\"name\":\"Saudi Arabia\",\n" +
					"\"country_code\":\"+966\",\n" +
					"\"iso_code\":\"SA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"185\",\n" +
					"\"name\":\"Senegal\",\n" +
					"\"country_code\":\"+221\",\n" +
					"\"iso_code\":\"SN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"186\",\n" +
					"\"name\":\"Seychelles\",\n" +
					"\"country_code\":\"+248\",\n" +
					"\"iso_code\":\"SC\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"187\",\n" +
					"\"name\":\"Sierra Leone\",\n" +
					"\"country_code\":\"+232\",\n" +
					"\"iso_code\":\"SL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"188\",\n" +
					"\"name\":\"Singapore\",\n" +
					"\"country_code\":\"+65\",\n" +
					"\"iso_code\":\"SG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"189\",\n" +
					"\"name\":\"Slovak Republic\",\n" +
					"\"country_code\":\"+421\",\n" +
					"\"iso_code\":\"SK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"190\",\n" +
					"\"name\":\"Slovenia\",\n" +
					"\"country_code\":\"+386\",\n" +
					"\"iso_code\":\"SI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"191\",\n" +
					"\"name\":\"Solomon Islands\",\n" +
					"\"country_code\":\"+677\",\n" +
					"\"iso_code\":\"SB\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"192\",\n" +
					"\"name\":\"Somalia\",\n" +
					"\"country_code\":\"+252\",\n" +
					"\"iso_code\":\"SO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"193\",\n" +
					"\"name\":\"South Africa\",\n" +
					"\"country_code\":\"+27\",\n" +
					"\"iso_code\":\"ZA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"194\",\n" +
					"\"name\":\"South Georgia & South Sandwich Islands\",\n" +
					"\"country_code\":\"+995\",\n" +
					"\"iso_code\":\"GS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"195\",\n" +
					"\"name\":\"Spain\",\n" +
					"\"country_code\":\"+34\",\n" +
					"\"iso_code\":\"ES\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"196\",\n" +
					"\"name\":\"Sri Lanka\",\n" +
					"\"country_code\":\"+94\",\n" +
					"\"iso_code\":\"LK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"197\",\n" +
					"\"name\":\"St. Helena\",\n" +
					"\"country_code\":\"+290\",\n" +
					"\"iso_code\":\"SH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"198\",\n" +
					"\"name\":\"St. Pierre and Miquelon\",\n" +
					"\"country_code\":\"+508\",\n" +
					"\"iso_code\":\"PM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"199\",\n" +
					"\"name\":\"Sudan\",\n" +
					"\"country_code\":\"+249\",\n" +
					"\"iso_code\":\"SD\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"200\",\n" +
					"\"name\":\"Suriname\",\n" +
					"\"country_code\":\"+597\",\n" +
					"\"iso_code\":\"SR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"201\",\n" +
					"\"name\":\"Svalbard and Jan Mayen Islands\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"SJ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"202\",\n" +
					"\"name\":\"Swaziland\",\n" +
					"\"country_code\":\"+268\",\n" +
					"\"iso_code\":\"SZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"203\",\n" +
					"\"name\":\"Sweden\",\n" +
					"\"country_code\":\"+46\",\n" +
					"\"iso_code\":\"SE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"204\",\n" +
					"\"name\":\"Switzerland\",\n" +
					"\"country_code\":\"+41\",\n" +
					"\"iso_code\":\"CH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"205\",\n" +
					"\"name\":\"Syrian Arab Republic\",\n" +
					"\"country_code\":\"+963\",\n" +
					"\"iso_code\":\"SY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"206\",\n" +
					"\"name\":\"Taiwan\",\n" +
					"\"country_code\":\"+886\",\n" +
					"\"iso_code\":\"TW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"207\",\n" +
					"\"name\":\"Tajikistan\",\n" +
					"\"country_code\":\"+992\",\n" +
					"\"iso_code\":\"TJ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"208\",\n" +
					"\"name\":\"Tanzania, United Republic of\",\n" +
					"\"country_code\":\"+255\",\n" +
					"\"iso_code\":\"TZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"209\",\n" +
					"\"name\":\"Thailand\",\n" +
					"\"country_code\":\"+66\",\n" +
					"\"iso_code\":\"TH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"210\",\n" +
					"\"name\":\"Togo\",\n" +
					"\"country_code\":\"+228\",\n" +
					"\"iso_code\":\"TG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"211\",\n" +
					"\"name\":\"Tokelau\",\n" +
					"\"country_code\":\"+690\",\n" +
					"\"iso_code\":\"TK\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"212\",\n" +
					"\"name\":\"Tonga\",\n" +
					"\"country_code\":\"+676\",\n" +
					"\"iso_code\":\"TO\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"213\",\n" +
					"\"name\":\"Trinidad and Tobago\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"TT\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"214\",\n" +
					"\"name\":\"Tunisia\",\n" +
					"\"country_code\":\"+216\",\n" +
					"\"iso_code\":\"TN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"215\",\n" +
					"\"name\":\"Turkey\",\n" +
					"\"country_code\":\"+90\",\n" +
					"\"iso_code\":\"TR\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"216\",\n" +
					"\"name\":\"Turkmenistan\",\n" +
					"\"country_code\":\"+993\",\n" +
					"\"iso_code\":\"TM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"217\",\n" +
					"\"name\":\"Turks and Caicos Islands\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"TC\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"218\",\n" +
					"\"name\":\"Tuvalu\",\n" +
					"\"country_code\":\"+688\",\n" +
					"\"iso_code\":\"TV\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"219\",\n" +
					"\"name\":\"Uganda\",\n" +
					"\"country_code\":\"+256\",\n" +
					"\"iso_code\":\"UG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"220\",\n" +
					"\"name\":\"Ukraine\",\n" +
					"\"country_code\":\"+380\",\n" +
					"\"iso_code\":\"UA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"221\",\n" +
					"\"name\":\"United Arab Emirates\",\n" +
					"\"country_code\":\"+971\",\n" +
					"\"iso_code\":\"AE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"222\",\n" +
					"\"name\":\"United Kingdom\",\n" +
					"\"country_code\":\"+44\",\n" +
					"\"iso_code\":\"GB\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"223\",\n" +
					"\"name\":\"United States\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"US\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"224\",\n" +
					"\"name\":\"United States Minor Outlying Islands\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"UM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"225\",\n" +
					"\"name\":\"Uruguay\",\n" +
					"\"country_code\":\"+598\",\n" +
					"\"iso_code\":\"UY\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"226\",\n" +
					"\"name\":\"Uzbekistan\",\n" +
					"\"country_code\":\"+998\",\n" +
					"\"iso_code\":\"UZ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"227\",\n" +
					"\"name\":\"Vanuatu\",\n" +
					"\"country_code\":\"+678\",\n" +
					"\"iso_code\":\"VU\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"228\",\n" +
					"\"name\":\"Vatican City State (Holy See)\",\n" +
					"\"country_code\":\"+39\",\n" +
					"\"iso_code\":\"VA\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"229\",\n" +
					"\"name\":\"Venezuela\",\n" +
					"\"country_code\":\"+58\",\n" +
					"\"iso_code\":\"VE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"230\",\n" +
					"\"name\":\"Viet Nam\",\n" +
					"\"country_code\":\"+84\",\n" +
					"\"iso_code\":\"VN\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"231\",\n" +
					"\"name\":\"Virgin Islands (British)\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"VG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"232\",\n" +
					"\"name\":\"Virgin Islands (U.S.)\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"VI\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"233\",\n" +
					"\"name\":\"Wallis and Futuna Islands\",\n" +
					"\"country_code\":\"+681\",\n" +
					"\"iso_code\":\"WF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"234\",\n" +
					"\"name\":\"Western Sahara\",\n" +
					"\"country_code\":\"+212\",\n" +
					"\"iso_code\":\"EH\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"235\",\n" +
					"\"name\":\"Yemen\",\n" +
					"\"country_code\":\"+967\",\n" +
					"\"iso_code\":\"YE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"237\",\n" +
					"\"name\":\"Democratic Republic of Congo\",\n" +
					"\"country_code\":\"+243\",\n" +
					"\"iso_code\":\"CD\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"238\",\n" +
					"\"name\":\"Zambia\",\n" +
					"\"country_code\":\"+260\",\n" +
					"\"iso_code\":\"ZM\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"239\",\n" +
					"\"name\":\"Zimbabwe\",\n" +
					"\"country_code\":\"+263\",\n" +
					"\"iso_code\":\"ZW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"240\",\n" +
					"\"name\":\"Jersey\",\n" +
					"\"country_code\":\"+44\",\n" +
					"\"iso_code\":\"JE\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"241\",\n" +
					"\"name\":\"Guernsey\",\n" +
					"\"country_code\":\"+44\",\n" +
					"\"iso_code\":\"GG\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"242\",\n" +
					"\"name\":\"Montenegro\",\n" +
					"\"country_code\":\"+382\",\n" +
					"\"iso_code\":\"ME\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"243\",\n" +
					"\"name\":\"Serbia\",\n" +
					"\"country_code\":\"+381\",\n" +
					"\"iso_code\":\"RS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"244\",\n" +
					"\"name\":\"Aaland Islands\",\n" +
					"\"country_code\":\"+358\",\n" +
					"\"iso_code\":\"AX\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"245\",\n" +
					"\"name\":\"Bonaire, Sint Eustatius and Saba\",\n" +
					"\"country_code\":\"+599\",\n" +
					"\"iso_code\":\"BQ\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"246\",\n" +
					"\"name\":\"Curacao\",\n" +
					"\"country_code\":\"+599\",\n" +
					"\"iso_code\":\"CW\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"247\",\n" +
					"\"name\":\"Palestinian Territory, Occupied\",\n" +
					"\"country_code\":\"+970\",\n" +
					"\"iso_code\":\"PS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"248\",\n" +
					"\"name\":\"South Sudan\",\n" +
					"\"country_code\":\"+211\",\n" +
					"\"iso_code\":\"SS\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"249\",\n" +
					"\"name\":\"St. Barthelemy\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"BL\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"250\",\n" +
					"\"name\":\"St. Martin (French part)\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"MF\"\n" +
					"},\n" +
					"{\n" +
					"\"country_id\":\"251\",\n" +
					"\"name\":\"Canary Islands\",\n" +
					"\"country_code\":\"+1\",\n" +
					"\"iso_code\":\"IC\"\n" +
					"}\n" +
					"]\n" +
					"}";


}
