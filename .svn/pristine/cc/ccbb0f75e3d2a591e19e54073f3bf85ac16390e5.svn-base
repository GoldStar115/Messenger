package com.app.webserviceshandler;

import android.content.Context;
import android.os.AsyncTask;

import com.app.messenger.GlobalConstant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gagandeep on 24/9/15.
 */
public class SetLastSeen extends AsyncTask<String, Void, Void>
{

    //----------------------------------------------------------------------------------------------------------------------
    private String jsonString_ = "",user_id="",is_online;

//    is_online (Y, N)
    public SetLastSeen(String user_id,String is_online)
    {
        this.user_id = user_id;
        this.is_online=is_online;
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
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "set_last_seen"));
            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, user_id));
            param.add(new BasicNameValuePair("is_online", is_online));

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


        try {
            JSONObject jsonObject = new JSONObject(jsonString_);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//            String message = jsonObject.getString(GlobalConstant.MESSAGE);
//
//            if (message.equalsIgnoreCase(GlobalConstant.SUCCESS))
//            {
//
//
//            }



    }


}