//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.adapter.AddSocialFriendAdapter;
import com.app.adapter.ShowSocialFriends;
import com.app.util.GlobalUtills;
import com.app.util.NetworkCheck;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.GroupImageSend;
import com.app.webserviceshandler.WebServiceHandler;
import com.facebook.Session;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

//------------------------------------------------------------------------------------------------------------------------------
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;  // TODO: remove deprecated type
import org.apache.http.message.BasicNameValuePair;  // TODO: remove deprecated type
import org.json.JSONObject;


//==============================================================================================================================
public class AddGroupActivity extends Activity implements OnItemSelectedListener
{

    //--Create Group Webservice ------------------------------------------------------------------------------------------------
    class CreateGroup extends AsyncTask<String, String, String>  // TODO: move this class out from external class if possible.
    {
        //----------------------------------------------------------------------------------------------------------------------
        // TransparentProgressDialog dialog;
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                // TODO: remove deprecated constructors and classes.
                ArrayList<NameValuePair> param = new ArrayList<>();

                param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
                param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "adgroup"));
                param.add(new BasicNameValuePair(GlobalConstant.GROUP_NAME, groupName_));
                param.add(new BasicNameValuePair(GlobalConstant.GROUP_TYPE, spinerText_));
                param.add(new BasicNameValuePair(GlobalConstant.USER_ID, global_.getUser_id()));
                param.add(new BasicNameValuePair(GlobalConstant.GROUP_USERS_ID, appendedFriendIDs_));
                param.add(new BasicNameValuePair(GlobalConstant.SEARCH_KEYWOARD, searchKeyword_));
                param.add(new BasicNameValuePair(GlobalConstant.GROUP_IMAGE, ""));
                param.add(new BasicNameValuePair("password", password_ + ""));
                param.add(new BasicNameValuePair("phones", appendedPhoneNumbers_ + ""));

                Log.e("sending parameters", "" + param.toString());

                return (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();

                System.out.println("Exception calling");

                return "";
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            buttonCreateGroup_.setVisibility(View.INVISIBLE);

            dialog_ = new TransparentProgressDialog(AddGroupActivity.this, R.drawable.loading_spinner_icon);

            dialog_.show();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            try
            {
                JSONObject jsonObject = new JSONObject(result);

                if (!jsonObject.getString(GlobalConstant.MESSAGE).equalsIgnoreCase(GlobalConstant.SUCCESS))
                    resetDialog(GlobalConstant.ERROR_GROUP_NOT_CREATED);

                else
                {
                    String groupId = jsonObject.getString("groupId");

                    if (imageBytesGroupIcon_.length() > 1)
                    {
                        new GroupImageSend(imageBytesGroupIcon_, dialog_, AddGroupActivity.this, groupName_).execute(groupId);
                        buttonCreateGroup_.setVisibility(View.VISIBLE);

                    }
                    else
                    {
                        onCreateGroupSucceed(groupName_);
                    }
                }
            }
            catch (Exception error)
            {
                resetDialog(GlobalConstant.ERROR_GROUP_NOT_CREATED);

                error.printStackTrace();
            }

            friendID_.clear();
            phoneNumber_.clear();
        }
    }

    //--send image--------------------------------------------------------------------------------------------------------------


    //--------------------------------------------------------------------------------------------------------------------------
    interface ObjectViewGetter
    {
        View getView(String parameter);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed()
    {
        resetControls();

        super.onBackPressed();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        selectedTextView_.setText(state_[position]);

        spinerText_ = state_[position];

        System.out.println(spinerText_ + "spinner");

        if (spinerText_.equalsIgnoreCase("Public"))  // TODO: it's not good to keep strings and compare for them. Enums are pref
        {
            spinerText_ = "P";

            editGroupPswd_.setVisibility(View.GONE);
        }
        else if (spinerText_.equalsIgnoreCase("Private"))
        {
            spinerText_ = "PV";

            editGroupPswd_.setVisibility(View.VISIBLE);
        }
        else
            spinerText_ = "PV";

        System.out.println(spinerText_ + "spinner");
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("pic", imageBytesGroupIcon_);

        super.onSaveInstanceState(outState);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        String pic = savedInstanceState.getString("pic");

        if (pic.length() > 1)
        {
            imageBytesGroupIcon_ = pic;
            imageEditProfile_.setImageBitmap(Facebook_ProfilePictureView_rounded.getRoundedBitmap(
                    globalUtills_.StringToBitMap(imageBytesGroupIcon_)));
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)  // TODO: huge function, must be refactored.
    {
        super.onCreate(savedInstanceState);

        // TODO: group where possible.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.create_group);


//        ACTION BAR + init view's
        actionBarCommon_ = new ActionBarCommon(AddGroupActivity.this, null);
        gettingValues();
        actionBarCommon_.setActionText("Add Group");
        actionBarCommon_.leftImage().setImageResource(R.drawable.icon_back_arrow);

        actionBarCommon_.layoutLeft().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                resetControls();

                finish();
            }
        });
//        ACTION BAR + init view's END


        hashMapGetIDs_ = global_.getHashMap();


//        SPINNER STUFF
        publicSpinner_.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line,
                state_);
        publicSpinner_.setAdapter(adapter_state);
        publicSpinner_.setOnItemSelectedListener(this);

        openSpiner_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                publicSpinner_.performClick();
            }
        });
//        SPINNER STUFF END


//ONCLICK LISTNER
        buttonCreateGroup_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!NetworkCheck.getConnectivityStatusString(AddGroupActivity.this).equalsIgnoreCase("true"))
                    NetworkCheck.openInternetDialog(AddGroupActivity.this);

                else
                {
                    for (int index = 0; index < friendID_.size(); ++index)
                        appendedFriendIDs_ += (appendedFriendIDs_.length() == 0 ? "" : ",") + friendID_.get(index);

                    for (int index = 0; index < phoneNumber_.size(); ++index)
                        appendedPhoneNumbers_ += (appendedPhoneNumbers_.length() == 0 ? "" : ",") +
                                phoneNumber_.get(index).replaceAll(" ", "").trim();

                    groupName_ = editGroupName_.getText().toString().trim();
                    searchKeyword_ = editSearchKeywoards_.getText().toString().trim();

                    if (groupName_.length() == 0)
                        showNotification(GlobalConstant.NOTIFICATION_ENTER_GROUP_NAME, editGroupName_);

                    else if (searchKeyword_.length() == 0)
                        showNotification(GlobalConstant.NOTIFICATION_ENTER_SEARCH_KEYWORD, editSearchKeywoards_);

                    else if (spinerText_.length() == 0)
                        showNotification(GlobalConstant.NOTIFICATION_SELECT_GROUP_TYPE, openSpiner_);

                    else if (appendedFriendIDs_.trim().equals(""))
                        showNotification(GlobalConstant.NOTIFICATION_ADD_AT_LEAST_ONE_FRIEND, null);

                    else if (spinerText_.equals("PV"))
                    {
                        if (editGroupPswd_.getText().toString().trim().equals(""))
                        {
                            editGroupPswd_.requestFocus();
                            editGroupPswd_.setHint(GlobalConstant.HINT_ENTER_PASSWORD);
                            editGroupPswd_.setHintTextColor(Color.RED);
                        }
                        else
                        {
                            password_ = editGroupPswd_.getText().toString().trim();

                            new CreateGroup().execute();
                        }
                    }
                    else
                        new CreateGroup().execute();
                }
            }

            private void showNotification(String notification, View viewToFocus)
            {
                if (toast_ != null)
                    toast_.cancel();

                if (viewToFocus != null)
                    viewToFocus.requestFocus();

                toast_ = Toast.makeText(AddGroupActivity.this, notification, Toast.LENGTH_LONG);

                toast_.show();
            }
        });

        imageEditProfile_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                chooseDialog();
            }
        });
//        ONCLICK END


//        FILTER TO PSWD EDITTEXT

        InputFilter[] filters = new InputFilter[1];

        filters[0] = new InputFilter()
        {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
            {
                if (end > start)
                    for (int index = start; index < end; ++index)
                        if (!acceptedChars.contains(String.valueOf(source.charAt(index))))
                            return "";

                return null;
            }

            private final String acceptedChars = new String(new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                    'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                    'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                    'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
                    '3', '4', '5', '6', '7', '8', '9', '@', '.', '_', '#',
                    '$', '%', '&', '*', '-', '+', '(', ')', '!', '"', ':',
                    ';', '/', '?', ',', '~', '`', '|', '^', '<', '>', '{',
                    '}', '[', ']', '=', '.', '\'', '\\'});
        };

        editGroupPswd_.setFilters(filters);

//        FILTER TO EDITTEXT PSWD END


    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        super.onResume();

        global_ = new Global();
        globalUtills_ = new GlobalUtills();
        hashMapGetIDs_ = global_.getHashMap();
        appendedFriendIDs_ = "";

        layoutAddedFriends_.setVisibility(View.GONE);


        global_.setUser_id(getSharedPreferences("login", MODE_PRIVATE).getString("UserID", ""));

        if (imageBytesGroupIcon_.length() > 1)  // TODO: magin num
            imageEditProfile_.setImageBitmap(
                    Facebook_ProfilePictureView_rounded.getRoundedBitmap(globalUtills_.StringToBitMap(imageBytesGroupIcon_)));










        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {


                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        getSelectedfriends();
                    }
                });

                return null;
            }
        }.execute();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Bitmap bitmap = BitmapDecoder.getFromData(requestCode, resultCode, data, getContentResolver());

        imageEditProfile_.setImageBitmap(Facebook_ProfilePictureView_rounded.getRoundedBitmap(bitmap));

        imageBytesGroupIcon_ = globalUtills_.BitMapToString(bitmap);

        super.onActivityResult(requestCode, resultCode, data);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void gettingValues()
    {
        imageBytesGroupIcon_ = "";
        global_ = new Global();
        actionBarCommon_ = (ActionBarCommon) findViewById(R.id.action_bar);
        addFriend_ = (TextView) findViewById(R.id.text_view_addFriends);
        selectedTextView_ = (TextView) findViewById(R.id.text_View_group_selection);
        buttonCreateGroup_ = (TextView) findViewById(R.id.btn_create_group);
        openSpiner_ = (ImageView) findViewById(R.id.img_drop_down_group);
        imageEditProfile_ = (ImageView) findViewById(R.id.imgV_edit_profile);
        editGroupName_ = (EditText) findViewById(R.id.edit_group_name);
        editGroupPswd_ = (EditText) findViewById(R.id.edGroupPswd);
        editSearchKeywoards_ = (EditText) findViewById(R.id.edit_txt_EnterKeyWords);
        publicSpinner_ = (Spinner) findViewById(R.id.private_public_spinner);
        layoutAddedFriends_ = (LinearLayout) findViewById(R.id.layout_added_friends);

        addFriend_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                appendedFriendIDs_ = "";
                appendedPhoneNumbers_ = "";

                Intent intent = new Intent(AddGroupActivity.this, FriendsTabBar.class);
                startActivity(intent);
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void getSelectedfriends()
    {
        friendID_.clear();
        phoneNumber_.clear();

        appendedPhoneNumbers_ = "";
        appendedFriendIDs_ = "";

        // around me
        if (UsersAroundMe.usersAroundMe().size() > 0 && UsersAroundMe.radiosAroundMeChecked().size() > 0)
            for (int index = 0; index < UsersAroundMe.usersAroundMe().size(); ++index)
                if (UsersAroundMe.radiosAroundMeChecked().get(index))
                    friendID_.add(UsersAroundMe.usersAroundMe().get(index).getId());

        // show social
        if (ShowingSocialsFreinds.allFriendList().size() > 0 && AddSocialFriendAdapter.radio_checkCONtacts.size() > 0)
            for (int index = 0; index < ShowingSocialsFreinds.allFriendList().size(); ++index)
                if (AddSocialFriendAdapter.radio_checkCONtacts.get(index))
                {
                    if (ShowingSocialsFreinds.allFriendList().get(index).getId().equals(""))
                        phoneNumber_.add(ShowingSocialsFreinds.allFriendList().get(index).getMobile_no());

                    else
                        friendID_.add(ShowingSocialsFreinds.allFriendList().get(index).getId());
                }

        // group mates
        try
        {
            if (GroupMates.usersList().size() > 0 && ShowSocialFriends.radioCheckGroup().size() > 0)
                for (int index = 0; index < GroupMates.usersList().size(); ++index)
                    if (ShowSocialFriends.radioCheckGroup().get(index))
                        friendID_.add(GroupMates.usersList().get(index).getId());
        }
        catch (Exception error)
        {
        }

        HashSet<String> hashSet = new HashSet<String>();

        hashSet.addAll(friendID_);

        friendID_.clear();
        friendID_.addAll(hashSet);

        HashSet<String> hashSetPhones = new HashSet<>();

        hashSetPhones.addAll(phoneNumber_);

        phoneNumber_.clear();
        phoneNumber_.addAll(hashSetPhones);

        if (friendID_.size() > 0)
            showAddedFriends();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void chooseDialog()
    {
        final Dialog dialogLoader = new Dialog(this, R.style.Theme_Dialog);

        dialogLoader.setTitle("Select a Image ");
        dialogLoader.setContentView(R.layout.dialogo_choose);

        dialogLoader.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        ImageButton buttonTakeGallery = (ImageButton) dialogLoader.findViewById(R.id.uiC_imgbtntake_gallery);
        ImageButton buttonTakeCamera = (ImageButton) dialogLoader.findViewById(R.id.uiC_imgbtntake_camera);
        ImageButton buttonYoutube = (ImageButton) dialogLoader.findViewById(R.id.btn_youTube);

        buttonYoutube.setVisibility(View.GONE);

        buttonTakeGallery.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogLoader.dismiss();

                Intent pickPhoto = new Intent(Intent.ACTION_PICK);

                pickPhoto.setType("image/*");

                startActivityForResult(pickPhoto, GlobalConstant.REQUEST_CODE_22);
            }
        });

        buttonTakeCamera.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogLoader.dismiss();

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, BitmapDecoder.getTemporaryUri());
                cameraIntent.putExtra("return-data", true);

                startActivityForResult(cameraIntent, GlobalConstant.REQUEST_CODE_1888);
            }
        });

        dialogLoader.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void showAddedFriends()
    {
        layoutAddedFriends_.setVisibility(View.VISIBLE);
        layoutAddedFriends_.removeAllViews();

        showAddedObjects(GlobalConstant.TEXT_SELECTED_FB_FRIENDS, friendID_, new ObjectViewGetter()
        {
            @Override
            public View getView(String parameter)
            {
                Facebook_ProfilePictureView_rounded addedFriend = new Facebook_ProfilePictureView_rounded(getApplicationContext());

                addedFriend.setProfileId(parameter);

                return addedFriend;
            }
        });

        if (phoneNumber_.size() > 0)
            showAddedObjects(GlobalConstant.TEXT_SELECTED_PHONES, phoneNumber_, new ObjectViewGetter()
            {
                @Override
                public View getView(String parameter)
                {
                    TextView number = new TextView(getApplicationContext());

                    number.setTextColor(GlobalConstant.COLOR_RED);
                    number.setText(parameter);

                    return number;
                }
            });
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void showAddedObjects(String header, ArrayList<String> stringsList, ObjectViewGetter viewGetter)
    {
        TextView title = new TextView(getApplicationContext());

        title.setTextColor(Color.BLACK);
        title.setText(header + " (" + stringsList.size() + ") :");

        layoutAddedFriends_.addView(title);

        for (int index = 0; index < stringsList.size(); ++index)
        {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

            layoutParams.gravity = Gravity.CENTER_HORIZONTAL;

            View view = viewGetter.getView(stringsList.get(index));

            view.setLayoutParams(layoutParams);

            layoutAddedFriends_.addView(view);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void resetDialog(String notification)
    {
        if (dialog_.isShowing())
            dialog_.dismiss();

        buttonCreateGroup_.setVisibility(View.VISIBLE);

        GlobalUtills.showToast(notification, AddGroupActivity.this);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void resetControls()
    {
        global_.clearhashMAp();
        global_.clearhashMAp_ph();

        phoneNumber_.clear();
        friendID_.clear();

        AddSocialFriendAdapter.radio_checkCONtacts.clear();
        ShowSocialFriends.radioCheckGroup().clear();
        UsersAroundMe.radiosAroundMeChecked().clear();

        appendedPhoneNumbers_ = "";
        appendedFriendIDs_ = "";
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void onCreateGroupSucceed(String groupName_)
    {
        hashMapGetIDs_.clear();

        resetControls();

        if (dialog_.isShowing())
            dialog_.dismiss();

        postStatusImage(groupName_, AddGroupActivity.this);

//        finish();
    }

    //-----------------------------------------------------------------------------------------------------------------------------------
    public static void postStatusImage(String msg, final Context con)
    {
        Facebook facebook_ = new Facebook(LoginActivity.FACEBOOK_APP_ID);
        ;
        Session fbSession_ = Session.getActiveSession();
        if (fbSession_ != null && fbSession_.isOpened())
        {
            facebook_.setSession(Session.getActiveSession());
            Bundle parameters = new Bundle();

            parameters.putString("name", "GetGroupy");
            parameters.putString("link", "https://play.google.com/store/apps/details?id=com.app.messenger&hl=en");
            parameters.putString("description", "Check out my exciting new group " + msg + " in GetGroupy....Stay connected..!");
//        parameters.putString("caption","Check out my exciting new group " + msg + " in GetGroupy....Stay connected..!");


            facebook_.dialog(con, "feed", parameters, new Facebook.DialogListener()
            {
                @Override
                public void onComplete(Bundle values)
                {
                    GlobalUtills.showToast(GlobalConstant.SUCCESS_GROUP_CREATED, con);
                    ((Activity) con).finish();
                }

                @Override
                public void onCancel()
                {
                    GlobalUtills.showToast(GlobalConstant.SUCCESS_GROUP_CREATED, con);
                    ((Activity) con).finish();

                }

                @Override
                public void onFacebookError(FacebookError e)
                {
                    GlobalUtills.showToast(GlobalConstant.SUCCESS_GROUP_CREATED, con);
                    ((Activity) con).finish();
                }

                @Override
                public void onError(DialogError e)
                {
                    GlobalUtills.showToast(GlobalConstant.SUCCESS_GROUP_CREATED, con);
                    ((Activity) con).finish();
                }
            });
        }
    }


    //--------------------------------------------------------------------------------------------------------------------------
    private static LinearLayout layoutAddedFriends_;
    private static ImageView imageEditProfile_;
    private static String imageBytesGroupIcon_ = "";
    private static HashMap<Integer, String> hashMapGetIDs_ = new HashMap<Integer, String>();

    //--------------------------------------------------------------------------------------------------------------------------
    // TODO: give appropriate names to variables
    private ActionBarCommon actionBarCommon_;
    private TextView addFriend_;
    private TextView buttonCreateGroup_;
    private TextView selectedTextView_;
    private Spinner publicSpinner_;
    private ImageView openSpiner_;
    private EditText editGroupName_;
    private EditText editSearchKeywoards_;
    private EditText editGroupPswd_;
    private Global global_;
    private GlobalUtills globalUtills_;
    private String searchKeyword_;
    private String groupName_;
    private String password_ = "";
    private String spinerText_ = "P";
    private String appendedFriendIDs_ = "";
    private String appendedPhoneNumbers_ = "";
    private String[] state_ = {"Public", "Private"};
    private ArrayList<String> friendID_ = new ArrayList<String>();
    private ArrayList<String> phoneNumber_ = new ArrayList<String>();
    private Toast toast_;
    private TransparentProgressDialog dialog_;
}
