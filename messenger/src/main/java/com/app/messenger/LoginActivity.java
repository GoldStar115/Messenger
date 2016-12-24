//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.model.FriendInfo;
import com.app.util.GlobalUtills;
import com.app.util.NetworkCheck;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.WebServiceHandler;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

//------------------------------------------------------------------------------------------------------------------------------
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//==============================================================================================================================
@SuppressWarnings("deprecation")
public class LoginActivity extends Activity implements OnClickListener
{

    //--------------------------------------------------------------------------------------------------------------------------
    class RegistrationByFacebook extends AsyncTask<Void, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params)
        {
            SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);

            country_     = sharedPref.getString("country", "India");
            phoneNumber_ = sharedPref.getString("ph_no",   "2");
            userId_      = sharedPref.getString("UserID",  "2");

            List<NameValuePair> param = new ArrayList<>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE,          "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE,             "register"));
            param.add(new BasicNameValuePair(GlobalConstant.FIRST_NAME,          firstName_));
            param.add(new BasicNameValuePair(GlobalConstant.COUNTRY,            country_));
            param.add(new BasicNameValuePair(GlobalConstant.FACE_BOOK_ID,       facebookId_));
            param.add(new BasicNameValuePair(com.app.util.GlobalConstant.EMAIL, email_));
            param.add(new BasicNameValuePair(GlobalConstant.PHONE_NUMBER,       phoneNumber_));
            param.add(new BasicNameValuePair(GlobalConstant.IS_SOCIAL,          "Y"));
            param.add(new BasicNameValuePair("userId",                          userId_));

            try
            {
                WebServiceHandler web = new WebServiceHandler();

                responseG_ = web.makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            return responseG_;
        }

        //----------------------------------------------------------------------------------------------------------------------
        protected void onPostExecute(String result)
        {
            try
            {
                if (result.equals("Error! Try Again"))
                {

                    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONObject jsonObject;

                    jsonObject = new JSONObject(responseG_);
                    message_   = jsonObject.getString("Message");

                    if (message_.equals("Success"))
                    {
                        String uID = userId_;

                        global_.setUser_id(uID);

                        sendRequestDialog();
                    }
                    else  // TODO:
                        Toast.makeText(LoginActivity.this, "Error..!", Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------        private String    message_;
        private String    responseG_    = "";
        private String    country_;
        private String    phoneNumber_;
        private String    userId_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public class GetValidFriends extends AsyncTask<Void, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(Void... params)
        {
            List<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair("post_type", "post"));
            param.add(new BasicNameValuePair("mtype",     "get_valid_fb_users"));
            param.add(new BasicNameValuePair("users", fbIdCheckValidation_));

            WebServiceHandler web        = new WebServiceHandler();
            String            jsonString = web.makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);

            return jsonString;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                if (!friendInfoList_.isEmpty())
                    friendInfoList_.clear();

                JSONObject jsonResponse = new JSONObject(result);

                if (jsonResponse.getString("Message").equalsIgnoreCase("Success"))
                {
                    JSONArray jsonArr = jsonResponse.getJSONArray("users");

                    for (int g = 0; g < jsonArr.length(); ++g)
                    {
                        FriendInfo friend    = new FriendInfo();
                        JSONObject innerJson = jsonArr.getJSONObject(g);

                        friend.setId       (innerJson.getString("facebook_id"));
                        friend.setName     (innerJson.getString("user_name"));
                        friend.setMobileNumber(innerJson.getString("user_telephone"));

                        friendInfoList_.add(friend);
                    }

                    global_.setFriend_info_list(friendInfoList_);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v)
    {
        if (globalUtills_.haveNetworkConnection(LoginActivity.this))
        {
            switch (v.getId())
            {

            case R.id.facebook_login:
                if (!NetworkCheck.getConnectivityStatusString(LoginActivity.this).equalsIgnoreCase("true"))
                    NetworkCheck.openInternetDialog(LoginActivity.this);

                else
                {
                    loginToFacebook();

                    Log.e("callled fb", "methos");
                }
            }
        }
        else  // TODO:
            GlobalUtills.showToast("No network connection..!", LoginActivity.this);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            Session.getActiveSession().onActivityResult(LoginActivity.this, requestCode, resultCode, data);

            Log.e("Returned", "returned");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void gettingIds()
    {
        gmailLogin_    = (LinearLayout) findViewById(R.id.gmail_login);
        facebookLogin_ = (LinearLayout) findViewById(R.id.facebook_login);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public boolean saveCredentials(Facebook facebook)
    {
        Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();

        editor.putString(TOKEN, facebook.getAccessToken());
        editor.putLong  (EXPIRES, facebook.getAccessExpires());

        return editor.commit();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public boolean restoreCredentials(Facebook facebook)
    {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);

        facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
        facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));

        return facebook.isSessionValid();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void setFriendInfoJson()
    {
        try
        {
            if (!friendInfoList_.isEmpty())
                friendInfoList_.clear();

            ArrayList<HashMap<String, String>> friendListArray = new ArrayList<HashMap<String, String>>();

            SharedPreferences sharedPref = getSharedPreferences("FacebookFrnd", MODE_PRIVATE);
            String            friendList = sharedPref.getString("FriendList", "");
            JSONArray         jsonArr    = new JSONArray();
            JSONArray         jArr       = new JSONArray(friendList);

            for (int i = 0; i < jArr.length(); ++i)
            {
                JSONObject obj           = jArr.getJSONObject(i);
                JSONObject picture       = obj.getJSONObject("picture");
                JSONObject jsonObjectURL = picture.getJSONObject("data");

                String id = obj.getString("id");

                friendInfo_ = new FriendInfo();

                String IMAGE_URL = jsonObjectURL.getString("url");

                friendInfo_.setId(id);

                String name = obj.getString("name");

                friendInfo_.setImage(IMAGE_URL);

                HashMap<String, String> map = new HashMap<String, String>();

                map.put("id", id);

                friendListArray.add(map);

                JSONObject pnObj = new JSONObject();

                pnObj.put("userid", id);

                jsonArr.put(pnObj);

                friendInfo_.setName(name);

                if (fbIdCheckValidation_.equals(""))
                    fbIdCheckValidation_ = id + "";

                else
                    fbIdCheckValidation_ = fbIdCheckValidation_ + "," + id;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        new GetValidFriends().execute();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void postStatusImage()
    {
        facebook_.setSession(Session.getActiveSession());
        Bundle parameters = new Bundle();

        parameters.putString("name", "GetGroupy");
        parameters.putString("link", "https://play.google.com/store/apps/details?id=com.app.messenger&hl=en");
        parameters.putString("message", "Hey ..! i found get-groupy an osm application to connect with your friends..");

        facebook_.dialog(LoginActivity.this, "feed", parameters, new DialogListener()
        {
            @Override
            public void onComplete(Bundle values)
            {
                viewDetails();

                System.out.println(values.toString());
            }

            @Override
            public void onCancel()
            {
                viewDetails();

            }

            @Override
            public void onFacebookError(FacebookError e)
            {
                viewDetails();

            }

            @Override
            public void onError(DialogError e)
            {
                viewDetails();

            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        super.onResume();

        fbSession_ = Session.getActiveSession();

        if (fbSession_ != null && fbSession_.isOpened())
        {
            loginButton_.setVisibility(View.GONE);

            progressDialog_ = new TransparentProgressDialog(LoginActivity.this, R.drawable.loading_spinner_icon);

            String fbToken = "" + fbSession_.getAccessToken();

            global_  = new Global();

            global_.setFbtoken(fbToken);


            postStatusImage();
        }
        else  // TODO:
            loginButton_.setVisibility(View.VISIBLE);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_screen);
        context_             = LoginActivity.this;
        global_       = new Global();
        globalUtills_ = new GlobalUtills();

        gettingIds();

        loginButton_ = (LoginButton) findViewById(R.id.login_button);

        loginButton_.setPublishPermissions(Arrays.asList(new String[ ] {"email", "publish_actions", "public_profile",
                                                                        "read_friendlists", "user_friends" }));
        loginButton_.setApplicationId     (FACEBOOK_APP_ID);

        gmailLogin_.setOnClickListener(this);
        facebookLogin_.setOnClickListener(this);

        facebook_ = new Facebook(FACEBOOK_APP_ID);

        restoreCredentials(facebook_);

        asyncRunner_ = new AsyncFacebookRunner(facebook_);
//        getPackageInfo();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private static Session openActiveSession(Activity activity, boolean allowLoginUI, Session.StatusCallback callback,
                                             List<String> permissions)
    {
        Session.OpenRequest openRequest = new Session.OpenRequest(activity).setPermissions(permissions).setCallback(callback);
        Session             session     = new Session.Builder(activity).build();

        if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || allowLoginUI)
        {
            Session.setActiveSession(session);

            session.openForRead(openRequest);

            return session;
        }

        return null;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void loginToFacebook()
    {
        Session session = new Session(LoginActivity.this);

        Session.setActiveSession(session);

        Session.OpenRequest request = new Session.OpenRequest(LoginActivity.this);

        request.setPermissions  (Arrays.asList("public_profile", "email", "user_friends", "user_birthday"));
        request.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
        request.setCallback     (new Session.StatusCallback()
        {
            public void call(Session session, SessionState state, Exception exception)
            {
                if (session.isOpened())
                {

                    progressDialog_ = new TransparentProgressDialog(LoginActivity.this, R.drawable.loading_spinner_icon);

                    progressDialog_.show();

                    String fbToken = "" + session.getAccessToken();

                    global_.setFbtoken(fbToken);


                    viewDetails();
                }
                else
                {
                    // TODO:
                }
            }
        });

        session.openForRead(request);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void viewDetails()
    {
        List<String> permissions = new ArrayList<String>();

        permissions.add("email");

        Request request = Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback()
        {
            @Override
            public void onCompleted(GraphUser user, Response response)
            {
                if (user != null)
                {
                    try
                    {
                        progressDialog_.dismiss();

                        saveCredentials(facebook_);

                        fbId_       = user.getId().toString();
                        firstName_  = user.getFirstName();
                        facebookId_ = user.getId().toString();
                        email_      = user.getProperty("email").toString();

                        getFriends();
                    }
                    catch (Exception e)
                    {
                        Log.d("Error", "Error parsing returned user data.");

                        if (progressDialog_.isShowing())
                            progressDialog_.dismiss();
                    }

                    if (NetworkCheck.checkInternetConnection(context_) == true)
                    {
                        SharedPreferences sharedPref = getSharedPreferences("login", MODE_PRIVATE);
                        Editor            editorPref = sharedPref.edit();

                        String regPhoneNumber = sharedPref.getString("ph_no", "----");

                        System.out.println("My facebook id is " + fbId_);
                        System.out.println("My email_id id is " + email_);
                        System.out.println("My global.email_id is " + GlobalUtills.email_id);

                        if (!(GlobalUtills.email_id.equals("")) && !(email_.equals(GlobalUtills.email_id)))
                        {
                            GlobalUtills.showToast(email_ + " is not associated with the " + regPhoneNumber + " number.",
                                                   LoginActivity.this);

                            Session fbSession = Session.getActiveSession();

                            Intent intent = new Intent(LoginActivity.this, PhoneNumberRegistertationScreen.class);

                            startActivity(intent);

                            LoginActivity.this.finish();

                            fbSession = Session.getActiveSession();

                            if (fbSession.isOpened())
                                fbSession.close();
                        }
                        else
                        {
                            editorPref.putString("FbID",  fbId_ + "");
                            editorPref.putString("email", email_ + "");
                            editorPref.commit   ();

                            SharedPreferences sharedPrefFb = getSharedPreferences("fbID", MODE_PRIVATE);
                            Editor            ed           = sharedPrefFb.edit();

                            ed.putString("fb", fbId_);
                            ed.commit   ();

                            if (progressDialog_.isShowing())
                                progressDialog_.dismiss();

                            new RegistrationByFacebook().execute();

                            GlobalUtills.showToast("Logged in successfully", LoginActivity.this);

                            Intent intent = new Intent(LoginActivity.this, Tab.class);

                            startActivity(intent);

                            finish();
                        }
                    }
                    else  // TODO:
                        GlobalUtills.showToast("Check your internet connection", LoginActivity.this);
                }
                else if (response.getError() != null)
                {
                    // TODO:
                }
            }
        });

        request.executeAsync();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void getFriends()
    {
        Session activeSession = Session.getActiveSession();

        if (activeSession.getState().isOpened())
        {
            Request friendRequest = Request.newMyFriendsRequest(activeSession, new GraphUserListCallback()
            {
                public void onCompleted(List<GraphUser> users, Response response)
                {
                    String friendList = response.getGraphObject().getProperty("data").toString();

                    SharedPreferences sharedPref = getSharedPreferences("FacebookFrnd", MODE_PRIVATE);
                    Editor            editorPref = sharedPref.edit();

                    editorPref.clear    ();
                    editorPref.commit   ();
                    editorPref.putString("FriendList", friendList);
                    editorPref.commit   ();

                    setFriendInfoJson();
                }
            });

            Bundle params = new Bundle();
            params.putString("fields", "id,name,email,picture,gender");

            friendRequest.setParameters(params);
            friendRequest.executeAsync ();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void sendRequestDialog()
    {
        Bundle params = new Bundle();

        params.putString("title", "get groupy");
        params.putString("message", "hi i am using groupy");

        WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(LoginActivity.this, Session.getActiveSession(), params))
                                   .setOnCompleteListener(new OnCompleteListener()
        {
            @Override
            public void onComplete(Bundle values, FacebookException error)
            {
                if (error != null)
                {
                    if (error instanceof FacebookOperationCanceledException)
                    {
                        // TODO:
                    }
                    else
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final String requestId = values.getString("request");

                    if (requestId != null)
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();

                    else
                    {
                        // TODO:
                    }
                }

                Intent intent = new Intent(LoginActivity.this, Tab.class);

                startActivity(intent);

                finish();
            }

        }).build();

        requestsDialog.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

    //--------------------------------------------------------------------------------------------------------------------------
    private static final String TOKEN           = "access_token";
    private static final String EXPIRES         = "expires_in";
    private static final String KEY             = "facebook-credentials";
    public static final String FACEBOOK_APP_ID = "1409731509334926";

    //--------------------------------------------------------------------------------------------------------------------------
    private LoginButton               loginButton_;
    private LinearLayout              gmailLogin_;
    private LinearLayout              facebookLogin_;
    private FriendInfo                friendInfo_;
    private Context                   context_ ;
    private Facebook                  facebook_;
    private String                    facebookId_;
    private AsyncFacebookRunner       asyncRunner_;
    private String                    fbId_;
    private String                    firstName_ = "";
    private String                    email_ = "";
    private String                    message_ = "";
    private TransparentProgressDialog progressDialog_;
    private Global                    global_;
    private ArrayList<FriendInfo>     friendInfoList_      = new ArrayList<FriendInfo>();
    private String                    fbIdCheckValidation_ = "";
    private Session                   fbSession_           = null;
    private GlobalUtills              globalUtills_;
}
