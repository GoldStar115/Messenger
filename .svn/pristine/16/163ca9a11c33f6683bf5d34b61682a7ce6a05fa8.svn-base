package com.app.webserviceshandler;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.app.messenger.AddGroupActivity;
import com.app.messenger.GlobalConstant;
import com.app.util.GlobalUtills;
import com.app.util.TransparentProgressDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 Created by Gagan on 8/4/2015. */
public class GroupImageSend extends AsyncTask<String, String, String>
{


	TransparentProgressDialog dialog_;
	Context                   con;
	//----------------------------------------------------------------------------------------------------------------------
	private String responseSendImage_, imageBytesGroupIcon_,groupName;

	public GroupImageSend(String imageBytesGroupIcon_, TransparentProgressDialog dialog_, Context con,String groupName)
	{
		this.imageBytesGroupIcon_ = imageBytesGroupIcon_;
		this.dialog_ = dialog_;
		this.con = con;
		this.groupName=groupName;
	}

	//----------------------------------------------------------------------------------------------------------------------
	@Override
	protected String doInBackground(String... params)
	{
		String GroupID = params[0];


		ArrayList<NameValuePair> param = new ArrayList<>();

		param.add(new BasicNameValuePair(GlobalConstant.GROUP_ID, GroupID));
		param.add(new BasicNameValuePair(GlobalConstant.GROUP_IMAGE, imageBytesGroupIcon_));

		try
		{
			responseSendImage_ = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL + GlobalConstant.REQUEST_PARAM_UP_GROUP, WebServiceHandler.POST, param);
		} catch (Exception exception)
		{
			exception.printStackTrace();

		}

		return responseSendImage_;
	}

	//----------------------------------------------------------------------------------------------------------------------
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
	}

	//----------------------------------------------------------------------------------------------------------------------
	@Override
	protected void onPostExecute(String result)
	{
		if (dialog_.isShowing())
			dialog_.dismiss();




		AddGroupActivity.postStatusImage(groupName,con);

		super.onPostExecute(result);
	}
}