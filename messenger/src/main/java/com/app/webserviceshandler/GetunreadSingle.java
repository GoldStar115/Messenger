package com.app.webserviceshandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.app.messenger.ChatOneToOne;
import com.app.messenger.Global;
import com.app.messenger.GlobalConstant;

public class GetunreadSingle extends AsyncTask<String, Void, String> {
	String response = "";

	String FbID = "", userID = "";
	Handler replyTo;

	public GetunreadSingle(String GroupID, String FbID, Handler replyTo) {
		this.FbID = GroupID;
		this.userID = FbID;
		this.replyTo = replyTo;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {

		// http://messenger.amebasoftware.com/webservice/get_posts/?post_type=post&mtype=get_unread&group_id=101&user_id=50

		List<NameValuePair> param = new ArrayList<>();
		param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
		param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "get_unread_chat"));
		param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, userID + ""));
		param.add(new BasicNameValuePair(GlobalConstant.FACE_BOOK_ID, FbID));
		param.add(new BasicNameValuePair("timezone", Global.timeZone + ""));

		// String jsonString = WebServiceHandler.makeServiceCall_forChat(
		// GlobalConstant.URL, WebServiceHandler.GET, param);

		String url = "" + GlobalConstant.URL;

		String paramString = URLEncodedUtils.format(param, "utf-8");
		url += "?" + paramString;
		System.out.println(url);

		try {

			HttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			HttpClient httpClient = new DefaultHttpClient(httpParams);

			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			HttpGet httpGet = new HttpGet(url);

			httpResponse = httpClient.execute(httpGet);

			httpEntity = httpResponse.getEntity();

			response = EntityUtils.toString(httpEntity);

		} catch (Exception e) {
			System.out.println("EXCEPTION FROM THE SERVER" + e.getMessage());
			return response = GlobalConstant.ERROR_CODE_STRING;
		}

		String jsonString = response;
		Log.d("GetUnreadSingle","GetUnreadSingle" + "GetUnread Single" + jsonString);
		return jsonString;

	}

	@Override
	protected void onPostExecute(String result) {

		try {
			JSONObject jobjOuter = new JSONObject(result);
			String responseMSG = jobjOuter.getString(GlobalConstant.MESSAGE);
			Log.d("GetUnreadSingle","GetUnreadSingle" + "responseMSG" + responseMSG);
			if (responseMSG.equalsIgnoreCase(GlobalConstant.SUCCESS))
			{
				Bundle data = new Bundle();
				data.putString("unread", result);
				Message msg = Message.obtain();
				msg.setData(data);
				Log.d("GetUnreadSingle","GetUnreadSingle" + "-->" + "result" + " = "+ result + "Msg"+ " = "+msg.toString());
				replyTo.sendMessage(msg);
			} else
			{
				if (ChatOneToOne.refreshChat()) {
					 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					 {
						 new GetunreadSingle(FbID, userID, replyTo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						
					 } else
					    {
						 new GetunreadSingle(FbID, userID, replyTo).execute();
					    }
				}

			}

		} catch (Exception e) {
			if (ChatOneToOne.refreshChat()) {
				 if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				 {
					 new GetunreadSingle(FbID, userID, replyTo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					
				 } else
				    {
					 new GetunreadSingle(FbID, userID, replyTo).execute();
				    }
			}
			e.printStackTrace();
		}

		super.onPostExecute(result);
	}

}
