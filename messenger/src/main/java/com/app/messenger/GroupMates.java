
//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.adapter.ShowSocialFriends;
import com.app.model.FriendInfo;
import com.app.util.GlobalUtills;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.WebServiceHandler;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.List;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//==============================================================================================================================
public class GroupMates extends Activity
{

    //--------------------------------------------------------------------------------------------------------------------------
    class GroupMatesGetter extends AsyncTask<String, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            usersList_.clear();

            progressDialog_ = new TransparentProgressDialog(GroupMates.this, com.app.messenger.R.drawable.loading_spinner_icon);

            progressDialog_.show();

            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "get_active_members"));
            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, global_.getUser_id() + ""));

            String jsonString = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);

            if (jsonString.equals(GlobalConstant.ERROR_CODE_STRING))
                return jsonString;

            else
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(jsonString);

                    message_ = jsonObject.getString(GlobalConstant.MESSAGE);

                    JSONArray groupListInfo = jsonObject.getJSONArray("user_info");  // TODO: name & param mismatch


                    for (int index = 0; index < groupListInfo.length(); ++index)
                    {
                        FriendInfo friendinfo = new FriendInfo();
                        JSONObject groupInfo = groupListInfo.getJSONObject(index);

                        if (!myFacebookID_.equals(groupInfo.getString(GlobalConstant.FACE_BOOK_ID)))
                        {
                            friendinfo.setId(groupInfo.getString(GlobalConstant.FACE_BOOK_ID) + "");
                            friendinfo.setImage(groupInfo.getString("userImage") + "");
                            friendinfo.setName(groupInfo.getString("userName") + "");
                            friendinfo.setUnread_count(groupInfo.getString("unread_count"));
                            friendinfo.setMobileNumber(groupInfo.optString("user_telephone"));
                            friendinfo.setLastseen(groupInfo.optString("lastseen"));

                            usersList_.add(friendinfo);
                        }
                    }
                }
                catch (JSONException error)
                {
                    error.printStackTrace();
                }
                catch (Exception error)
                {
                    error.printStackTrace();
                }

                return message_;
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if (result.equalsIgnoreCase(GlobalConstant.SUCCESS))
            {
                ShowSocialFriends socialusers = new ShowSocialFriends(GroupMates.this, usersList_, false, true);

                groupMatesList_.setAdapter(socialusers);
            }
            else
            {
                groupMatesList_.setAdapter(null);

                GlobalUtills.showToast("No Group Mates..!", GroupMates.this);
            }

            progressDialog_.dismiss();
        }

        //----------------------------------------------------------------------------------------------------------------------
        private String message_ = "";
        private TransparentProgressDialog progressDialog_;
    }

    public static ArrayList<FriendInfo> usersList()
    {
        return usersList_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);

        global_.setUser_id(sharedPref.getString("UserID", ""));

        super.onResume();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(com.app.messenger.R.layout.activity_group_mates);

        global_ = new Global();

        SharedPreferences preferences = getSharedPreferences("fbID", MODE_PRIVATE);

        myFacebookID_ = preferences.getString("fb", "");
        groupMatesList_ = (ListView) findViewById(com.app.messenger.R.id.listVgroupMates);
        globalUtills_ = new GlobalUtills();

        if (globalUtills_.haveNetworkConnection(GroupMates.this))
            new GroupMatesGetter().execute();

        else
            GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, GroupMates.this);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static ArrayList<FriendInfo> usersList_ = new ArrayList<FriendInfo>();

    //--------------------------------------------------------------------------------------------------------------------------
    private Global global_;
    private GlobalUtills globalUtills_;
    private ListView groupMatesList_;
    private String myFacebookID_ = "";
}
