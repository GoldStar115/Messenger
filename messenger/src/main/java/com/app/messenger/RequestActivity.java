//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.adapter.RequestAdapter;
import com.app.model.Request_groupmembers;
import com.app.util.GlobalUtills;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.WebServiceHandler;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.Collections;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


//==============================================================================================================================
public class RequestActivity extends Activity
{

    //--------------------------------------------------------------------------------------------------------------------------
    class GetRequests extends AsyncTask<String, String, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair("member_Id",              global_.getUser_id()));
            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE,    "get_join_request"));

            try
            {
                responseGetRequests_ = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET,
                                                                                 param);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();

                System.out.println("Exception calling");
            }

            return responseGetRequests_;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            if (requestsList_.size() > 0)
                requestsList_.clear();

            dialog_ = new TransparentProgressDialog(RequestActivity.this, R.drawable.loading_spinner_icon);

            dialog_.show();
        }

        //----------------------------------------------------------------------------------------------------------------------
        protected void onPostExecute(String result)
        {
            try
            {
                JSONObject jsonObject     = new JSONObject(result);
                JSONArray  arrayGroupInfo = jsonObject.getJSONArray("requests");

                if (arrayGroupInfo.length() <= 0)
                    GlobalUtills.showToast("No request Found..!", RequestActivity.this);

                else
                {
                    for (int i = 0; i < arrayGroupInfo.length(); ++i)
                    {
                        Request_groupmembers requesData    = new Request_groupmembers();
                        JSONObject           requestObject = arrayGroupInfo.getJSONObject(i);

                        requesData.setGroupID  (requestObject.getString("groupId"));
                        requesData.setGroupName(requestObject.getString("groupName"));
                        requesData.setUserFBid (requestObject.getString(GlobalConstant.FACE_BOOK_ID));
                        requesData.setUserID   (requestObject.getString(GlobalConstant.USER_ID));
                        requesData.setUserName (requestObject.getString("user_name"));

                        requestsList_.add(requesData);
                    }

                    Collections.reverse(requestsList_);

                    requestAdapter_ = new RequestAdapter(RequestActivity.this, requestsList_, global_.getUser_id());

                    requestList_.setAdapter(requestAdapter_);
                }

                if (dialog_.isShowing())
                    dialog_.dismiss();
            }
            catch (Exception e)
            {
                if (dialog_.isShowing())
                    dialog_.dismiss();

                GlobalUtills.showToast("No request Found..!", RequestActivity.this);

                e.printStackTrace();
            }
            catch (Error e)
            {
                GlobalUtills.showToast("No request Found..!", RequestActivity.this);

                if (dialog_.isShowing())
                    dialog_.dismiss();
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        private TransparentProgressDialog dialog_;
        private String                    responseGetRequests_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static ArrayList<Request_groupmembers> requestsList()
    {
        return requestsList_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static RequestAdapter requestAdapter()
    {
        return requestAdapter_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed()
    {
        Intent gotoSetting = new Intent(RequestActivity.this,SettingActivity.class);

        startActivity(gotoSetting);

        finish();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        try
        {
            if (globalUtills_.haveNetworkConnection(getApplicationContext()))
                new GetRequests().execute();

            else
                GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, RequestActivity.this);
        }
        catch (Exception e)
        {
            globalUtills_=new GlobalUtills();
        }

        super.onResume();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.request_activity);

        actionBarCommon_ = new ActionBarCommon(RequestActivity.this, null);

        gettingValues();

        actionBarCommon_.setActionText("Requests");

        globalUtills_ = new GlobalUtills();

        actionBarCommon_.leftImage().setImageResource(R.drawable.icon_back_arrow);

        OnClickListener left_ClickListener = new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent gotoSetting = new Intent(RequestActivity.this,SettingActivity.class);

                startActivity(gotoSetting);

                finish();
            }
        };

        actionBarCommon_.setLayoutLeftClickListener(left_ClickListener);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void gettingValues()
    {
        requestList_     = (ListView)        findViewById(R.id.listV_requests);
        actionBarCommon_ = (ActionBarCommon) findViewById(R.id.action_bar);

        global_ = new Global();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static ArrayList<Request_groupmembers> requestsList_   = new ArrayList<Request_groupmembers>();
    private static RequestAdapter                  requestAdapter_;

    //--------------------------------------------------------------------------------------------------------------------------
    private ListView        requestList_;
    private ActionBarCommon actionBarCommon_;
    private Global          global_;
    private GlobalUtills    globalUtills_;
}
