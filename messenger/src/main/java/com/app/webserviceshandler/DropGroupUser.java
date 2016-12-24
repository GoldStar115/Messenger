package com.app.webserviceshandler;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.app.adapter.ShowSocialFriends;
import com.app.messenger.GlobalConstant;
import com.app.model.FriendInfo;
import com.app.util.GlobalUtills;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 Created by Gagan on 7/23/2015. */
public class DropGroupUser extends AsyncTask<String, Void, Void>
{

	//----------------------------------------------------------------------------------------------------------------------
	private String jsonString_ = "", groupId;
	FriendInfo GroupMembersData;

	private Context con;
	ShowSocialFriends socialusers;

	public DropGroupUser(FriendInfo GroupMembersData, String groupId, Context con, ShowSocialFriends socialusers)
	{
		this.GroupMembersData = GroupMembersData;
		this.con = con;
		this.groupId = groupId;
		this.socialusers = socialusers;
	}


	//----------------------------------------------------------------------------------------------------------------------
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
	}

	//----------------------------------------------------------------------------------------------------------------------
	@Override
	protected Void doInBackground(String... params)
	{
		try
		{
			List<NameValuePair> param = new ArrayList<>();

			param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
			param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "dropgroupuser"));
			param.add(new BasicNameValuePair("user_id", GroupMembersData.getUserId()));
			param.add(new BasicNameValuePair("group_id", groupId));


			jsonString_ = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	//----------------------------------------------------------------------------------------------------------------------
	@Override
	protected void onPostExecute(Void result)
	{
		super.onPostExecute(result);

		try
		{
			JSONObject jsonObject = new JSONObject(jsonString_);
			String message = jsonObject.getString(GlobalConstant.MESSAGE);

			if (message.equalsIgnoreCase(GlobalConstant.SUCCESS))
			{

				if (GlobalUtills.list_of_GroupMembers.size()<=2)
				{
					((Activity)con).finish();
				}
				else
				{
					GlobalUtills.list_of_GroupMembers.remove(GroupMembersData);
					socialusers.notifyDataSetChanged();
				}
			}
			else
			{
				Toast.makeText(con,"Oops an error has occurred.",Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e)
		{
			e.printStackTrace();
		}


	}


}
//--------------------------------------------------------------------------------------------------------------------------