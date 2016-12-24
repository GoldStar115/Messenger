//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.adapter.ShowSocialFriends;
import com.app.adapter.WorldGroupAdapter;
import com.app.model.FriendInfo;
import com.app.model.GroupInfo;
import com.app.util.GPSTracker;
import com.app.util.GlobalUtills;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.WebServiceHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;  // TODO: remove deprecated classes
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//==============================================================================================================================
public class AroundMeActivity extends ActivityInTab {

    //--------------------------------------------------------------------------------------------------------------------------
    // ASYNC CLASS TO get nearby groups
    public class NearbyGroupsGetter extends AsyncTask<String, Void, String> {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            buttonGroupsAround_.setTextColor(getResources().getColor(R.color.pinkNew));
            buttonUsersAround_.setTextColor(GlobalConstant.COLOR_BLACK);

            viewGroupsAround_.setVisibility(View.VISIBLE);
            viewUsersAround_.setVisibility(View.INVISIBLE);

            progressDialog_ = new TransparentProgressDialog(AroundMeActivity.this, R.drawable.loading_spinner_icon);

            if (nearbyGroups_.isEmpty())
                progressDialog_.show();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));

            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "user_around_me"));
            param.add(new BasicNameValuePair("radius", distance_ + ""));
            param.add(new BasicNameValuePair(GlobalConstant.LATITUDE, Global.lati + ""));
            param.add(new BasicNameValuePair(GlobalConstant.LONGITUDE, Global.longi + ""));
            param.add(new BasicNameValuePair(GlobalConstant.UPDATION_USER_ID, global_.getUser_id() + ""));


            // TODO: this looks like copy/paste from UsersAroundMe.java
            String jsonString = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);

            if (jsonString.equals(GlobalConstant.ERROR_CODE_STRING))
                return jsonString;

            else {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);

                    response_ = jsonObject.getString(GlobalConstant.MESSAGE);

                    if (response_.equalsIgnoreCase(GlobalConstant.SUCCESS)) {
                        String stringGroupsInfo = jsonObject.getString("groupInfo");
                        JSONArray jsonGroupsInfo = new JSONArray(stringGroupsInfo + "");

                        nearbyGroups_.clear();

                        for (int index = 0; index < jsonGroupsInfo.length(); ++index) {
                            GroupInfo groupInfo = new GroupInfo();
                            JSONObject jsonGroup = jsonGroupsInfo.getJSONObject(index);

                            groupInfo.setgroupId(jsonGroup.getString("groupId"));
                            groupInfo.setgroupName(jsonGroup.getString("groupName"));
                            groupInfo.setgroupImage(jsonGroup.getString("groupImage"));
                            groupInfo.setgroupTotalMembers(jsonGroup.getString("groupTotalMembers"));
                            groupInfo.setGroupType(jsonGroup.getString("groupType"));
                            groupInfo.setGroupPassword(jsonGroup.getString("groupPassword"));
                            groupInfo.setGroupTotalmsgs(jsonGroup.getString("groupMessageCount"));
                            groupInfo.setGroupTotalnew(jsonGroup.getString("groupLastMessageCount"));
                            groupInfo.setUserStatus(jsonGroup.getString("user_status"));

                            nearbyGroups_.add(groupInfo);
                        }
                    }
                } catch (JSONException error) {
                    error.printStackTrace();
                }

                return response_;
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (progressDialog_.isShowing())
                progressDialog_.dismiss();

            if (result.equals(GlobalConstant.ERROR_CODE_STRING)) // TODO: copy/paste
                GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, AroundMeActivity.this);

            else {
                if (nearbyGroups_.size() <= 0)
                    GlobalUtills.showToast(GlobalConstant.ERROR_NO_NEAR_BY_GROUP_FOUND, AroundMeActivity.this);

                else {
                    nearbyAdapter_ = new WorldGroupAdapter(AroundMeActivity.this, nearbyGroups_);

                    listViewAround_.setAdapter(nearbyAdapter_);
                }
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        TransparentProgressDialog progressDialog_;
        String response_ = "";
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // ASYNC CLASS TO get nearby groups
    public class NearbyUsersGetter extends AsyncTask<String, Void, String>  // TODO: copy/paste
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            buttonGroupsAround_.setTextColor(GlobalConstant.COLOR_BLACK);
            buttonUsersAround_.setTextColor(getResources().getColor(R.color.pinkNew));

            viewGroupsAround_.setVisibility(View.INVISIBLE);
            viewUsersAround_.setVisibility(View.VISIBLE);

            progressDialog_ = new TransparentProgressDialog(AroundMeActivity.this, R.drawable.loading_spinner_icon, true);

            progressDialog_.show();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));

            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "uaround_me"));
            param.add(new BasicNameValuePair("radius", distance_ + ""));
            param.add(new BasicNameValuePair(GlobalConstant.LATITUDE, gps.getLatitude() + ""));
            param.add(new BasicNameValuePair(GlobalConstant.LONGITUDE, gps.getLongitude() + ""));
            param.add(new BasicNameValuePair(GlobalConstant.USER_ID, global_.getUser_id() + ""));


            String jsonString = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);


            if (jsonString.equals(GlobalConstant.ERROR_CODE_STRING))
                return jsonString;

            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                response_ = jsonObject.getString(GlobalConstant.MESSAGE);

                if (response_.equalsIgnoreCase(GlobalConstant.SUCCESS)) {
                    String stringUsersInfo = jsonObject.getString("userInfo");
                    JSONArray jsonUsersInfo = new JSONArray(stringUsersInfo + "");

                    nearbyUsers_.clear();

                    for (int index = 0; index < jsonUsersInfo.length(); ++index) {
                        FriendInfo nearByUsers = new FriendInfo();
                        JSONObject jsonUserInfo = jsonUsersInfo.getJSONObject(index);

                        nearByUsers.setId(jsonUserInfo.getString(GlobalConstant.FACE_BOOK_ID) + "");
                        nearByUsers.setImage(jsonUserInfo.getString("userimage") + "");
                        nearByUsers.setName(jsonUserInfo.getString("username") + "");
                        nearByUsers.setMobileNumber(jsonUserInfo.getString("user_telephone") + "");
                        nearByUsers.setUnread_count(jsonUserInfo.getString("distance") + "");

//                        nearByUsers.setLastseen(jsonUserInfo.optString("lastseen"));

                        if (!nearByUsers.getId().equalsIgnoreCase(faceBookID_))
                            nearbyUsers_.add(nearByUsers);
                    }
                }
            } catch (JSONException error) {
                error.printStackTrace();
            }

            return response_;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);

            new CountDownTimer(2000, 4000)  // TODO: magic numbers
            {
                @Override
                public void onTick(long arg0) {
                }

                @Override
                public void onFinish() {
                    if (result.equals(GlobalConstant.ERROR_CODE_STRING))  // TODO: copy/paste
                        GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, AroundMeActivity.this);

                    else {
                        if (nearbyUsers_.size() > 0)
                            listViewUsersAround_.setAdapter(new ShowSocialFriends(AroundMeActivity.this, nearbyUsers_, false,
                                    false));

                        else
                            GlobalUtills.showToast(GlobalConstant.ERROR_NO_NEAR_BY_USERS_FOUND, AroundMeActivity.this);
                    }

                    if (progressDialog_.isShowing())
                        progressDialog_.dismiss();
                }
            }.start();// TODO: wait for 4000ms
        }

        //----------------------------------------------------------------------------------------------------------------------
        TransparentProgressDialog progressDialog_;
        String response_ = "";
    }

    //--------------------------------------------------------------------------------------------------------------------------
    class Join_worldGroup_Aroundme extends AsyncTask<String, String, String> {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params) {
            String GroupID = params[0];

            List<NameValuePair> param = new LinkedList<>();  // TODO: deprecated.

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "join_group"));
            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, global_.getUser_id()));
            param.add(new BasicNameValuePair(GlobalConstant.GROUP_ID, GroupID));

            try {
                joinWorldGroupResponse_ = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET,
                        param);

                Log.e("Response of Join Group", "" + joinWorldGroupResponse_.toString());
            } catch (Exception exception) {
                exception.printStackTrace();

                System.out.println("Exception calling");
            }

            return joinWorldGroupResponse_;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog_ = new TransparentProgressDialog(AroundMeActivity.this, R.drawable.loading_spinner_icon);

            dialog_.show();
        }

        //----------------------------------------------------------------------------------------------------------------------
        protected void onPostExecute(String result) {
            dialog_.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(result);

                messaString_ = jsonObject.getString(GlobalConstant.MESSAGE);

                if (messaString_.equalsIgnoreCase(GlobalConstant.SUCCESS))
                    globalUtills_.DialogOK(AroundMeActivity.this,
                            "Join request sent", "If members of this group will accept your request,then you will become member of this group.");

                else
                    Toast.makeText(AroundMeActivity.this, messaString_, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        TransparentProgressDialog dialog_;
        String joinWorldGroupResponse_;
        String messaString_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void Gotochat(final int position) {
        if ((nearbyGroups_.get(position).getGroupType()).equalsIgnoreCase("PV") &&
                (nearbyGroups_.get(position).getUserStatus()).equalsIgnoreCase("N"))
            openLogoutAlert(position);

        else {
            GlobalUtills.joinORadd_group = nearbyGroups_.get(position).getUserStatus().equalsIgnoreCase("Y");

            if ((nearbyGroups_.get(position).getUserStatus()).equalsIgnoreCase("N"))
                GlobalUtills.mygroups_savelocal = false;

            Intent gotoChatting = new Intent(AroundMeActivity.this, GroupChat.class);

            gotoChatting.putExtra("groupID", nearbyGroups_.get(position).getgroupId());
            gotoChatting.putExtra("groupName", nearbyGroups_.get(position).getgroupName());
            gotoChatting.putExtra("groupImage", nearbyGroups_.get(position).getgroupImage() + "");

            startActivity(gotoChatting);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // TODO: huge method.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_activity);

        global_ = new Global();
        gps = new GPSTracker(AroundMeActivity.this);
        gps.getLocation();


        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        actionBarCommon_ = (ActionBarCommon) findViewById(R.id.action_bar);  // TODO: rearrange if possible.

        actionBarCommon_.setActionText("Around Me");

        listViewAround_ = (ListView) findViewById(R.id.listVAroundgroup);
        listViewUsersAround_ = (ListView) findViewById(R.id.listVAroundMembers);
        buttonUsersAround_ = (Button) findViewById(R.id.btnAroundmefriends);
        buttonGroupsAround_ = (Button) findViewById(R.id.btnAroundmeGroups);
        viewGroupsAround_ = (View) findViewById(R.id.viewbtnAroundmeGroups);
        viewUsersAround_ = (View) findViewById(R.id.viewbtnAroundmefriends);

        SharedPreferences sharedPreferences = getSharedPreferences("fbID", MODE_PRIVATE);

        faceBookID_ = sharedPreferences.getString("fb", "");

        listViewAround_.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (globalUtills_.haveNetworkConnection(AroundMeActivity.this))
                    Gotochat(position);

                else
                    GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, AroundMeActivity.this);  // TODO: copy/paste
            }
        });

        listViewUsersAround_.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gotoChatting = new Intent(AroundMeActivity.this, ChatOneToOne.class);

                gotoChatting.putExtra("name", nearbyUsers_.get(position).getName() + "");
                gotoChatting.putExtra("fbID", nearbyUsers_.get(position).getId() + "");
                gotoChatting.putExtra(GlobalConstant.PHONE_NUMBER, nearbyUsers_.get(position).getMobile_no() + "");

                startActivity(gotoChatting);
            }
        });

        buttonUsersAround_.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listViewUsersAround_.getVisibility() != View.VISIBLE) {
                    listViewAround_.setVisibility(View.GONE);
                    listViewUsersAround_.setVisibility(View.VISIBLE);

                    if (globalUtills_.haveNetworkConnection(AroundMeActivity.this)) {
                        listViewUsersAround_.setAdapter(null);

                        new NearbyUsersGetter().execute();
                    } else {
                        if (nearbyUsers_.size() > 0)
                            listViewUsersAround_.setAdapter(new ShowSocialFriends(AroundMeActivity.this, nearbyUsers_, false,
                                    false));

                        GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, AroundMeActivity.this);
                    }
                }
            }
        });

        buttonGroupsAround_.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listViewAround_.getVisibility() != View.VISIBLE) {
                    listViewAround_.setVisibility(View.VISIBLE);
                    listViewUsersAround_.setVisibility(View.GONE);

                    if (globalUtills_.haveNetworkConnection(AroundMeActivity.this))
                        new NearbyGroupsGetter().execute();

                    else {
                        if (nearbyGroups_.size() > 0) {
                            nearbyAdapter_ = new WorldGroupAdapter(AroundMeActivity.this, nearbyGroups_);

                            listViewAround_.setAdapter(nearbyAdapter_);
                        }

                        GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, AroundMeActivity.this);
                    }
                }
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        super.onResume();

        global_ = new Global();
        globalUtills_ = new GlobalUtills();
        preferences_ = getSharedPreferences("CountryPreferences", Context.MODE_PRIVATE);

        if (preferences_.contains("km"))
            distance_ = preferences_.getInt("km", 100);

        else
            distance_ = 100;  // TODO:

        if (listViewAround_.getVisibility() == View.VISIBLE) {
            if (nearbyGroups_.size() <= 0)
                new NearbyGroupsGetter().execute();

            else {
                buttonGroupsAround_.setTextColor(getResources().getColor(R.color.pinkNew));
                buttonUsersAround_.setTextColor(GlobalConstant.COLOR_BLACK);

                viewGroupsAround_.setVisibility(View.VISIBLE);
                viewUsersAround_.setVisibility(View.INVISIBLE);
            }
        } else {
            listViewUsersAround_.setAdapter(null);
            if (nearbyUsers_.size() > 0) {
                buttonGroupsAround_.setTextColor(GlobalConstant.COLOR_BLACK);
                buttonUsersAround_.setTextColor(getResources().getColor(R.color.pinkNew));

                viewGroupsAround_.setVisibility(View.INVISIBLE);
                viewUsersAround_.setVisibility(View.VISIBLE);
            }

            new NearbyUsersGetter().execute();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    void openLogoutAlert(final int position) {
        final Dialog dialog = globalUtills_.prepararDialog(AroundMeActivity.this, R.layout.dialog_three_options);

        TextView title = (TextView) dialog.findViewById(R.id.txtVmainTitle);

        title.setText("Join Group");

        TextView subheading = (TextView) dialog.findViewById(R.id.txtVsubheading);

        subheading.setText("You are not a member of this closed group..!");

        Button buttonChat = (Button) dialog.findViewById(R.id.btnChat);
        Button buttonCall = (Button) dialog.findViewById(R.id.btncall);
        Button buttonGroups = (Button) dialog.findViewById(R.id.btngroups);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.btnCloseDialog);

        buttonClose.setVisibility(View.GONE);
        buttonGroups.setVisibility(View.GONE);

        buttonCall.setText("Join");
        buttonChat.setText("Cancel");

        buttonChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new Join_worldGroup_Aroundme().execute(nearbyGroups_.get(position).getgroupId());

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private ActionBarCommon actionBarCommon_;
    private ArrayList<GroupInfo> nearbyGroups_ = new ArrayList<GroupInfo>();
    private ArrayList<FriendInfo> nearbyUsers_ = new ArrayList<FriendInfo>();
    private Button buttonGroupsAround_;
    private Button buttonUsersAround_;
    private Global global_;
    private GlobalUtills globalUtills_;
    private int distance_ = 0;
    private ListView listViewAround_;  // TODO: what is around?
    private ListView listViewUsersAround_;
    private SharedPreferences preferences_;
    private String faceBookID_ = "";
    private View viewGroupsAround_;
    private View viewUsersAround_;
    private WorldGroupAdapter nearbyAdapter_;

    private GPSTracker gps;
}
