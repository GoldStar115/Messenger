//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.model.FriendInfo;
import com.app.util.GPSTracker;
import com.app.util.GlobalUtills;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.WebServiceHandler;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;  // TODO: remove deprecated classes.
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//==============================================================================================================================
public class UsersAroundMe extends Activity
{

    //--------------------------------------------------------------------------------------------------------------------------
    // get user around me
    class UsersAroundMeGetter extends AsyncTask<String, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            usersAroundMe_.clear();

            progressDialog_ = new TransparentProgressDialog(UsersAroundMe.this,
                                                            com.app.messenger.R.drawable.loading_spinner_icon);

            progressDialog_.show();

            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));

            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE,    "uaround_me"));
            param.add(new BasicNameValuePair("radius",                 distance_ + ""));
            param.add(new BasicNameValuePair(GlobalConstant.LATITUDE,  Global.lati + ""));
            param.add(new BasicNameValuePair(GlobalConstant.LONGITUDE, Global.longi + ""));
            param.add(new BasicNameValuePair(GlobalConstant.USER_ID,   global_.getUser_id() + ""));

            String jsonString = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);

            if (jsonString.equals(GlobalConstant.ERROR_CODE_STRING))
                return jsonString;

            else
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(jsonString);

                    response_ = jsonObject.getString(GlobalConstant.MESSAGE);

                    if (response_.equalsIgnoreCase(GlobalConstant.SUCCESS))
                    {
                        String    stringUsersInfo = jsonObject.getString("userInfo");
                        JSONArray jsonUsersInfo   = new JSONArray(stringUsersInfo);

                        for (int index = 0; index < jsonUsersInfo.length(); ++index)
                        {
                            FriendInfo nearByUsers  = new FriendInfo();
                            JSONObject userInfo     = jsonUsersInfo.getJSONObject(index);

                            nearByUsers.setId          (userInfo.getString(GlobalConstant.FACE_BOOK_ID) + "");
                            nearByUsers.setImage       (userInfo.getString("userimage") + "");
                            nearByUsers.setName        (userInfo.getString("username") + "");
                            nearByUsers.setMobileNumber(userInfo.getString("user_telephone") + "");
                            nearByUsers.setUnread_count(userInfo.getString("distance") + "");  // TODO: rearrange after rename

                            if (!nearByUsers.getId().equalsIgnoreCase(faceBookID_))
                            {
                                usersAroundMe_.add(nearByUsers);
                            }


                        }
                    }
                }
                catch(JSONException error)
                {
                    error.printStackTrace();
                }

                return response_;
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            progressDialog_.dismiss();

            if (result.equals(GlobalConstant.ERROR_CODE_STRING))
                GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, UsersAroundMe.this);

            else
            {
                if (usersAroundMe_.size() > 0)
                    nearbyUsersList.setAdapter(new UsersAroundMeAdapter(UsersAroundMe.this, usersAroundMe_, false, true));

                else
                    GlobalUtills.showToast("No near by Users found ..!", UsersAroundMe.this);
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        private TransparentProgressDialog progressDialog_;
        private String                    response_       = "";
    }

    //--------------------------------------------------------------------------------------------------------------------------
    class UsersAroundMeAdapter extends BaseAdapter
    {

        //----------------------------------------------------------------------------------------------------------------------
        public UsersAroundMeAdapter(Context context, ArrayList<FriendInfo> allFriends, boolean chatOrNot,
                                    boolean groupMates)
        {
            context_    = context;
            global_          = new Global();
            allFriends_ = allFriends;

            if (!(radiosAroundMeChecked_.size() > 0))
                for (int index = 0; index < allFriends.size(); ++index)
                    radiosAroundMeChecked_.add(false);

            this.groupMates_ = groupMates;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        public int getCount()
        {
            return allFriends_.size();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        public Object getItem(int position)
        {
            return null;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        public long getItemId(int position)
        {
            return position;
        }

        //----------------------------------------------------------------------------------------------------------------------
        // TODO: huge func must be refactored.
        @Override
        public View getView(final int arg0, View view, ViewGroup parent)
        {
            try
            {
                LayoutInflater layoutInFlater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view               = layoutInFlater.inflate(R.layout.friend_adapter, null);
                currentFriendInfo_ = allFriends_.get(arg0);

                // TODO: rearrange after class Facebook_ProfilePictureView_rounded rename.
                name               = (TextView)                            view.findViewById(R.id.Friend_name);
                phoneNumber_       = (TextView)                            view.findViewById(R.id.txtV_FBphno);
                freindImage_       = (Facebook_ProfilePictureView_rounded) view.findViewById(R.id.friends_title_image);
                checkButton_       = (RadioButton)                         view.findViewById(R.id.friend_check_box);

                if (!groupMates_)
                    checkButton_.setVisibility(View.GONE);

                else
                {
                    checkButton_.setVisibility(View.VISIBLE);


                    checkButton_.setChecked(radiosAroundMeChecked_.get(arg0));


                }

                checkButton_.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        currentFriendInfo_ = allFriends_.get(arg0);

                        if (!radiosAroundMeChecked_.get(arg0))
                        {
                            ((RadioButton) view).setChecked(true);

                            radiosAroundMeChecked_.set(arg0, true);

                            identifiersMap_.put(arg0, currentFriendInfo_.getId());

                            global_.setHashMaparoundme(identifiersMap_);
                        }
                        else
                        {
                            radiosAroundMeChecked_.set(arg0, false);

                            ((RadioButton) view).setChecked(false);

                            if (identifiersMap_.size() > 0)
                            {
                                identifiersMap_.remove(arg0);

                                global_.setHashMaparoundme(identifiersMap_);
                            }
                        }
                    }
                });

                name.setText(currentFriendInfo_.getName());

                phoneNumber_.setText    ("last seen" + " " + currentFriendInfo_.getUnread_count() + " km away");
                phoneNumber_.setTypeface(null, Typeface.ITALIC);

                freindImage_.setProfileId(currentFriendInfo_.getId());
            }
            catch (Exception error)
            {
                error.printStackTrace();
            }
            catch (OutOfMemoryError error)
            {
                error.printStackTrace();
            }

            view.setBackgroundColor(arg0 % 2 != 0 ? GlobalConstant.COLOR_GREY : GlobalConstant.COLOR_WHITE);

            return view;
        }

        //----------------------------------------------------------------------------------------------------------------------
        private ArrayList<FriendInfo>               allFriends_;
        private boolean                             groupMates_    = true;
        private Context                             context_;
        private FriendInfo                          currentFriendInfo_;
        private Facebook_ProfilePictureView_rounded freindImage_;  // TODO: correct position when type will be renamed.
        private Global                              global_;
        private HashMap<Integer , String>           identifiersMap_        = new HashMap<Integer , String>();
        private RadioButton                         checkButton_;
        private TextView                            name;
        private TextView                            phoneNumber_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static ArrayList<Boolean> radiosAroundMeChecked()
    {
        return radiosAroundMeChecked_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static ArrayList<FriendInfo> usersAroundMe()
    {
        return usersAroundMe_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        global_.setUser_id(getSharedPreferences("login", MODE_PRIVATE).getString("UserID", ""));

        super.onResume();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aroundme_users);






        global_       = new Global();
        globalUtills_ = new GlobalUtills();


        GPSTracker gps_=new GPSTracker(UsersAroundMe.this);
        double latitude  = gps_.getLatitude ();
        double longitude = gps_.getLongitude();
        Global.lati  = String.valueOf(latitude);
        Global.longi = String.valueOf(longitude);




        nearbyUsersList = (ListView) findViewById(com.app.messenger.R.id.listVaroundusers_creategrouptab);

        if (globalUtills_.haveNetworkConnection(UsersAroundMe.this))
            new UsersAroundMeGetter().execute();

        else
            GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, UsersAroundMe.this);

        SharedPreferences preferences = getSharedPreferences("CountryPreferences", Context.MODE_PRIVATE);

        if (preferences.contains("km"))
            distance_ = preferences.getInt("km", 100);

        else
            distance_ = 100;  // TODO: magic number

        faceBookID_ = getSharedPreferences("fbID", MODE_PRIVATE).getString("fb", "");
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static ArrayList<Boolean>    radiosAroundMeChecked_ = new ArrayList<Boolean>();
    private static ArrayList<FriendInfo> usersAroundMe_         = new ArrayList<FriendInfo>();

    //--------------------------------------------------------------------------------------------------------------------------
    private Global       global_;
    private GlobalUtills globalUtills_;
    private ListView     nearbyUsersList;
    private int          distance_       = 1;
    private String       faceBookID_     = "";
}
