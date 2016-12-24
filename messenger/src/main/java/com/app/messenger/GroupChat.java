//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.adapter.AddSocialFriendAdapter;
import com.app.adapter.ShowSocialFriends;
import com.app.messenger.R.color;
import com.app.model.Chat_Single;
import com.app.util.DBhelperG;
import com.app.util.GlobalUtills;
import com.app.util.RoundedCornersGaganImageView;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.GetUserGroup;
import com.app.webserviceshandler.ReportAbuseUser;
import com.app.webserviceshandler.WebServiceHandler;
import com.facebook.widget.ProfilePictureView;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

//------------------------------------------------------------------------------------------------------------------------------
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//==============================================================================================================================
public class GroupChat extends FragmentActivity
        implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener
{

    //--------------------------------------------------------------------------------------------------------------------------
    public class SendMsgAsync extends AsyncTask<String, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            if (!(GlobalUtills.YouTube_URL.equals("")))
            {
                youtubeThumbnail_ = GlobalUtills.YouTube_URL.substring(8, GlobalUtills.YouTube_URL.length());
                youtubeThumbnail_ = youtubeThumbnail_ + "," + GlobalUtills.YouTube_VIdeoID;
                GlobalUtills.YouTube_URL = "";
                GlobalUtills.YouTube_VIdeoID = "";
//                msgToSend_                   = "";
                msgToSend_ = GlobalUtills.youtubeTitile;
            }

            if (imgBytes_.equals(""))
            {
                if (visibility_.equals("Y"))
                    visibilityFbID_ = myFbID_;

                else
                    visibilityFbID_ = "";

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(calendar.getTime());
                Chat_Single chat = new Chat_Single(global_.getUser_id(), msgToSend_, formattedDate, "", "",
                        "" + youtubeThumbnail_, visibilityFbID_, "");
                List<Chat_Single> chatsList = new ArrayList<Chat_Single>();
                chatsList.add(chat);

                try
                {

                    if (GlobalUtills.mygroups_savelocal)
                    {
                        /***************By Harpreet****************************************/
                        db.createTable("G" + groupID_);
                        /******************************************************/
                        db.insertRecords(chat);
                    }

//                    if (GlobalUtills.mygroups_savelocal)
//                    {
//                        if (visibility_.equals("Y"))
//                            visibilityFbID_ = myFbID_;
//
//                        else
//                            visibilityFbID_ = "";
//
//                        SharedPreferences sharedChatDataS = getSharedPreferences("Chat", Context.MODE_PRIVATE);
//                        JSONArray jsonOld = new JSONArray(sharedChatDataS.getString(groupID_, ""));
//                        JSONObject json = new JSONObject();
//
//                        json.put(GlobalConstant.USER_ID, "" + global_.getUser_id());
//                        json.put("message", "" + msgToSend_);
//                        json.put("date", "" + formattedDate);
//                        json.put("user_name", "G");
//                        json.put("image", "" + "");
//                        json.put("youtube", "" + youtubeThumbnail_);
//                        json.put(GlobalConstant.FACE_BOOK_ID, visibilityFbID_);
//                        json.put("user_telephone", "0000000000");
//
//                        jsonOld.put(json);
//
//                        Editor editShared = sharedChatDataS.edit();
//
//                        editShared.putString(groupID_, jsonOld.toString());
//                        editShared.apply();
//                    }

                    parseMsgGroupData(chatsList, false, false);
                }
                catch (Exception e)
                {
                }
            }
            else
            {
                progressBarSendMsgGroup_.setVisibility(View.VISIBLE);

                buttonSendChat_.setEnabled(false);
                uiImgViewTakePic_.setEnabled(false);
                editMsg_.setEnabled(false);
            }

            editMsg_.setText("");

            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            String msg = params[0];

            if (youtubeThumbnail_.length() > 7)
            {
                msg = msgToSend_;
            }


            List<NameValuePair> param = new ArrayList<NameValuePair>();

            if (imgBytes_.equals(""))
            {
                param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
                param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "mychat"));
            }

            param.add(new BasicNameValuePair("gid", groupID_ + ""));
            param.add(new BasicNameValuePair(GlobalConstant.GROUP_USERID, global_.getUser_id() + ""));
            param.add(new BasicNameValuePair("msg", msg + ""));
            param.add(new BasicNameValuePair("image", imgBytes_ + ""));
            param.add(new BasicNameValuePair("visibility", visibility_ + ""));
            param.add(new BasicNameValuePair("youtube", youtubeThumbnail_));

            if (imgBytes_.equals(""))
                return (new WebServiceHandler()).makeServiceCallSendchat(GlobalConstant.URL, WebServiceHandler.GET, param);

            return (new WebServiceHandler()).makeServiceCallSendchat(GlobalConstant.URLSendMSGGroup, WebServiceHandler.POST,
                    param);
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                buttonSendChat_.setEnabled(true);
                uiImgViewTakePic_.setEnabled(true);
                editMsg_.setEnabled(true);

                progressBarSendMsgGroup_.setVisibility(View.GONE);

                if (result.contains(GlobalConstant.SUCCESS))
                {
                    if (!imgBytes_.equals(""))
                    {
                        try
                        {
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = dateFormat.format(calendar.getTime());

                            JSONObject jobjIMage = new JSONObject(result);
                            String imageURL = jobjIMage.getString("image");


                            if (visibility_.equals("Y"))
                                visibilityFbID_ = myFbID_;

                            else
                                visibilityFbID_ = "";

//                                SharedPreferences sharedChatDataS = getSharedPreferences("Chat", Context.MODE_PRIVATE);
//                                JSONArray jsonOld = new JSONArray(sharedChatDataS.getString(groupID_, ""));
//                                JSONObject json = new JSONObject();
//
//                                json.put(GlobalConstant.USER_ID, "" + global_.getUser_id());
//                                json.put("message", "" + "");
//                                json.put("date", "" + formattedDate);
//                                json.put("user_name", "G");
//                                json.put("image", "" + imageURL);
//                                json.put("youtube", "");
//                                json.put(GlobalConstant.FACE_BOOK_ID, visibilityFbID_);
//
//                                jsonOld.put(json);
//
//                                Editor editShared = sharedChatDataS.edit();
//
//                                editShared.putString(groupID_, jsonOld.toString());
//                                editShared.apply();


                            Chat_Single chat = new Chat_Single(global_.getUser_id(), "", formattedDate, "", imageURL,
                                    "", visibilityFbID_, "");
                            List<Chat_Single> chatsList = new ArrayList<Chat_Single>();

                            chatsList.add(chat);

                            if (GlobalUtills.mygroups_savelocal)
                            {
                                /***************By Harpreet****************************************/
                                db.createTable("G" + groupID_);
                                /******************************************************/
                                db.insertRecords(chat);
                            }
                            parseMsgGroupData(chatsList, false, false);


//                            else
//                            {
//                                if (!imgBytes_.equals(""))
//                                {
//                                    if (visibility_.equals("Y"))
//                                        visibilityFbID_ = myFbID_;
//
//                                    else
//                                        visibilityFbID_ = "";
//
//                                    Chat_Single chat = new Chat_Single(global_.getUser_id(), "", formattedDate, "", imageURL, "", visibilityFbID_, "");
//                                    List<Chat_Single> chatsList = new ArrayList<Chat_Single>();
//
//                                    chatsList.add(chat);
//
//                                    parseMsgGroupData(chatsList, false, false);
//                                }
//                            }
                        }
                        catch (JSONException e)
                        {

                            db.dropTable("G" + groupID_);

//                            SharedPreferences sharedChatDataS = getSharedPreferences("Chat", Context.MODE_PRIVATE);
//                            Editor editShared = sharedChatDataS.edit();
//
//                            editShared.remove(groupID_);
//                            editShared.apply();

                            e.printStackTrace();
                        }
                    }

                    imgBytes_ = "";
                }

                else
                {
                    imgBytes_ = "";

                    GlobalUtills.showToast("Please try again", GroupChat.this);
                    System.out.println(result + "result error");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                imgBytes_ = "";
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        private String visibilityFbID_ = "";
        private String youtubeThumbnail_ = "";
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public class ReceiveMsgAsync extends AsyncTask<String, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressDialog_ = new TransparentProgressDialog(GroupChat.this, R.drawable.loading_spinner_icon);

            if (chatDataListGroup_.size() > 0 && GlobalUtills.mygroups_savelocal)
                localData_ = true;

            else
            {
                localData_ = false;

                progressDialog_.show();
            }
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                if (localData_)
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (progressDialog_.isShowing())
                                progressDialog_.dismiss();


                            parseMsgGroupData(chatDataListGroup_, true, false);
                        }
                    });

                }
                else
                {

                    List<NameValuePair> param = new ArrayList<NameValuePair>();

                    param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
                    param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "getchat"));
                    param.add(new BasicNameValuePair(GlobalConstant.GROUP_USERID, global_.getUser_id() + ""));
                    param.add(new BasicNameValuePair("gid", groupID_ + ""));
                    param.add(new BasicNameValuePair("timezone", Global.timeZone + ""));

                    return (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
                }
            }
            catch (Exception e)
            {
                if (progressDialog_.isShowing())
                    progressDialog_.dismiss();

                e.printStackTrace();
            }

            return "";
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            System.out.println(result + "result");

            try
            {
                if (localData_)
                {
                    if (progressDialog_.isShowing())
                        progressDialog_.dismiss();
                }
                else
                {



                    JSONObject jobjOuter = new JSONObject(result);
                    String responseMsg = jobjOuter.getString(GlobalConstant.MESSAGE);

                    if (responseMsg.equalsIgnoreCase(GlobalConstant.SUCCESS))
                    {
                        final String msgsArrayString = jobjOuter.getString("msgInfo");
//                        SharedPreferences sharedChatDataS = getSharedPreferences("Chat", Context.MODE_PRIVATE);
//                        if (GlobalUtills.mygroups_savelocal)
//                        {
//                            Editor editShared = sharedChatDataS.edit();
//
//                            editShared.putString(groupID_, msgsArrayString);
//                            editShared.apply();
//                        }

                        new AsyncTask<Void, Void, String>()
                        {
                            @Override
                            protected String doInBackground(Void... params)
                            {
                                jsonToList(msgsArrayString);
                                parseMsgGroupData(chatDataListGroup_, true, false);
                                return "";
                            }

                            @Override
                            protected void onPostExecute(String aVoid)
                            {
                                super.onPostExecute(aVoid);
                                if (progressDialog_.isShowing())
                                    progressDialog_.dismiss();
                            }
                        }.execute();


                    }
                    else
                    {
                        if (progressDialog_.isShowing())
                            progressDialog_.dismiss();
                        System.out.println(result + "error");
                        Toast.makeText(GroupChat.this,"Error in getting message history, Please try later",Toast.LENGTH_LONG).show();

                    }


                }
            }

            catch (Exception | Error e)
            {
                if (progressDialog_.isShowing())
                    progressDialog_.dismiss();

                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        private TransparentProgressDialog progressDialog_;
        private boolean localData_ = false;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public class GetUnreadMsgsAsync extends AsyncTask<String, Void, String>
    {
        //----------------------------------------------------------------------------------------------------------------------
        public GetUnreadMsgsAsync(Context con)
        {
            this.context_ = con;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            System.gc();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> param = new ArrayList<>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "get_unread"));
            param.add(new BasicNameValuePair(GlobalConstant.GROUP_ID, groupID_ + ""));
            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, uID_ + ""));
            param.add(new BasicNameValuePair("timezone", Global.timeZone + ""));

            String url = "" + GlobalConstant.URL;
            String paramString = URLEncodedUtils.format(param, "utf-8");

            url += "?" + paramString;

            System.out.println(url);

            try
            {
                HttpParams httpParams = new BasicHttpParams();

                httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

                HttpClient httpClient = new DefaultHttpClient(httpParams);
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response_ = EntityUtils.toString(httpEntity);
            }
            catch (Exception e)
            {
                System.out.println("EXCEPTION FROM THE SERVER" + e.getMessage());

                return response_ = "Error";
            }

            return response_;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                JSONObject jobjOuter = new JSONObject(result);
                String responseMSG = jobjOuter.getString(GlobalConstant.MESSAGE);

                if (responseMSG.equalsIgnoreCase(GlobalConstant.SUCCESS))
                {
                    String msgsArrayString = jobjOuter.getString("msgInfo");
                    JSONArray jsonArrMsgs = new JSONArray(msgsArrayString + "");

//                    SharedPreferences sharedChatDataS = null;
//                    JSONArray jsonOld = null;
//
//                    if (GlobalUtills.mygroups_savelocal)
//                    {
//                        sharedChatDataS = context_.getSharedPreferences("Chat", Context.MODE_PRIVATE);
//                        jsonOld = new JSONArray(sharedChatDataS.getString(groupID_, ""));
//                    }
                    for (int i = 0; i < jsonArrMsgs.length(); ++i)
                    {
                        JSONObject jobjinner = jsonArrMsgs.getJSONObject(i);
                        String userId = jobjinner.getString(GlobalConstant.USER_ID);
                        String message = jobjinner.getString("message");
                        String date = jobjinner.getString("date");

                        final String imgURL = jobjinner.getString("image");
                        final String fbID = jobjinner.getString(GlobalConstant.FACE_BOOK_ID);
                        final String username = jobjinner.getString("user_name");
                        final String youtube = jobjinner.getString("youtube");

                        final String user_telephone = jobjinner.getString("user_telephone");

//                        if (GlobalUtills.mygroups_savelocal)
//                        {
//                            try
//                            {
//                                JSONObject json = new JSONObject();
//
//                                json.put(GlobalConstant.USER_ID, "" + userId);
//                                json.put("message", "" + message);
//                                json.put("date", "" + date);
//                                json.put("user_name", username);
//                                json.put("image", "" + imgURL);
//                                json.put("youtube", "" + youtube);
//                                json.put(GlobalConstant.FACE_BOOK_ID, fbID);
//                                json.put("user_telephone", user_telephone);
//
//                                jsonOld.put(json);
//                            }
//                            catch (JSONException e)
//                            {
//                                Editor editShared = sharedChatDataS.edit();
//
//                                editShared.remove(groupID_);
//                                editShared.apply();
//
//                                e.printStackTrace();
//                            }
//                        }

                        Chat_Single chat = new Chat_Single(userId, message, date, username, imgURL, youtube, fbID,
                                user_telephone);
                        List<Chat_Single> chatsList = new ArrayList<Chat_Single>();

                        chatsList.add(chat);

                        if (GlobalUtills.mygroups_savelocal)
                        {
                            /***************By Harpreet****************************************/
                            db.createTable("G" + groupID_);
                            /******************************************************/
                            db.insertRecords(chat);
                        }


                        parseMsgGroupData(chatsList, false, false);
                    }

                    if (jsonArrMsgs.length() > 0 && GlobalUtills.mygroups_savelocal)
                    {
//                        Editor editShared = sharedChatDataS.edit();
//
//                        editShared.putString(groupID_, jsonOld.toString());
//                        editShared.apply();

                        if (ChatOneToOne.generateNoTi() && GlobalUtills.allNotification)
                        {
                            String messageNotiGroup = "You have new message in " + groupName_;

                            globalUtills_.generateNotification_local(GroupChat.this, messageNotiGroup, 1);

                            ChatOneToOne.setGenerateNoTi(false);
                        }
                    }
                }

                if (refreshChat_)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        new GetUnreadMsgsAsync(GroupChat.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    else
                        new GetUnreadMsgsAsync(GroupChat.this).execute();
                }
            }
            catch (Exception e)
            {
                if (refreshChat_)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        new GetUnreadMsgsAsync(GroupChat.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    else
                        new GetUnreadMsgsAsync(GroupChat.this).execute();
                }

                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        private String response_ = "";
        private Context context_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public class AsyncAddMembers extends AsyncTask<String, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            List<NameValuePair> param = new ArrayList<>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "add_members"));
            param.add(new BasicNameValuePair(GlobalConstant.USER_ID, global_.getUser_id()));
            param.add(new BasicNameValuePair("groupId", groupID_));
            param.add(new BasicNameValuePair(GlobalConstant.GROUP_USERS_ID, appendedFriendIDs_));
            param.add(new BasicNameValuePair("phones", appendedPhoneNumber_ + ""));

            return (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            JSONObject jsonObject;
            try
            {
                jsonObject = new JSONObject(result);

                message_ = jsonObject.getString(GlobalConstant.MESSAGE);

                if (message_.equalsIgnoreCase(GlobalConstant.SUCCESS))
                {
                    String count = jsonObject.getString("userAddedCount");

                    if (count.equalsIgnoreCase("0"))
                        GlobalUtills.showToast("Members already added..!", GroupChat.this);

                    else
                    {
                        new GetUserGroup(groupID_, actionBarCommon_).execute();

                        if (count.equalsIgnoreCase("1"))
                            GlobalUtills.showToast(count + " new member added", GroupChat.this);

                        else
                            GlobalUtills.showToast(count + " new members added", GroupChat.this);
                    }

                    if (!appendedPhoneNumber_.equals(""))
                        GlobalUtills.showToast("Invitation sent ,to new numbers..", GroupChat.this);

                    global_.clearhashMAp();
                }
                else
                    GlobalUtills.showToast("No member added..!", GroupChat.this);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        private String message_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    class JoinWorldGroup extends AsyncTask<String, String, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        public JoinWorldGroup(boolean showProgressDialog)
        {
            this.showProgressDialog_ = showProgressDialog;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            String groupID = params[0];

            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, global_.getUser_id()));
            param.add(new BasicNameValuePair(GlobalConstant.GROUP_ID, groupID));
            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));

            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "join_group"));


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

            dialog_ = new TransparentProgressDialog(GroupChat.this, R.drawable.loading_spinner_icon);

            if (showProgressDialog_)
                dialog_.show();
        }

        //----------------------------------------------------------------------------------------------------------------------
        protected void onPostExecute(String result)
        {
            try
            {
                if (dialog_.isShowing())
                    dialog_.dismiss();

                JSONObject jsonObject = new JSONObject(result);

                messaString_ = jsonObject.getString(GlobalConstant.MESSAGE);

                if (messaString_.equalsIgnoreCase(GlobalConstant.SUCCESS))
                {
                    if (showProgressDialog_)
                    {
                        globalUtills_.DialogOK(GroupChat.this, "Join group", "You have successfully joined this group..!");
                    }
                    else
                    {
                        globalUtills_.DialogOK(GroupChat.this, "Join group", "You are now member of this group.");
                    }

                    GlobalUtills.joinORadd_group = true;

                }


            }
            catch (Exception e)
            {
                Toast.makeText(GroupChat.this, "Error..!", Toast.LENGTH_LONG).show();  // TODO:

                System.out.println(e.getMessage());
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        private TransparentProgressDialog dialog_;
        private String responseJoinWorldGroup_;
        private String messaString_;
        private boolean showProgressDialog_ = true;
    }


    //--------------------------------------------------------------------------------------------------------------------------
    class ShowMsgDataAsyncGroup extends AsyncTask<String, Chat_Single, String>
    {
        private TransparentProgressDialog dialog_;
        List<Chat_Single> listChatData = null;


        //----------------------------------------------------------------------------------------------------------------------
        public ShowMsgDataAsyncGroup(List<Chat_Single> listChatData)
        {

            dialog_ = new TransparentProgressDialog(GroupChat.this, R.drawable.loading_spinner_icon);

            dialog_.show();


            this.listChatData = listChatData;


        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {


            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------


//        @Override
//        protected void onProgressUpdate(Chat_Single... values)
//        {
//
//            showChat(values[0], g, chatDataListGroup_.size(), false, true);
//
////            parseMsgGroupData(listChatData, false, true);
//            //            super.onProgressUpdate(values[0]);
//        }


        @Override
        protected String doInBackground(String... params)
        {
//            chatDataListGroup_ = db.getAfterChatList(CHATLIST_SIZE);
//            Collections.reverse(chatDataListGroup_);


//            showChat(values[0], g, chatDataListGroup_.size(), false, true);

            parseMsgGroupData(listChatData, (db.getSizeOfChat() > CHATLIST_SIZE * LOAD_MORE_TIMES), true);


            return null;
        }


        @Override
        protected void onPostExecute(String s)
        {

//            parseMsgGroupData(chatDataListGroup_, false, true);

            dialog_.dismiss();
            super.onPostExecute(s);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void setGroupName(String value)
    {
        groupName_ = value;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void setGroupImage(String value)
    {
        groupImage_ = value;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static String groupID()
    {
        return groupID_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static String groupName()
    {
        return groupName_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static String groupImage()
    {
        return groupImage_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static boolean refreshChat()
    {
        return refreshChat_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static String parseDateToddMMyyyy(String time)
    {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try
        {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return str;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed()
    {
        GlobalUtills.groupChat_noti = true;
        GlobalUtills.GroupChat_Sender = "";
        groupImage_ = "";

        if (!checkKeyboard_)
        {
            checkKeyboard_ = true;

            imgViewSmily_.setVisibility(View.VISIBLE);
            imgViewKeyboard_.setVisibility(View.INVISIBLE);
            frameEmoji_.setVisibility(View.GONE);
        }
        else
        {
            dateTemp_ = "";
            refreshChat_ = false;

            super.onBackPressed();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onEmojiconBackspaceClicked(View v)
    {
        EmojiconsFragment.backspace(editMsg_);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onEmojiconClicked(Emojicon emojicon)
    {
        EmojiconsFragment.input(editMsg_, emojicon);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void getUnreadGroup(Context ccon)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new GetUnreadMsgsAsync(GroupChat.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        else
            new GetUnreadMsgsAsync(GroupChat.this).execute();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void dialogoChoose()
    {
        final Dialog dialogLoader = new Dialog(this, R.style.Theme_Dialog);

        dialogLoader.setTitle("Select a Image ");
        dialogLoader.setContentView(R.layout.dialogo_choose);

        dialogLoader.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        ImageButton buttonTakeGallery = (ImageButton) dialogLoader.findViewById(R.id.uiC_imgbtntake_gallery);
        ImageButton buttonTakeCamera = (ImageButton) dialogLoader.findViewById(R.id.uiC_imgbtntake_camera);
        ImageButton buttonYoutube = (ImageButton) dialogLoader.findViewById(R.id.btn_youTube);

        buttonYoutube.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogLoader.dismiss();

                Intent gotoYouTube = new Intent(GroupChat.this, YouTubeVideosList.class);

                startActivity(gotoYouTube);
            }
        });

        buttonTakeGallery.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
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
    public void dialogShowFriends()
    {
        final Dialog dialogLoader = new Dialog(this, R.style.Theme_Dialog);

        dialogLoader.setTitle("Selected friends ");
        dialogLoader.setContentView(R.layout.dialog_showfrnd);

        dialogLoader.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        dialogLoader.setCancelable(false);

        LinearLayout layoutAddedFriends = (LinearLayout) dialogLoader.findViewById(R.id.layout_showfrnds);

        layoutAddedFriends.removeAllViews();

        Button buttonAdd = (Button) dialogLoader.findViewById(R.id.btn_showfrnds_add);
        Button buttonCancel = (Button) dialogLoader.findViewById(R.id.btn_showfrnds_cancel);

        TextView title = new TextView(getApplicationContext());

        title.setTextColor(Color.WHITE);
        title.setText("Selected fb friends (" + friendID_.size() + ") :");
        title.setTextSize(25);
        title.setTypeface(null, Typeface.ITALIC);

        layoutAddedFriends.addView(title);

        for (int i = 0; i < friendID_.size(); ++i)
        {
            LinearLayout.LayoutParams lpLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

            lpLayout.gravity = Gravity.CENTER_HORIZONTAL;

            Facebook_ProfilePictureView_rounded addedFriends = new Facebook_ProfilePictureView_rounded(getApplicationContext());

            addedFriends.setLayoutParams(lpLayout);
            addedFriends.setProfileId(friendID_.get(i));

            layoutAddedFriends.addView(addedFriends);
        }

        if (hashMapGetPhones_.size() > 0)
        {
            TextView title2 = new TextView(getApplicationContext());

            title2.setTextColor(Color.WHITE);
            title2.setText("Selected phone contacts (" + phoneNumber_.length + ") :");
            title.setTextSize(25);
            title.setTypeface(null, Typeface.ITALIC);

            layoutAddedFriends.addView(title2);

            for (int g = 0; g < phoneNumber_.length; ++g)
            {
                LinearLayout.LayoutParams lpLayout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                lpLayout.gravity = Gravity.CENTER_HORIZONTAL;

                TextView name = new TextView(getApplicationContext());


                name.setTextColor(getResources().getColor(color.pinkNew));
                name.setText(phoneNumber_[g] + "");
                name.setLayoutParams(lpLayout);

                layoutAddedFriends.addView(name);
            }
        }

        buttonAdd.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                global_.clearhashMAp();
                global_.clearhashMAp_ph();

                friendID_.clear();
                AddSocialFriendAdapter.radio_checkCONtacts.clear();
                ShowSocialFriends.radioCheckGroup().clear();
                UsersAroundMe.radiosAroundMeChecked().clear();

                new AsyncAddMembers().execute();

                dialogLoader.cancel();
            }
        });

        buttonCancel.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                global_.clearhashMAp();
                global_.clearhashMAp_ph();

                friendID_.clear();
                AddSocialFriendAdapter.radio_checkCONtacts.clear();
                ShowSocialFriends.radioCheckGroup().clear();
                UsersAroundMe.radiosAroundMeChecked().clear();

                dialogLoader.cancel();
            }
        });

        dialogLoader.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        try
        {
            globalUtills_ = new GlobalUtills();
            if (GlobalUtills.YouTube_URL.length() > 2)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                {
                    new SendMsgAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                }
                else
                {
                    new SendMsgAsync().execute("");
                }


                if (!GlobalUtills.joinORadd_group)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        new JoinWorldGroup(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, groupID_);

                    else
                        new JoinWorldGroup(false).execute(groupID_);


                }


            }

            refreshChat_ = true;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new GetUnreadMsgsAsync(GroupChat.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            else
                new GetUnreadMsgsAsync(GroupChat.this).execute();

            hashMapGetIDs_ = global_.getHashMap();
            hashMapGetPhones_ = global_.getHashMap_ph();

            if (hashMapGetIDs_.size() > 0 || hashMapGetPhones_.size() > 0)
            {
                if (hashMapGetIDs_.size() > 0)
                {
                    friendID_ = new ArrayList<String>();

                    Set<Integer> keys = hashMapGetIDs_.keySet();

                    for (Integer key : keys)
                    {


                        if (!friendID_.contains(hashMapGetIDs_.get(key)))
                            friendID_.add(hashMapGetIDs_.get(key));
                    }

                    StringBuilder builder;

                    for (int i = 0; i < friendID_.size(); ++i)
                    {
                        builder = new StringBuilder();

                        if (i == friendID_.size())
                            appendedFriendIDs_ = appendedFriendIDs_ + builder.append(friendID_.get(i));

                        else
                            appendedFriendIDs_ = appendedFriendIDs_ + builder.append(friendID_.get(i)).append(",");
                    }

                    appendedFriendIDs_ = appendedFriendIDs_.substring(0, appendedFriendIDs_.length() - 1);
                }

                int g = 0;

                if (hashMapGetPhones_.size() > 0)
                {
                    phoneNumber_ = new String[hashMapGetPhones_.size()];

                    Set<Integer> keys = hashMapGetPhones_.keySet();

                    for (Integer key : keys)
                    {


                        phoneNumber_[g] = hashMapGetPhones_.get(key);

                        ++g;
                    }


                    StringBuilder builder;

                    for (int i = 0; i < phoneNumber_.length; ++i)
                    {
                        builder = new StringBuilder();

                        if (i == phoneNumber_.length)
                            appendedPhoneNumber_ = appendedPhoneNumber_ + builder.append(phoneNumber_[i].replaceAll(" ", "").trim());

                        else
                            appendedPhoneNumber_ = appendedPhoneNumber_ + builder.append(phoneNumber_[i].replaceAll(" ", "").trim()).append(",");


                    }
                }


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
                                dialogShowFriends();
                            }
                        });

                        return null;
                    }
                }.execute();

            }

            NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notifManager.cancel(1);

            SharedPreferences sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE);

            global_.setUser_id(sharedPref.getString("UserID", ""));

            actionBarCommon_.setActionText(groupName_ + "");
        }
        catch (Exception | Error e)
        {
        }

        super.onResume();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onPause()
    {
        refreshChat_ = false;

        super.onPause();
    }

    DBhelperG db;

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            Global.showAdsIfNeeded(GroupChat.this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.group__chat);


        uiScroll_ = ((ScrollView) findViewById(R.id.uiC_scrllView));


        layoutChat_ = (LinearLayout) findViewById(R.id.layoutChat);
        buttonSendChat_ = (ImageButton) findViewById(R.id.btnSentChat);
        editMsg_ = (EmojiconEditText) findViewById(R.id.edMsg);
        progressBarSendMsgGroup_ = (ProgressBar) findViewById(R.id.uiC_progressBSendMsgGroup);
        layoutSmily_ = (FrameLayout) findViewById(R.id.linear_layout_smily_group);
        frameEmoji_ = (FrameLayout) findViewById(R.id.emojiconsG);
        imgViewSmily_ = (ImageView) findViewById(R.id.img_view_smily_group);
        imgViewKeyboard_ = (ImageView) findViewById(R.id.img_view_keyboard_group);
        uiImgViewTakePic_ = (ImageView) findViewById(R.id.uiC_imgVtakePicG);
        toggleButton_ = (ToggleButton) findViewById(R.id.toggleButtonVisibility);

        toggleButton_.setChecked(true);

        refreshChat_ = true;

        Intent intentdata = getIntent();

        GlobalUtills.YouTube_URL = "";
        globalUtills_ = new GlobalUtills();
        global_ = new Global();

        uID_ = global_.getUser_id();
        groupID_ = intentdata.getStringExtra("groupID");
        groupName_ = intentdata.getStringExtra("groupName");
        groupImage_ = intentdata.getStringExtra("groupImage");


        db = new DBhelperG(GroupChat.this, "G" + groupID_);


        GlobalUtills.GroupChat_Sender = groupID_;
        hashMapGetIDs_ = global_.getHashMap();

        SharedPreferences sharedPref = getSharedPreferences("fbID", Context.MODE_PRIVATE);

        myFbID_ = sharedPref.getString("fb", "");

        GlobalUtills.list_of_GroupMembers.clear();

        System.out.println(groupID_ + "<--group id");

        actionBarCommon_ = new ActionBarCommon(GroupChat.this, null);
        actionBarCommon_ = (ActionBarCommon) findViewById(R.id.action_barC);

        actionBarCommon_.setActionText(groupName_ + "");
        actionBarCommon_.leftImage().setImageResource(R.drawable.icon_back_arrow);
        actionBarCommon_.layoutLeft().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GlobalUtills.groupChat_noti = true;
                GlobalUtills.GroupChat_Sender = "";

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(editMsg_.getWindowToken(), 0);

                dateTemp_ = "";
                refreshChat_ = false;

                finish();
            }
        });

        actionBarCommon_.setLayoutCenterClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (GlobalUtills.list_of_GroupMembers.size() > 0)
                {
                    refreshChat_ = false;

                    Intent gotoGChatting = new Intent(GroupChat.this, GroupUsersInfo.class);

                    gotoGChatting.putExtra("groupID", groupID_);
                    gotoGChatting.putExtra("uID", global_.getUser_id());

                    startActivity(gotoGChatting);
                }
            }
        });

        actionBarCommon_.rightImage().setImageResource(R.drawable.join_group);
        actionBarCommon_.layoutRight().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (globalUtills_.haveNetworkConnection(GroupChat.this))
                {
                    if (GlobalUtills.joinORadd_group)
                    {
                        Intent intent = new Intent(GroupChat.this, FriendsTabBar.class);

                        hashMapGetIDs_.clear();

                        global_.setHashMap(hashMapGetIDs_);

                        appendedFriendIDs_ = "";

                        global_.hashMap_idsGroupmates.clear();

                        phoneNumber_ = new String[0];

                        friendID_.clear();
                        hashMapGetPhones_.clear();
                        global_.hashMap_ids_ph.clear();
                        global_.hashMap_idsaroundme.clear();

                        appendedPhoneNumber_ = "";

                        startActivity(intent);
                    }
                    else
                    {
                        final Dialog dialog = globalUtills_.prepararDialog(GroupChat.this, R.layout.dialog_three_options);

                        TextView title = (TextView) dialog.findViewById(R.id.txtVmainTitle);

                        title.setText("Join Group");

                        TextView subheading = (TextView) dialog.findViewById(R.id.txtVsubheading);

                        subheading.setText("Send request to join this group ?");

                        Button buttonChat = (Button) dialog.findViewById(R.id.btnChat);
                        Button buttonCall = (Button) dialog.findViewById(R.id.btncall);
                        Button buttonGroups = (Button) dialog.findViewById(R.id.btngroups);
                        ImageButton buttonClose = (ImageButton) dialog.findViewById(R.id.btnCloseDialog);

                        buttonGroups.setVisibility(View.GONE);
                        buttonClose.setVisibility(View.GONE);

                        buttonCall.setText("Join");
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
                                new JoinWorldGroup(true).execute(groupID_);
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }
                }
                else
                    GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, GroupChat.this);
            }
        });


        chatDataListGroup_.clear();

//        SharedPreferences sharedChatDataS = getSharedPreferences("Chat", Context.MODE_PRIVATE);

//        if (sharedChatDataS.contains(groupID_) && GlobalUtills.mygroups_savelocal)
//        {
//            String data = sharedChatDataS.getString(groupID_, "");
//
//            if (data.contains("user_telephone"))
//                jsonToList(data);
//
//            else
//                sharedChatDataS.edit().remove(groupID_).apply();
//        }

        chatDataListGroup_ = db.getTopChatList(CHATLIST_SIZE);
//        if(chatDataListGroup_.size()>0)


        if (globalUtills_.haveNetworkConnection(GroupChat.this))
        {
            if (chatDataListGroup_.size() > 0 && GlobalUtills.mygroups_savelocal)
            {
                parseMsgGroupData(chatDataListGroup_, true, false);
            }
            else
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new ReceiveMsgAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                else
                    new ReceiveMsgAsync().execute();
            }
        }
        else
        {
            if (chatDataListGroup_.size() > 0)
                new ShowMsgDataAsyncGroup(chatDataListGroup_).execute();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new GetUserGroup(groupID_, actionBarCommon_).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        else
            new GetUserGroup(groupID_, actionBarCommon_).execute();

        setEmojiconFragment(false);

        buttonSendChat_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (editMsg_.getText().toString().trim().equals(""))
                    GlobalUtills.showToast("nothing to send ..!", GroupChat.this);

                else
                {
                    msgToSend_ = editMsg_.getText().toString().trim();

                    if (globalUtills_.haveNetworkConnection(GroupChat.this))
                    {
                        GlobalUtills.YouTube_URL = "";
                        GlobalUtills.YouTube_VIdeoID = "";

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            new SendMsgAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, msgToSend_);

                        else
                            new SendMsgAsync().execute(msgToSend_);

                        if (!GlobalUtills.joinORadd_group)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                                new JoinWorldGroup(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, groupID_);

                            else
                                new JoinWorldGroup(false).execute(groupID_);


                        }
                    }
                    else
                        GlobalUtills.showToast("No internet connection..!", GroupChat.this);
                }
            }
        });

        editMsg_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!checkKeyboard_)
                {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        layoutSmily_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkKeyboard_)
                {
                    checkKeyboard_ = false;

                    imgViewSmily_.setVisibility(View.INVISIBLE);
                    imgViewKeyboard_.setVisibility(View.VISIBLE);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    frameEmoji_.setVisibility(View.VISIBLE);
                }
                else
                {
                    checkKeyboard_ = true;

                    imgViewSmily_.setVisibility(View.VISIBLE);
                    imgViewKeyboard_.setVisibility(View.INVISIBLE);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.showSoftInput(editMsg_, InputMethodManager.SHOW_IMPLICIT);

                    frameEmoji_.setVisibility(View.GONE);
                }
            }
        });

        uiImgViewTakePic_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogoChoose();
            }
        });

        editMsg_.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (editMsg_.getText().toString().length() > 0)
                {
                    uiImgViewTakePic_.setVisibility(View.GONE);
                    buttonSendChat_.setVisibility(View.VISIBLE);
                }
                else
                {
                    uiImgViewTakePic_.setVisibility(View.VISIBLE);
                    buttonSendChat_.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

        toggleButton_.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    visibility_ = "Y";
                }
                else
                {
                    visibility_ = "N";
                }
            }
        });

        if (!ChatOneToOne.shareImage().isEmpty())
        {
            processBitmap(BitmapDecoder.decodeFile(ChatOneToOne.shareImage()));

            GlobalUtills.showToast("Sharing Image....", GroupChat.this);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (resultCode == RESULT_OK) {

                processBitmap(BitmapDecoder.getFromData(requestCode, resultCode, data, getContentResolver()));
                 super.onActivityResult(requestCode, resultCode, data);
            }
    }


    //--------------------------------------------------------------------------------------------------------------------------
    private void processBitmap(Bitmap bitmap)
    {
        imgBytes_ = globalUtills_.BitMapToString(bitmap);
        msgToSend_ = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new SendMsgAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

        else
            new SendMsgAsync().execute("");

        ChatOneToOne.setShareImage("");
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void popupUserDetails(final String Name, final String fbID, final String phoneNumber, final String userID)
    {
        final Dialog dialog = globalUtills_.prepararDialog(GroupChat.this, R.layout.user_details);

        ProfilePictureView userPicture = (ProfilePictureView) dialog.findViewById(R.id.ImgVuserImgDP);

        userPicture.setProfileId(fbID);

        TextView textSaveType = (TextView) dialog.findViewById(R.id.txtV_userName);
        ((TextView) dialog.findViewById(R.id.txtReportAbused)).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new ReportAbuseUser(GroupChat.this, global_.getUser_id(), userID).execute();
                dialog.dismiss();
            }
        });


        textSaveType.setText(Name.toString());


        ImageView fbIcon = (ImageView) dialog.findViewById(R.id.fbIcon);
        fbIcon.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new GlobalUtills().getOpenFacebookIntent(GroupChat.this, fbID));
            }
        });


        final Button buttonChat = (Button) dialog.findViewById(R.id.btnUserChat);
        final Button buttonCancel = (Button) dialog.findViewById(R.id.btnUserCancel);
        final Animation animation = AnimationUtils.loadAnimation(GroupChat.this, R.anim.zoom_out);


        animation.setFillAfter(true);

        buttonCancel.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        buttonCancel.setAnimation(animation);
                        buttonCancel.startAnimation(animation);

                        break;

                    case MotionEvent.ACTION_UP:
                        final Animation animation2 = AnimationUtils.loadAnimation(GroupChat.this, R.anim.zoom_out_two);

                        animation2.setFillAfter(true);
                        animation2.setAnimationListener(new AnimationListener()
                        {
                            @Override
                            public void onAnimationStart(Animation animation)
                            {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation)
                            {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation)
                            {
                                dialog.dismiss();
                            }
                        });

                        buttonCancel.setAnimation(animation2);
                        buttonCancel.startAnimation(animation2);

                        break;
                }

                return false;
            }
        });

        buttonChat.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        buttonChat.setAnimation(animation);
                        buttonChat.startAnimation(animation);

                        break;

                    case MotionEvent.ACTION_UP:
                        final Animation animaiton2 = AnimationUtils.loadAnimation(GroupChat.this, R.anim.zoom_out_two);

                        animaiton2.setFillAfter(true);
                        animaiton2.setAnimationListener(new AnimationListener()
                        {
                            @Override
                            public void onAnimationStart(Animation animation)
                            {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation)
                            {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation)
                            {
                                Intent gotoChatting = new Intent(GroupChat.this, ChatOneToOne.class);

                                gotoChatting.putExtra("name", Name + "");
                                gotoChatting.putExtra("fbID", fbID + "");
                                gotoChatting.putExtra(GlobalConstant.PHONE_NUMBER, phoneNumber + "");
                                gotoChatting.putExtra(GlobalConstant.JOIN_USER_ID, userID + "");

                                startActivity(gotoChatting);

                                dialog.dismiss();
                            }
                        });

                        buttonChat.setAnimation(animaiton2);
                        buttonChat.startAnimation(animaiton2);

                        break;
                }

                return false;
            }
        });

        dialog.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void setEmojiconFragment(boolean useSystemDefault)
    {
        try
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.emojiconsG,
                    EmojiconsFragment.newInstance(useSystemDefault)).commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    final int CHATLIST_SIZE = 50;

    private static int LOAD_MORE_TIMES = 0;

    //--------------------------------------------------------------------------------------------------------------------------
    private void parseMsgGroupData(final List<Chat_Single> listChat, final boolean loadmore, final boolean index)
    {

        Runnable runnable = new Runnable()
        {
            public void run()
            {

                dateToMatch_ = dateTemp_;



                for (int group = 0; group < listChat.size(); ++group)
                {
                    showChat(listChat.get(group), group, listChat.size(), loadmore, index);
                }




                if (loadmore)
                {
                    if (db.getSizeOfChat() > CHATLIST_SIZE)
                    {
                        Button buttonLoadMore = new Button(getApplicationContext());

                        buttonLoadMore.setText("LOAD MORE");
                        buttonLoadMore.setBackgroundColor(GlobalConstant.COLOR_DARK_GREY);
                        buttonLoadMore.setTextSize(18);
                        buttonLoadMore.setTextColor(Color.WHITE);
                        buttonLoadMore.setAlpha(.40f);

                        buttonLoadMore.setOnClickListener(new OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                layoutChat_.removeViewAt(0);

//                                jsonToListLoadMore();

//                                new Thread(new Runnable()
//                                {
//                                    @Override
//                                    public void run()
//                                    {
//                                        chatDataListGroup_ = db.getAfterChatList(CHATLIST_SIZE+1);
//                                        Collections.reverse(chatDataListGroup_);
//
//                                        parseMsgGroupData(chatDataListGroup_, false,true);
//                                    }
//                                }).start();

                                LOAD_MORE_TIMES++;

                                if (db.getSizeOfChat() > CHATLIST_SIZE * LOAD_MORE_TIMES)
                                {
                                    chatDataListGroup_ = db.getLastRecords(CHATLIST_SIZE, LOAD_MORE_TIMES);
                                }

                                new ShowMsgDataAsyncGroup(chatDataListGroup_).execute();

                            }
                        });

                        layoutChat_.addView(buttonLoadMore, 0);
                    }
                }




                if (!index)
                {
                    uiScroll_.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            uiScroll_.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }










                chatDataListGroup_.clear();


            }
        };


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);

    }


    //    chat to be displayed
    private void showChat(Chat_Single chatSingle, int group, int listSize, final boolean loadmore, final boolean index)
    {
        final String userId = chatSingle.getUserId();
        final String message = chatSingle.getMessage();
        final String date = chatSingle.getDate();
        final String username = chatSingle.getUsername();
        final String imgURL = chatSingle.getImgURL();
        final String fbID = chatSingle.getFbID();
        final String youtube = chatSingle.getYoutube();
        final String userPhone = chatSingle.getPhNO_();

        String formtDate = parseDateToddMMyyyy(date);


        if (formtDate == null || formtDate.equals(""))
        {
            db.dropTable("G" + groupID_);
//                        SharedPreferences chatPreferences = getSharedPreferences("Chat", Context.MODE_PRIVATE);
//                        Editor editor = chatPreferences.edit();
//
//                        editor.remove(facebookID_);
//                        editor.apply();

            new ReceiveMsgAsync().execute();

            return;
        }

        if (index && group == 0)
            dateToMatch_ = formtDate;

        if (dateToMatch_.equalsIgnoreCase(formtDate))
        {
        }
        else
        {
            if (!index)
            {
                LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                lpDate.gravity = Gravity.CENTER;

                TextView tvDate = new TextView(getApplicationContext());

                tvDate.setLayoutParams(lpDate);
                tvDate.setText(formtDate + "");
                tvDate.setTextColor(Color.DKGRAY);
                tvDate.setTextSize(18);
                tvDate.setPadding(5, 5, 5, 5);
                tvDate.setGravity(Gravity.CENTER);

                layoutChat_.addView(tvDate);
            }
            else
            {
                LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                lpDate.gravity = Gravity.CENTER;

                TextView tvDate = new TextView(getApplicationContext());

                tvDate.setLayoutParams(lpDate);
                tvDate.setText(dateToMatch_ + "");
                tvDate.setTextColor(Color.DKGRAY);
                tvDate.setTextSize(18);
                tvDate.setPadding(5, 5, 5, 5);
                tvDate.setGravity(Gravity.CENTER);

                if (group == 0)
                {
                }
                else
                    layoutChat_.addView(tvDate, 0);
            }
        }

        if (index)
            dateToMatch_ = formtDate;

        else
        {
            dateTemp_ = formtDate;
            dateToMatch_ = dateTemp_;
        }

        LinearLayout layoutMsgContainer = new LinearLayout(GroupChat.this);

        if (userId.equalsIgnoreCase(global_.getUser_id()))
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.LEFT;

            layoutMsgContainer.setLayoutParams(lp);

            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.chat_my, layoutMsgContainer);

            EmojiconTextView tvMsg = (EmojiconTextView) view.findViewById(R.id.txtV_MyChatmsg);
            RoundedCornersGaganImageView imgView = (RoundedCornersGaganImageView)
                    view.findViewById(R.id.imgV_MyChatImage);
            Facebook_ProfilePictureView_rounded
                    profilePicture = (Facebook_ProfilePictureView_rounded)
                    view.findViewById(R.id.fb_MyChat);
            LinearLayout framemsgLayout = (LinearLayout) view.findViewById(R.id.FrameLayoutMyChat);
            TextView tvTime = (TextView) view.findViewById(R.id.txtV_MyChatDate);
            FrameLayout imageContainer = (FrameLayout) view.findViewById(R.id.FrameLayoutIMAGE);
            ImageButton buttonPlay = (ImageButton) view.findViewById(
                    R.id.img_btn_play_youtube_video);

            profilePicture.setProfileId(fbID);

            tvTime.setText(globalUtills_.parseDateToTime(date));

            if (imgURL.length() > 2)
            {
                tvMsg.setVisibility(View.GONE);
                imageContainer.setVisibility(View.VISIBLE);

                imgView.setImageUrl(GroupChat.this, imgURL);
                imgView.bringToFront();

                imgView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        globalUtills_.ViewPhoto(imgURL, GroupChat.this, "");
                    }
                });
            }
            else if (youtube.length() > 5)
            {
                String youtubeUrl = youtube.substring(0, youtube.indexOf(","));
                final String youtubeID = youtube.substring(youtubeUrl.length() + 1, youtube.length());

                if (!message.isEmpty())
                {
                    tvMsg.setText(message);
                }
                else
                {
                    tvMsg.setVisibility(View.GONE);
                }
                imageContainer.setVisibility(View.VISIBLE);

                imgView.setImageUrl(GroupChat.this, "https://" + youtubeUrl);
                imgView.setScaleType(ScaleType.FIT_XY);

                buttonPlay.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(GroupChat.this, YoutubeVideoPlayer.class);

                        intent.putExtra("video_id", youtubeID);
                        intent.putExtra("preview", false);

                        startActivity(intent);
                    }
                });
            }
            else
            {
                framemsgLayout.setVisibility(View.VISIBLE);
                imageContainer.setVisibility(View.GONE);

                tvMsg.setText(message);
                tvTime.setText(globalUtills_.parseDateToTime(date));
            }
        }
        else
        {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

            lp.gravity = Gravity.RIGHT;

            layoutMsgContainer.setLayoutParams(lp);

            View viewOther = LayoutInflater.from(getApplicationContext()).inflate(R.layout.other_chat,
                    layoutMsgContainer);
            EmojiconTextView tvMsg = (EmojiconTextView) viewOther.findViewById(R.id.txtV_otherChatmsg);

            RoundedCornersGaganImageView imgView = (RoundedCornersGaganImageView)
                    viewOther.findViewById(R.id.imgV_otherChatImage);
            Facebook_ProfilePictureView_rounded profilePicture =
                    (Facebook_ProfilePictureView_rounded)
                            viewOther.findViewById(R.id.fb_otherChat);

            LinearLayout framemsgLayout = (LinearLayout) viewOther.findViewById(R.id.FrameLayoutotherChat);
            TextView tvName = (TextView) viewOther.findViewById(R.id.txtVChatName);
            TextView tvTime = (TextView) viewOther.findViewById(R.id.txtV_otherChatDate);
            FrameLayout imageContainer = (FrameLayout) viewOther.findViewById(R.id.FrameLayoutIMAGE_Other);
            ImageButton buttonPlay = (ImageButton) viewOther.findViewById(R.id.img_btn_play_youtube_video_other);

            profilePicture.setProfileId(fbID);

            tvTime.setText(globalUtills_.parseDateToTime(date));

            if (fbID.equals("") || username.equals(null) || username.equals(""))
            {
                if (userId.equals("299"))
                {
                    tvName.setText("Get Groupy");
                }
                else
                {
                    tvName.setText("Unknown");
                }
            }
            else
            {
                tvName.setText(username);
            }

            if (imgURL.length() > 2)
            {
                tvMsg.setVisibility(View.GONE);
                imageContainer.setVisibility(View.VISIBLE);

                imgView.setImageUrl(GroupChat.this, imgURL);
                imgView.bringToFront();

                imgView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        globalUtills_.ViewPhoto(imgURL, GroupChat.this, username + globalUtills_.parseDateToSAVE(date));
                    }
                });
            }
            else if (youtube.length() > 5)
            {
                String youtubeUrl = youtube.substring(0, youtube.indexOf(","));
                final String youtubeId = youtube.substring(youtubeUrl.length() + 1, youtube.length());

                if (!message.isEmpty())
                {
                    tvMsg.setText(message);
                }
                else
                {
                    tvMsg.setVisibility(View.GONE);
                }
                imageContainer.setVisibility(View.VISIBLE);

                imgView.setImageUrl(GroupChat.this, "https://" + youtubeUrl);
                imgView.setScaleType(ScaleType.FIT_XY);

                buttonPlay.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getApplicationContext(), YoutubeVideoPlayer.class);

                        intent.putExtra("video_id", youtubeId);
                        intent.putExtra("preview", false);

                        startActivity(intent);
                    }
                });
            }
            else
            {
                framemsgLayout.setVisibility(View.VISIBLE);
                imageContainer.setVisibility(View.GONE);

                tvMsg.setText(message);
                tvTime.setText(globalUtills_.parseDateToTime(date));
            }

            profilePicture.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (fbID.equals("") || username.equals(null) || username.equals(""))
                    {
                        if (!userId.equals("299"))
                        {
                            GlobalUtills.showToast("Privacy hidden..!", GroupChat.this);
                        }
                    }
                    else
                    {
                        String facebookID = fbID;  // TODO:
                        String facebookName = username;
                        String number = userPhone;
                        String usrID = userId;

                        popupUserDetails(facebookName, facebookID, number, usrID);
                    }
                }
            });
        }

        if (index)
        {
            layoutChat_.addView(layoutMsgContainer, 0);

            if (group == listSize - 1)
            {
                LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                lpDate.gravity = Gravity.CENTER;

                TextView tvDate = new TextView(getApplicationContext());

                tvDate.setLayoutParams(lpDate);
                tvDate.setText(formtDate + "");
                tvDate.setTextColor(Color.DKGRAY);
                tvDate.setTextSize(18);
                tvDate.setPadding(5, 5, 5, 5);

                tvDate.setGravity(Gravity.CENTER);

                layoutChat_.addView(tvDate, 0);
            }
        }
        else
            layoutChat_.addView(layoutMsgContainer);

        layoutMsgContainer.setOnLongClickListener(new OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View arg0)
            {
                if (!message.isEmpty())
                {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", message);

                    clipboard.setPrimaryClip(clip);

                    GlobalUtills.showToast("Text Copied to clip board", GroupChat.this);
                }

                return false;
            }
        });

    }

//    end chat to be displayed


    //--------------------------------------------------------------------------------------------------------------------------
//    private int getChatLength()
//    {
//        try
//        {
//            SharedPreferences sharedChatDataS = getSharedPreferences("Chat", MODE_PRIVATE);
//
//            if (sharedChatDataS.contains(groupID_))
//            {
//                JSONArray jsonArrMsgs = new JSONArray(sharedChatDataS.getString(groupID_, "") + "");
//
//                return jsonArrMsgs.length();
//            }
//            else
//                return 0;
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//
//            return 0;
//        }
//    }
//
//    //--------------------------------------------------------------------------------------------------------------------------
//    private void jsonToListLoadMore()
//    {
//        try
//        {
//            if (chatDataListGroup_.size() > 0)
//                chatDataListGroup_.clear();
//
//            SharedPreferences sharedChatDataS = getSharedPreferences("Chat", MODE_PRIVATE);
//
//            if (sharedChatDataS.contains(groupID_))
//            {
//                JSONArray jsonArrMsgs = new JSONArray(sharedChatDataS.getString(groupID_, "") + "");
//
//                for (int i = 0; i < jsonArrMsgs.length() - 40; ++i)  // TODO:
//                {
//                    JSONObject jobjinner = jsonArrMsgs.getJSONObject(i);
//
//                    String userId = jobjinner.getString(GlobalConstant.USER_ID);
//                    String message = jobjinner.getString("message");
//                    String date = jobjinner.getString("date");
//                    String username = jobjinner.getString("user_name");
//                    String imgURL = jobjinner.getString("image");
//                    String fbID = jobjinner.getString(GlobalConstant.FACE_BOOK_ID);
//                    String youtube = jobjinner.getString("youtube");
//                    String userPhone = jobjinner.getString("user_telephone");
//
//                    chatDataListGroup_.add(new Chat_Single(userId, message, date, username, imgURL, youtube, fbID, userPhone));
//                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void jsonToList(String stringjarray)
    {
        try
        {
            JSONArray jsonArrMsgs = new JSONArray(stringjarray + "");

//            if (chatDataListGroup_.size() > 0)
//                chatDataListGroup_.clear();
//
//            int StartFrom = 0;
//
//            if (jsonArrMsgs.length() > 40)
//                StartFrom = jsonArrMsgs.length() - 40;

            for (int i = 0; i < jsonArrMsgs.length(); ++i)
            {
                JSONObject jobjinner = jsonArrMsgs.getJSONObject(i);

                String userId = jobjinner.getString(GlobalConstant.USER_ID);
                String message = jobjinner.getString("message");
                String date = jobjinner.getString("date");
                String username = jobjinner.getString("user_name");
                String imgURL = jobjinner.getString("image");
                String fbID = jobjinner.getString(GlobalConstant.FACE_BOOK_ID);
                String youtube = jobjinner.getString("youtube");
                String userPhone = jobjinner.getString("user_telephone");


                Chat_Single chat_single = new Chat_Single(userId, message, date, username, imgURL, youtube, fbID, userPhone);

                chatDataListGroup_.add(chat_single);

                if (GlobalUtills.mygroups_savelocal)
                {
                    /***************By Harpreet****************************************/
                    db.createTable("G" + groupID_);
                    /******************************************************/
                    db.insertRecords(chat_single);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static Global global_;
    private static String dateToMatch_ = "";
    private static String groupID_ = "";
    private static String groupName_ = "";
    private static String groupImage_ = "";
    private static boolean refreshChat_ = false;

    //--------------------------------------------------------------------------------------------------------------------------
    private ActionBarCommon actionBarCommon_;
    private ImageButton buttonSendChat_;
    private EmojiconEditText editMsg_;
    private LinearLayout layoutChat_;
    private ScrollView uiScroll_;
    private String msgToSend_ = "";
    private String visibility_ = "Y";
    private String uID_ = "";
    private String dateTemp_ = "";

    private ProgressBar progressBarSendMsgGroup_;
    private HashMap<Integer, String> hashMapGetIDs_ = new HashMap<Integer, String>();
    private HashMap<Integer, String> hashMapGetPhones_ = new HashMap<Integer, String>();
    private String phoneNumber_[];
    private String appendedFriendIDs_ = "";
    private String appendedPhoneNumber_ = "";
    private boolean checkKeyboard_ = true;
    private FrameLayout layoutSmily_;
    private FrameLayout frameEmoji_;
    private ImageView imgViewSmily_;
    private ImageView imgViewKeyboard_;
    private ImageView uiImgViewTakePic_;
    private ToggleButton toggleButton_;
    private ArrayList<String> friendID_ = new ArrayList<String>();
    private String imgBytes_ = "";
    private String myFbID_ = "";
    private List<Chat_Single> chatDataListGroup_ = new ArrayList<Chat_Single>();
    private GlobalUtills globalUtills_;
}
