package com.app.webserviceshandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.app.messenger.GlobalConstant;

public class GetRatings extends AsyncTask<Void , Void , String>
{

	String					response	= "";
//	Context					con;

	String					userid,friend_id;

 String	Rating		= "0",MyRating="0";
 
	
	Handler responseHandler;

	public GetRatings( String id,String frnd_id, Handler responseHandlerRatings)
	{

		
		this.userid = id;
		this.friend_id=frnd_id;
		this.responseHandler=responseHandlerRatings;

	}

	@Override
	protected String doInBackground(Void... params)
	{

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
		param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "get_rating"));

		param.add(new BasicNameValuePair("friend_id", "" + friend_id));
		
		param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, "" + userid));

		response = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);

		return response;
	}

	@Override
	protected void onPostExecute(String result)
	{
		try
		{

			if( result.contains(GlobalConstant.SUCCESS) )
			{
				// {"Message":"Success","rating":null}

				JSONObject jsonObject = new JSONObject(response);
				Rating = jsonObject.getString("rating");
				MyRating=jsonObject.getString("user_rating");
			}
			
			
			Bundle data = new Bundle();
			data.putString("ratings", Rating);
			data.putString("user_rating", MyRating);
			Message msg = Message.obtain();
			msg.setData(data);
			responseHandler.sendMessage(msg);

		}
		catch(Exception e)
		{
			
		}

		super.onPostExecute(result);
	}
}
