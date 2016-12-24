package com.app.webserviceshandler;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.app.messenger.GlobalConstant;
import com.app.messenger.R;
import com.app.util.GlobalUtills;
import com.app.util.TransparentProgressDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 Created by Gagan on 7/15/2015. */
public class SetSilent extends AsyncTask<Void, Void, String>
{

	Context con;

	String userid, group_id, flag;
	TransparentProgressDialog pd;

	public SetSilent(Context con, String user_id, String group_id, String flag)
	{


		this.userid = user_id;
		this.group_id = group_id;
		this.flag = flag;
		this.con = con;

	}
	@Override
	protected void onPreExecute()
	{
		pd = new TransparentProgressDialog(con, R.drawable.loading_spinner_icon);
		pd.show();

		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... params)
	{

		List<NameValuePair> param = new ArrayList<>();
		param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
		param.add(new BasicNameValuePair("mtype", "set_silent"));
		param.add(new BasicNameValuePair("group_id", "" + group_id));
		param.add(new BasicNameValuePair("user_id", "" + userid));

		param.add(new BasicNameValuePair("flag", "" + flag));


		WebServiceHandler web = new WebServiceHandler();

		return web.makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
	}

	@Override
	protected void onPostExecute(String result)
	{
		try
		{
			pd.dismiss();

			if (result.contains("Success"))
			{
//
//				GlobalUtills.showToast("Group set to silent.", con);

				Intent intent = new Intent(com.app.util.GlobalConstant.BROADCAST_UPDATELIST_MYGROUPS);
				con.sendBroadcast(intent);
			}


		} catch (Exception e)
		{

		}

		super.onPostExecute(result);
	}
}