package com.app.webserviceshandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;

import com.app.messenger.GlobalConstant;
import com.app.messenger.R;
import com.app.util.GlobalUtills;

public class Group_visibility extends AsyncTask<Void, Void, String> 
{

	String response="";
	Context con;
	String visibility;
	String groupid;
	String userid;
	boolean visiisi=true;
	
	
	public Group_visibility(Context con,String user,String group,boolean visi) {
		
		this.con=con;
		this.userid=user;
		this.groupid=group;


		if( visi )
		{
			visibility = "N";
			visiisi=false;
		}
		else
		{
			visibility = "Y";
			visiisi=true;
		}


		
//		if(visibility.equalsIgnoreCase("Y"))
//		{
//			visiisi=true;
//		}
//		else
//		{
//			visiisi=false;
//		}
	}
	@Override
	protected String doInBackground(Void... params) {
		
		
//		http://ameba.get-groupy.com/webservice/get_posts/?post_type=post&mtype=group_visibility
		
		List<NameValuePair> param = new ArrayList<>();
		param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
		param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "group_visibility"));
	
		param.add(new BasicNameValuePair("userid", ""+userid));
		
		param.add(new BasicNameValuePair("groupid", ""+groupid));
		
		param.add(new BasicNameValuePair("visibility", ""+visibility));
		
		
		  response = (new WebServiceHandler()).makeServiceCall(
		 GlobalConstant.URL, WebServiceHandler.GET, param);
		
		 
		 
		 
		
		return response;
	}
	@Override
	protected void onPostExecute(String result) {
	
		
		if(result.contains(GlobalConstant.SUCCESS))
		{
//			GlobalUtills.showToast("Visibility hidden..!",con);
			  SharedPreferences preferences=con.getSharedPreferences("Chat", Context.MODE_PRIVATE);
			  Editor ed=preferences.edit();
			  ed.putBoolean("V"+groupid, visiisi);
			  ed.commit();


			Intent intent = new Intent(com.app.util.GlobalConstant.BROADCAST_UPDATELIST_MYGROUPS);
			con.sendBroadcast(intent);
		}
		else
		{
			GlobalUtills.showToast("Oops..an Error has occur while setting visibilty..!",con);
		}
		
		
		
		super.onPostExecute(result);
	}

	
}
