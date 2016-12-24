//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.adapter.WorldGroupAdapter;
import com.app.model.GroupInfo;
import com.app.util.GPSTracker;
import com.app.util.GlobalUtills;
import com.app.util.NetworkCheck;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.WebServiceHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


//==============================================================================================================================
public class WorldGroupFragment extends Fragment
{

    //--------------------------------------------------------------------------------------------------------------------------
    class LocationUpdation extends AsyncTask<Void, Void, Void>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected Void doInBackground(Void... params)
        {
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();



            param.add(new BasicNameValuePair(GlobalConstant.LATITUDE,         Global.lati));
            param.add(new BasicNameValuePair(GlobalConstant.LONGITUDE,        Global.longi));
            param.add(new BasicNameValuePair(GlobalConstant.UPDATION_USER_ID, global_.getUser_id()));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE,           "ulocation"));
            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE,        "post"));

            try
            {
                (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();

                System.out.println("Exception calling");
            }

            return null;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------

    }

    //--------------------------------------------------------------------------------------------------------------------------
    class GettingWorldGroupList extends AsyncTask<String , String , String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            searchTag_ = params[0];

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair("country",                      countryName_));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE,          "filter_country"));
            param.add(new BasicNameValuePair(GlobalConstant.SEARCH_KEYWOARD, searchTag_));
            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID,    global_.getUser_id()));
            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE,       "post"));

            try
            {
                worldGroupResponse_ = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET,
                                                                                param);
            }
            catch (Exception exception)
            {
                if (dialog_.isShowing())
                    dialog_.dismiss();

                exception.printStackTrace();

                System.out.println("Exception calling");
            }

            return worldGroupResponse_;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            if (dialog_ == null)
            {
                dialog_ = new TransparentProgressDialog(getActivity(), R.drawable.loading_spinner_icon);

                if (worldGroupList_.isEmpty())
                    dialog_.show();
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            try
            {
                if (result.equals(GlobalConstant.ERROR_CODE_STRING))
                {
                    if (dialog_.isShowing())
                        dialog_.dismiss();

                    GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, getActivity());
                }
                else if (result.equals("Slow"))
                {
                    if (dialog_.isShowing())
                    {
                        dialog_.dismiss();

                        GlobalUtills.showToast("Error in connecting..!", getActivity());
                    }
                }
                else
                {
                    JSONObject jsonObject = new JSONObject(result);

                    messageString_ = jsonObject.getString(GlobalConstant.MESSAGE);

                    if (messageString_.equals(GlobalConstant.SUCCESS))
                    {
                        if (dialog_.isShowing())
                            dialog_.dismiss();

                        worldGroupList_.clear();

                        JSONArray arrayGroupInfo = jsonObject.getJSONArray("groupInfo1");

                        if (arrayGroupInfo.length() > 0)
                        {
                            for (int i = 0; i < arrayGroupInfo.length(); ++i)
                            {
                                GroupInfo groupInfo = new GroupInfo();

                                JSONObject arrayObject = arrayGroupInfo.getJSONObject(i);
                                String     groupID     = arrayObject.getString("groupId");

                                groupInfo.setgroupId(groupID);

                                String groupName = arrayObject.getString("groupName");

                                groupInfo.setgroupName(groupName);

                                String groupImage = arrayObject.getString("groupImage");

                                groupInfo.setgroupImage(groupImage);

                                String groupTotalMembers = arrayObject.getString("groupTotalMembers");

                                groupInfo.setgroupTotalMembers(groupTotalMembers);

                                String groupType = arrayObject.getString("groupType");

                                groupInfo.setGroupType(groupType);

                                String groupPassword = arrayObject.getString("groupPassword");

                                groupInfo.setGroupPassword(groupPassword);

                                groupInfo.setGroupTotalmsgs(arrayObject.getString("groupMessageCount"));
                                groupInfo.setGroupTotalnew (arrayObject.getString("groupLastMessageCount"));
                                groupInfo.setUserStatus    (arrayObject.getString("user_status"));

                                worldGroupList_.add(groupInfo);
                            }

                            if (worldAdapter_ != null)
                                listViewWorldGroup_.invalidateViews();

                            else
                            {
                                worldAdapter_ = new WorldGroupAdapter(getActivity(), worldGroupList_);

                                listViewWorldGroup_.setAdapter(worldAdapter_);
                            }
                        }
                        else
                        {
                            if (!searchTag_.equals(""))
                                worldGroupList_.clear();

                            GlobalUtills.showToast("No New Groups Found..!", getActivity());

                            try
                            {
                                worldAdapter_ = new WorldGroupAdapter(getActivity(), worldGroupList_);

                                listViewWorldGroup_.setAdapter(worldAdapter_);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        if (dialog_.isShowing())
                            dialog_.dismiss();
                    }
                }
            }
            catch (Exception e)
            {
                GlobalUtills.showToast("No New Groups Found..!", getActivity());

                if (dialog_.isShowing())
                    dialog_.dismiss();

                e.printStackTrace();
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        private String worldGroupResponse_;
        private String messageString_;
        private String searchTag_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    class JoinWorldGroupP extends AsyncTask<String, String, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        public JoinWorldGroupP(int position)
        {
            this.position_ = position;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            String groupID = params[0];

            ArrayList<NameValuePair> param = new ArrayList<>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE,    "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE,       "join_group"));
            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, global_.getUser_id()));
            param.add(new BasicNameValuePair(GlobalConstant.GROUP_ID,     groupID));

            try
            {
                responseJoinWorldGroup_ = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET,
                                                                                    param);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();

                System.out.println("Exception calling");
            }

            return responseJoinWorldGroup_;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            dialogJoin_ = new TransparentProgressDialog(getActivity(), R.drawable.loading_spinner_icon);

            dialogJoin_.show();
        }

        //----------------------------------------------------------------------------------------------------------------------
        protected void onPostExecute(String result)
        {
            dialogJoin_.dismiss();

            try
            {
                JSONObject jsonObject = new JSONObject(result);

                messageString_ = jsonObject.getString(GlobalConstant.MESSAGE);

                if (!messageString_.equalsIgnoreCase(GlobalConstant.SUCCESS))
                    Toast.makeText(getActivity(), messageString_, Toast.LENGTH_LONG).show();

                else
                {
                    if (worldGroupList_.get(position_).getGroupType().equals("PV"))
                        globalUtills_.DialogOK(getActivity(), "Join request sent", "If members of this group will accept your request,then you will become member of this group.");

                    else
                        globalUtills_.DialogOK(getActivity(), "Join", "You are now member of this group..!");

                    worldGroupList_.remove(position_);

                    listViewWorldGroup_.invalidateViews();
                }
            }
            catch (Exception e)
            {
                GlobalUtills.showToast("Oops an error has occured..!", getActivity());

                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        private TransparentProgressDialog dialogJoin_;
        private String                    responseJoinWorldGroup_;
        private String                    messageString_;
        private int                       position_               = 0;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onPause()
    {
        if (getWorldGroupList_ != null)
            getWorldGroupList_.cancel(true);

        super.onPause();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        if (container == null)
            return null;

        container        = (ViewGroup) inflater.inflate(R.layout.world_group_activity, container, false);
        actionBarCommon_ = new ActionBarCommon(getActivity(), null);

        gettingValues(container);

        actionBarCommon_.setActionText("World Groups");
        actionBarCommon_.createActionTextRight();




        Global.initAdmob(container);







        OnClickListener rightClick = new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f,
                                                           Animation.RELATIVE_TO_SELF, .5f);

                anim.setInterpolator(new LinearInterpolator());
                anim.setRepeatCount (1);
                anim.setDuration    (1700);  // TODO:

                actionBarCommon_.leftImage().setAnimation(anim);
                actionBarCommon_.leftImage().startAnimation(anim);

                new CountDownTimer(600, 600)  // TODO:
                {
                    @Override
                    public void onTick(long millisUntilFinished)
                    {
                    }

                    @Override
                    public void onFinish()
                    {
                        Intent intent = new Intent(getActivity(), SettingActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);

                        getActivity().finish();
                    }
                }.start();
            }
        };

        actionBarCommon_.leftImage().setImageResource(R.drawable.icon_setting);

        OnClickListener leftClick = new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddGroupActivity.class);

                startActivity(intent);
            }
        };

        actionBarCommon_.setLayoutLeftClickListener (rightClick);  // TODO: Functionality of these buttons are interchanged in new designs.
        actionBarCommon_.setLayoutRightClickListener(leftClick);

        removeText_.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText_.setText("");

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(searchEditText_.getWindowToken(), 0);

                getWorldGroupList_ = new GettingWorldGroupList();

                getWorldGroupList_.execute("");

                removeText_.setVisibility(View.INVISIBLE);
            }
        });

        searchEditText_.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                removeText_.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchEditText_.setOnEditorActionListener(new OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                removeText_.setVisibility(View.VISIBLE);

                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    if (!NetworkCheck.getConnectivityStatusString(getActivity()).equalsIgnoreCase("true"))
                        NetworkCheck.openInternetDialog(getActivity());

                    else
                    {
                        searchText_ = searchEditText_.getText().toString().trim();

                        if (searchText_.length() <= 0)
                            GlobalUtills.showToast("Please enter a keyword..!", getActivity());

                        else
                        {
                            InputMethodManager imm =
                                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            imm.hideSoftInputFromWindow(searchEditText_.getWindowToken(), 0);

                            new GettingWorldGroupList().execute(searchText_);
                        }
                    }
                }

                return false;
            }
        });

        listViewWorldGroup_.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
            {
                if ((worldGroupList_.get(position).getGroupType()).equalsIgnoreCase("PV") &&
                        (worldGroupList_.get(position).getUserStatus()).equalsIgnoreCase("N"))
                    openLogoutAlert(position);

                else
                {
                    GlobalUtills.joinORadd_group = worldGroupList_.get(position).getUserStatus().equalsIgnoreCase("Y");

                    Intent gotoChatting = new Intent(getActivity(), GroupChat.class);

                    gotoChatting.putExtra("groupID", worldGroupList_.get(position).getgroupId());
                    gotoChatting.putExtra("groupName", worldGroupList_.get(position).getgroupName());
                    gotoChatting.putExtra("groupImage", worldGroupList_.get(position).getgroupImage() + "");

                    GlobalUtills.mygroups_savelocal = false;

                    startActivity(gotoChatting);
                }
            }
        });

        return container;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        global_      = new Global();
        preferences_ = getActivity().getSharedPreferences("CountryPreferences", Context.MODE_PRIVATE);
        countryName_ = preferences_.getString("CountryNAme", "All");
        gps_         = new GPSTracker(getActivity());

        if (!gps_.canGetLocation())
            gps_.showSettingsAlert();

        else
        {
            double latitude  = gps_.getLatitude ();
            double longitude = gps_.getLongitude();

            lati_  = String.valueOf(latitude);
            longi_ = String.valueOf(longitude);

            Global.lati  = lati_;
            Global.longi = longi_;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new LocationUpdation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            else
                new LocationUpdation().execute();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onResume()
    {
        System.gc();

        globalUtills_ =new GlobalUtills();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        global_.setUser_id(sharedPref.getString("UserID", ""));

        preferences_ = getActivity().getSharedPreferences("CountryPreferences", Context.MODE_PRIVATE);
        countryName_ = preferences_.getString("CountryNAme", "All");



        searchEditText_.setText("");

        removeText_.setVisibility(View.INVISIBLE);

        if (globalUtills_.haveNetworkConnection(getActivity()))
        {
            if (getWorldGroupList_ != null)
                getWorldGroupList_.cancel(true);

            getWorldGroupList_ = new GettingWorldGroupList();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                getWorldGroupList_.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

            else
                getWorldGroupList_.execute("");
        }
        else
        {
            if (worldGroupList_.size() > 0)
            {
                worldAdapter_ = new WorldGroupAdapter(getActivity(), worldGroupList_);

                listViewWorldGroup_.setAdapter(worldAdapter_);
            }

            GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, getActivity());
        }

        super.onResume();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.main, menu);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId())
        {
        case R.id.itemJoin:

            if (worldGroupList_.get(info.position).getUserStatus().equalsIgnoreCase("N"))
                new JoinWorldGroupP(info.position).execute(worldGroupList_.get(info.position).getgroupId());

            else
                GlobalUtills.showToast("You are already a member..!", getActivity());

            return true;

        default:
            return super.onContextItemSelected(item);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // TODO: c/p
    public void enterPasswordDialog(final int position)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle  ("Closed Group");
        alert.setMessage("Enter Password to access this Group");

        final EditText input = new EditText(getActivity());

        input.setHint                ("Please enter password..");
        input.setInputType           (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());

        int maxLength = 15;  // TODO:

        InputFilter[] fArray = new InputFilter[1];

        fArray[0] = new InputFilter.LengthFilter(maxLength);

        input.setFilters(fArray);

        alert.setView          (input);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String             psd = input.getText().toString().trim();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                if (psd.equals(worldGroupList_.get(position).getGroupPassword()))
                {
                    GlobalUtills.joinORadd_group = false;

                    Intent gotoChatting = new Intent(getActivity(), GroupChat.class);

                    // TODO: c/p
                    gotoChatting.putExtra("groupID",    worldGroupList_.get(position).getgroupId   ());
                    gotoChatting.putExtra("groupName",  worldGroupList_.get(position).getgroupName ());
                    gotoChatting.putExtra("groupImage", worldGroupList_.get(position).getgroupImage() + "");

                    startActivity(gotoChatting);
                }
                else

                    globalUtills_.DialogOK(getActivity(), "Warning", "Wrong password..! Please try again..");
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alert.create();

        alertDialog.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void openLogoutAlert(final int positionJ)
    {
        final Dialog dialog = globalUtills_.prepararDialog(getActivity(), R.layout.dialog_three_options);

        TextView title = (TextView) dialog.findViewById(R.id.txtVmainTitle);

        title.setText("Closed Group");

        TextView subheading = (TextView) dialog.findViewById(R.id.txtVsubheading);

        subheading.setText("Its a closed group..!");

        // TODO: c/p
        Button buttonChat       = (Button)      dialog.findViewById(R.id.btnChat);
        Button buttonCall       = (Button)      dialog.findViewById(R.id.btncall);
        Button buttonGroups     = (Button)      dialog.findViewById(R.id.btngroups);
        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.btnCloseDialog);

        buttonClose.setVisibility(View.GONE);

        buttonGroups.setText("Join");
        buttonCall.setText("Enter Password");
        buttonChat.setText("Cancel");

        buttonChat.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        buttonCall.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                enterPasswordDialog(positionJ);

                dialog.dismiss();
            }
        });

        buttonGroups.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new JoinWorldGroupP(positionJ).execute(worldGroupList_.get(positionJ).getgroupId());

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void gettingValues(ViewGroup container)
    {
        listViewWorldGroup_ = (ListView) container.findViewById(R.id.list_world_group);

        listViewWorldGroup_.setScrollingCacheEnabled(false);

        registerForContextMenu(listViewWorldGroup_);

        actionBarCommon_ = (ActionBarCommon) container.findViewById(R.id.action_bar);
        searchEditText_  = (EditText)        container.findViewById(R.id.search_edit_text);
        removeText_      = (RelativeLayout)  container.findViewById(R.id.layout_cencel_img);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private ActionBarCommon           actionBarCommon_;
    private ListView                  listViewWorldGroup_;
    private String                    lati_;
    private String                    longi_;
    private SharedPreferences         preferences_;
    private RelativeLayout            removeText_;
    private WorldGroupAdapter         worldAdapter_;
    private ArrayList<GroupInfo>      worldGroupList_     = new ArrayList<GroupInfo>();
    private Global                    global_;
    private EditText                  searchEditText_;
    private String                    searchText_         = "";
    private GPSTracker                gps_;
    private String                    countryName_;
    private GettingWorldGroupList     getWorldGroupList_;
    private GlobalUtills              globalUtills_;
    private TransparentProgressDialog dialog_;
}
