package com.app.webserviceshandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Handler;

import com.app.messenger.GlobalConstant;

public class Set_Rating extends AsyncTask<Void , Void , String>
{

	String					response	= "";
//	Context					con;

	String					userid,friend_id;

	 String	Rating		= "0";
	
	Handler responseHandler;

	public Set_Rating( String uid,String fid, String rating)
	{

		
		this.userid = uid;
		this.friend_id = fid;
		this.Rating=rating;

	}

	@Override
	protected String doInBackground(Void... params)
	{

		List<NameValuePair> param = new ArrayList<NameValuePair>();
		param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
		param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "set_rating"));

		param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, "" + userid));
		param.add(new BasicNameValuePair("friend_id", "" + friend_id));
		param.add(new BasicNameValuePair("rating", "" + Rating));
		

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
				

			}
			
			
			

		}
		catch(Exception e)
		{
			
		}

		super.onPostExecute(result);
	}
}
