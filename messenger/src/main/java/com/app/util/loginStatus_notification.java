package com.app.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.app.messenger.GlobalConstant;
import com.app.webserviceshandler.WebServiceHandler;

public class loginStatus_notification extends AsyncTask<String, Void, String> {

	
	
	
	@Override
	protected String doInBackground(String... params) {
		
		String status=params[0];
		String id=params[1];
		
//		http://Messenger.amebasoftware.com/webservice/get_posts/?post_type=post&mtype=logout&user_id=1&notification_status=Y

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
		param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "logout"));
//		param.add(new BasicNameValuePair("notification_status",status));
		param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID,id ));


		return (new WebServiceHandler()).makeServiceCall(
				GlobalConstant.URL,
				WebServiceHandler.GET, param);
	}
	@Override
	protected void onPostExecute(String result) {
		
		
		
		super.onPostExecute(result);
	}
	
	
	
	
	
	
	

}
