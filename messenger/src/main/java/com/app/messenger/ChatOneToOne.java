//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.model.Chat_Single;
import com.app.util.DBhelperG;
import com.app.util.GlobalUtills;
import com.app.util.RoundedCornersGaganImageView;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.Block_Unblock;
import com.app.webserviceshandler.GetRatings;
import com.app.webserviceshandler.GetunreadSingle;
import com.app.webserviceshandler.Set_Rating;
import com.app.webserviceshandler.WebServiceHandler;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------


//==============================================================================================================================
public class ChatOneToOne extends FragmentActivity implements AnimationListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener
{

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";

    //--------------------------------------------------------------------------------------------------------------------------
    private static boolean refreshChat_ = false;
    private static boolean generateNoTi_ = false;  // TODO: NO what?.
    private static ImageView imageViewErase_;
    private static LinearLayout layoutChat_;
    private static String facebookID_ = "";
    private static String myFacebookID_ = "";
    private static String shareImage_ = "";
    private static String dateToMatch_ = "";
    //--------------------------------------------------------------------------------------------------------------------------
    private ActionBarCommon actionBarCommon_;
    private Animation animationErase_;
    private boolean checkKeyboard_ = true;
    private boolean messageOrNot_ = false;
    private EmojiconEditText emojiEditMessage_;
    private FrameLayout layoutSmily_;
    private FrameLayout frameEmoji_;
    private Global global_;
    private GlobalUtills globalUtills_;
    private ImageButton buttonSendChat_;
    private ImageView imageViewSmily_;
    private ImageView imageViewKeyboard_;
    private ImageView imageViewTakePicture_;
    private LinearLayout layoutSendChat_;
    private LinearLayout layoutSendChatBlocked_;
    private List<Chat_Single> chatDataList_ = new ArrayList<Chat_Single>();
    private ProgressBar progressBarSendMessage_;
    private ScrollView scrollView_;
    //    private SharedPreferences sharedChatDataUnread_;
    private String messageToSend_ = "";
    private String name_ = "";
    private String phoneNumber_ = "";
    private String temporaryDate_ = "";
    private String imageBytes_ = "";
    private String blocked_ = "";


    //--------------------------------------------------------------------------------------------------------------------------
    private Handler responseHandlerGEtBlocked = new Handler()  // TODO:
    {
        public void handleMessage(Message msg)
        {
            try
            {
                Bundle bundle = msg.getData();
                String blocking = bundle.getString("Block");

                if (blocking.contains("Y") || blocking.equals("B"))
                {
                    layoutSendChat_.setVisibility(View.GONE);
                    layoutSendChatBlocked_.setVisibility(View.VISIBLE);

                    blocked_ = "B";

                    if (blocking.equals("Y_other"))
                        blocked_ = "B_other";
                }
                else if (blocking.equals("U"))
                {
                    layoutSendChatBlocked_.setVisibility(View.GONE);
                    layoutSendChat_.setVisibility(View.VISIBLE);

                    blocked_ = "U";
                }
                else  // TODO:
                    blocked_ = "U";
            }
            catch (Exception error)
            {
            }
            catch (Error error)
            {
            }
        }
    };
    private String blockStat_ = "";
    private String responseMessage_ = "";
    //--------------------------------------------------------------------------------------------------------------------------
    private Handler responseHandlerCHat = new Handler()  // TODO: move to constructor
    {
        public void handleMessage(Message msg)
        {
            Log.d("OnGetUnreadMessage","responseHandlerChat");
            Bundle bundle = msg.getData();
            String data = bundle.getString("unread");

            unreadData(data);

            if (generateNoTi_ && GlobalUtills.allNotification)
            {
                String notification = "You have new message from " + name_;

                globalUtills_.generateNotification_local(ChatOneToOne.this, notification, 0);

                generateNoTi_ = false;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new GetunreadSingle(facebookID_, global_.getUser_id(), responseHandlerCHat).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            else
                new GetunreadSingle(facebookID_, global_.getUser_id(), responseHandlerCHat).execute();
        }



        ;
    };

    //--------------------------------------------------------------------------------------------------------------------------
    public static void setGenerateNoTi(boolean value)
    {
        generateNoTi_ = value;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void setShareImage(String value)
    {
        shareImage_ = value;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static boolean refreshChat()
    {
        return refreshChat_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static boolean generateNoTi()
    {
        return generateNoTi_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static String facebookID()
    {
        return facebookID_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static String shareImage()
    {
        return shareImage_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static String parseDateToddMMyyyy(String time)
    {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String result = null;

        try
        {
            date = inputFormat.parse(time);
            result = outputFormat.format(date);
        }
        catch (Exception | Error error)
        {

            error.printStackTrace();
            return "";
        }

        return result;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void unreadData(String msg)
    {
        String result = msg;
        Log.d("Unread Msg","Unread Msg " +"   "+result);
        try
        {
            JSONObject jsonObject = new JSONObject(result);

            responseMessage_ = jsonObject.getString("Message");

            Log.d("Unread Msg","responseMessage_" +"   "+responseMessage_);
            if (responseMessage_.equalsIgnoreCase("Success"))
            {
                messageOrNot_ = true;

                String messages = jsonObject.getString("msgInfo");
                Log.d("Unread Msg","msgInfo" +"   "+messages);
                JSONArray jsonMessages = new JSONArray(messages + "");

                JSONArray jsonOld = null;

//                if (sharedChatDataUnread_.contains(facebookID_))
//                    jsonOld = new JSONArray(sharedChatDataUnread_.getString(facebookID_, ""));
//                else
//                    jsonOld = new JSONArray();

                for (int index = 0; index < jsonMessages.length(); ++index)
                {
                    JSONObject currentJsonObject = jsonMessages.getJSONObject(index);

                    // TODO: why some of objects are vars and some of them are constants?
                    String userID = currentJsonObject.getString("userId");
                    String message = currentJsonObject.getString("message");
                    Log.d("Unread Msg","message" +"   "+message);
                    String date = currentJsonObject.getString("date");
                    String imageURL = currentJsonObject.getString("image");
                    String youtube = currentJsonObject.getString("youtube");

//                    try
//                    {
//                        if (sharedChatDataUnread_.contains(facebookID_))
//                        {
//                            JSONObject json = new JSONObject();
//
//                            json.put("userId", "" + userID);
//                            json.put("message", "" + message);
//                            json.put("date", "" + date);
//                            json.put("user_name", "G");
//                            json.put("image", "" + imageURL);
//                            json.put("youtube", "" + youtube);
//
//                            jsonOld.put(json);
//                        }
//                    }
//                    catch (JSONException error)
//                    {
//                        Editor editShared = sharedChatDataUnread_.edit();
//
//                        editShared.remove(facebookID_);
//                        editShared.apply();
//
//                        error.printStackTrace();
//                    }

                    List<Chat_Single> data = new ArrayList<Chat_Single>();

                    Chat_Single chat_single = new Chat_Single(userID, message, date, "", imageURL, youtube, "", "");
                    data.add(chat_single);

                    /***************By Harpreet****************************************/
                    db.createTable("G" + facebookID_);
                    /******************************************************/
                    db.insertRecords(chat_single);

                    parseMessageData(data, false, false);
                }

//                if (jsonMessages.length() > 0)
//                {
//                    Editor editShared = sharedChatDataUnread_.edit();
//
//                    editShared.putString(facebookID_, jsonOld.toString());
//                    editShared.apply();
//                }
            }
        }
        catch (Exception error)
        {
            db.dropTable("G" + facebookID_);
            error.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onEmojiconBackspaceClicked(View view)
    {
        EmojiconsFragment.backspace(emojiEditMessage_);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onEmojiconClicked(Emojicon emojicon)
    {
        EmojiconsFragment.input(emojiEditMessage_, emojicon);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed()
    {
        GlobalUtills.SingleChat_noti = true;
        GlobalUtills.SingleChat_Sender = "";

        if (!checkKeyboard_)
        {
            checkKeyboard_ = true;

            imageViewSmily_.setVisibility(View.VISIBLE);
            imageViewKeyboard_.setVisibility(View.INVISIBLE);
            frameEmoji_.setVisibility(View.GONE);
        }
        else
        {
            temporaryDate_ = "";
            refreshChat_ = false;

            super.onBackPressed();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add("Clear Chat").setIcon(android.R.drawable.ic_menu_delete);

        return super.onCreateOptionsMenu(menu);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (messageOrNot_)
            new AsyncClearMsg().execute();
        else
            GlobalUtills.showToast("No messages..!", ChatOneToOne.this);

        return super.onOptionsItemSelected(item);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onAnimationEnd(Animation animation)
    {
        if (animation == animationErase_)
        {
            // TODO:
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onAnimationRepeat(Animation animation)
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onAnimationStart(Animation animation)
    {
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void clearChat(final Context con)
    {
        animationErase_ = AnimationUtils.loadAnimation(con, R.anim.sequential);

        imageViewErase_.bringToFront();
        imageViewErase_.setVisibility(View.VISIBLE);
        imageViewErase_.startAnimation(animationErase_);

        new CountDownTimer(4000, 1000)  // TODO:
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
            }

            @Override
            public void onFinish()
            {
                chatDataList_.clear();

                imageViewErase_.setVisibility(View.GONE);
                imageViewErase_.setAnimation(null);

                layoutChat_.removeAllViews();

                messageOrNot_ = false;
                temporaryDate_ = "";
                dateToMatch_ = "";

                GlobalUtills.showToast("Messages deleted by " + name_ + " !", con);  // TODO:
            }
        }.start();
    }

    //--------------------------------------------------------------------------------------------------------------------------

    final int CHATLIST_SIZE = 50;

    private static int LOAD_MORE_TIMES = 0;
    // TODO: huge func
    public void parseMessageData(final List<Chat_Single> listChat, final boolean loadMore, final boolean index)
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                messageOrNot_ = true;
                dateToMatch_ = temporaryDate_;



                for (int chatIndex = 0; chatIndex < listChat.size(); ++chatIndex)
                {
                    showChat(listChat.get(chatIndex),chatIndex,listChat.size(),loadMore,index);
                }

                if (loadMore)
                {
                    if (db.getSizeOfChat() > CHATLIST_SIZE)
                    {
                        dateToMatch_ = "";

                        Button buttonLoadMore = new Button(ChatOneToOne.this);

                        buttonLoadMore.setText("LOAD MORE");
                        buttonLoadMore.setTextSize(18);
                        buttonLoadMore.setBackgroundColor(Color.parseColor("#3c3c3c"));
                        buttonLoadMore.setTextColor(Color.WHITE);
                        buttonLoadMore.setAlpha(.40f);

                        buttonLoadMore.setOnClickListener(new OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {

                                layoutChat_.removeViewAt(0);

                                LOAD_MORE_TIMES++;

                                if (db.getSizeOfChat() > CHATLIST_SIZE * LOAD_MORE_TIMES)
                                {
                                    chatDataList_ = db.getLastRecords(CHATLIST_SIZE, LOAD_MORE_TIMES);
                                }

                                new ShowMsgDataAsyncGroup(chatDataList_).execute();

//                                chatDataFromJsonLoadMore();

//                                Collections.reverse(chatDataList_);
//
//                                if (chatDataList_.size() > 40)  // TODO:
//
//
//                                else
//                                    parseMessageData(chatDataList_, false, true);
                            }
                        });

                        layoutChat_.addView(buttonLoadMore, 0);
                    }
                }







                if (!index)
                {
                    scrollView_.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            scrollView_.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }

                chatDataList_.clear();
            }
        };

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }





    private void showChat(Chat_Single chatSingle, int chatIndex, int listSize, final boolean loadmore, final boolean index)
    {

        // TODO:
        String userId =chatSingle.getUserId();
        final String message = chatSingle.getMessage();
        final String date = chatSingle.getDate();
        final String username = chatSingle.getUsername();
        final String imageURL = chatSingle.getImgURL();
        final String youtube = chatSingle.getYoutube();

        String formtDate = parseDateToddMMyyyy(date);

        if (formtDate == null || formtDate.equals(""))
        {
            db.dropTable("G" + facebookID_);
//                        SharedPreferences chatPreferences = getSharedPreferences("Chat", Context.MODE_PRIVATE);
//                        Editor editor = chatPreferences.edit();
//
//                        editor.remove(facebookID_);
//                        editor.apply();

            new ReceiveMsgAsyncOneToOne().execute();

            return;
        }

        if (index && chatIndex == 0)
        {
            dateToMatch_ = formtDate;
        }

        if (dateToMatch_.equalsIgnoreCase(formtDate))
        {
            // TODO:
        }
        else
        {
            if (!index)
            {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                layoutParams.gravity = Gravity.CENTER;

                TextView textViewDate = new TextView(ChatOneToOne.this);

                textViewDate.setLayoutParams(layoutParams);
                textViewDate.setText(formtDate + "");
                textViewDate.setTextColor(Color.DKGRAY);
                textViewDate.setTextSize(18);
                textViewDate.setPadding(5, 5, 5, 5);
                textViewDate.setGravity(Gravity.CENTER);

                layoutChat_.addView(textViewDate);
            }
            else
            {
                // TODO: c/p
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                layoutParams.gravity = Gravity.CENTER;

                TextView textViewDate = new TextView(ChatOneToOne.this);

                textViewDate.setLayoutParams(layoutParams);
                textViewDate.setText(dateToMatch_ + "");
                textViewDate.setTextColor(Color.DKGRAY);
                textViewDate.setTextSize(18);
                textViewDate.setPadding(5, 5, 5, 5);
                textViewDate.setGravity(Gravity.CENTER);

                if (chatIndex == 0)
                {
                    // TODO:
                }
                else
                    layoutChat_.addView(textViewDate, 0);
            }
        }

        if (index)
            dateToMatch_ = formtDate;

        else
        {
            temporaryDate_ = formtDate;
            dateToMatch_ = temporaryDate_;
        }

        LinearLayout layoutMsgContainer = new LinearLayout(ChatOneToOne.this);

        if (userId.equals(global_.getUser_id()))
        {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            layoutParams.gravity = Gravity.LEFT;

            layoutMsgContainer.setLayoutParams(layoutParams);

            View view = LayoutInflater.from(ChatOneToOne.this).inflate(R.layout.chat_my, layoutMsgContainer);

            EmojiconTextView textViewMessage = (EmojiconTextView) view.findViewById(R.id.txtV_MyChatmsg);
            RoundedCornersGaganImageView imageView = (RoundedCornersGaganImageView) view.findViewById(R.id.imgV_MyChatImage);
            Facebook_ProfilePictureView_rounded profilePicture = (Facebook_ProfilePictureView_rounded) view.findViewById(R.id.fb_MyChat);
            LinearLayout framemsgLayout = (LinearLayout) view.findViewById(R.id.FrameLayoutMyChat);
            TextView textViewTime = (TextView) view.findViewById(R.id.txtV_MyChatDate);
            FrameLayout imageContainer = (FrameLayout) view.findViewById(R.id.FrameLayoutIMAGE);
            ImageButton buttonPlay = (ImageButton) view.findViewById(R.id.img_btn_play_youtube_video);

            profilePicture.setProfileId(myFacebookID_);

            textViewTime.setText(globalUtills_.parseDateToTime(date));

            if (imageURL.length() > 2)  // TODO:
            {
                textViewMessage.setVisibility(View.GONE);
                imageContainer.setVisibility(View.VISIBLE);

                imageView.setImageUrl(ChatOneToOne.this, imageURL);
                imageView.bringToFront();

                imageView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        globalUtills_.ViewPhoto(imageURL, ChatOneToOne.this, "");
                    }
                });
            }
            else if (youtube.length() > 5)  // TODO:
            {
                String youtubeURL = youtube.substring(0, youtube.indexOf(","));
                final String youtubeID = youtube.substring(youtubeURL.length() + 1, youtube.length());
                Log.i("Message1","Message1" + "   " + message);
                if (!message.isEmpty())
                {
                    textViewMessage.setText(message);                }
                else
                {
                    textViewMessage.setVisibility(View.GONE);
                }


                imageContainer.setVisibility(View.VISIBLE);
                imageView.setImageUrl(ChatOneToOne.this, "https://" + youtubeURL);
                imageView.setScaleType(ScaleType.FIT_XY);

                buttonPlay.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(ChatOneToOne.this, YoutubeVideoPlayer.class);

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

                textViewMessage.setText(message);
                textViewTime.setText(globalUtills_.parseDateToTime(date));
            }
        }
        else
        {
            // TODO: c/p
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            layoutParams.gravity = Gravity.RIGHT;

            layoutMsgContainer.setLayoutParams(layoutParams);

            View viewOther = LayoutInflater.from(ChatOneToOne.this).inflate(R.layout.other_chat, layoutMsgContainer);

            EmojiconTextView textViewMessage = (EmojiconTextView) viewOther.findViewById(R.id.txtV_otherChatmsg);

            RoundedCornersGaganImageView imageView = (RoundedCornersGaganImageView) viewOther.findViewById(R.id.imgV_otherChatImage);

            Facebook_ProfilePictureView_rounded profilePicture = (Facebook_ProfilePictureView_rounded) viewOther.findViewById(R.id.fb_otherChat);

            LinearLayout framemsgLayout = (LinearLayout) viewOther.findViewById(R.id.FrameLayoutotherChat);

            TextView textViewName = (TextView) viewOther.findViewById(R.id.txtVChatName);

            textViewName.setVisibility(View.GONE);

            TextView textViewTime = (TextView) viewOther.findViewById(R.id.txtV_otherChatDate);
            FrameLayout imageContainer = (FrameLayout) viewOther.findViewById(R.id.FrameLayoutIMAGE_Other);

            profilePicture.setProfileId(facebookID_);

            textViewTime.setText(globalUtills_.parseDateToTime(date));

            if (imageURL.length() > 2)  // TODO:
            {
                textViewMessage.setVisibility(View.GONE);
                imageContainer.setVisibility(View.VISIBLE);

                imageView.setImageUrl(ChatOneToOne.this, imageURL);
                imageView.bringToFront();

                imageView.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        globalUtills_.ViewPhoto(imageURL, ChatOneToOne.this, username + globalUtills_.parseDateToSAVE(date));
                    }
                });
            }
            else if (youtube.length() > 5)  // TODO:
            {
                String youtubeURL = youtube.substring(0, youtube.indexOf(","));
                final String youtubeID = youtube.substring(youtubeURL.length() + 1, youtube.length());
                Log.i("Message2","Message2" + "   " + message);
                if (!message.isEmpty())
                {
                    textViewMessage.setText(message);
                }
                else
                {
                    textViewMessage.setVisibility(View.GONE);
                }
                imageContainer.setVisibility(View.VISIBLE);

                imageView.setImageUrl(ChatOneToOne.this, "https://" + youtubeURL);
                imageView.setScaleType(ScaleType.FIT_XY);

                ImageButton buttonPlay = (ImageButton) viewOther.findViewById(R.id.img_btn_play_youtube_video_other);

                buttonPlay.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(ChatOneToOne.this, YoutubeVideoPlayer.class);

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

                textViewMessage.setText(message);
                textViewTime.setText(globalUtills_.parseDateToTime(date));
            }
        }

        if (index)  // TODO: -1 will be true
        {
            layoutChat_.addView(layoutMsgContainer, 0);

            if (chatIndex == listSize - 1)
            {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER;

                TextView textViewDate = new TextView(ChatOneToOne.this);

                textViewDate.setLayoutParams(layoutParams);
                textViewDate.setText(formtDate + "");
                textViewDate.setTextColor(Color.DKGRAY);
                textViewDate.setTextSize(18);
                textViewDate.setPadding(5, 5, 5, 5);
                textViewDate.setGravity(Gravity.CENTER);

                layoutChat_.addView(textViewDate, 0);
            }
        }
        else  // TODO:
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

                    GlobalUtills.showToast("Text Copied to clip board", ChatOneToOne.this);
                }

                return false;
            }
        });
    }




    //--------------------------------------------------------------------------------------------------------------------------
    public void getUnread(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new GetunreadSingle(facebookID_, global_.getUser_id(), responseHandlerCHat).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        else
            new GetunreadSingle(facebookID_, global_.getUser_id(), responseHandlerCHat).execute();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void chooseDialog()
    {
        final Dialog dialogLoader = new Dialog(this, R.style.Theme_Dialog);

        dialogLoader.setTitle("Select a Image ");
        dialogLoader.setContentView(R.layout.dialogo_choose);

        dialogLoader.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        ImageButton buttonGallery = (ImageButton) dialogLoader.findViewById(R.id.uiC_imgbtntake_gallery);
        ImageButton buttonCamera = (ImageButton) dialogLoader.findViewById(R.id.uiC_imgbtntake_camera);
        ImageButton buttonYoutube = (ImageButton) dialogLoader.findViewById(R.id.btn_youTube);

        buttonYoutube.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogLoader.dismiss();

                Intent gotoYouTube = new Intent(ChatOneToOne.this, YouTubeVideosList.class);

                startActivity(gotoYouTube);
            }
        });

        buttonGallery.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogLoader.dismiss();

                Intent pickPhoto = new Intent(Intent.ACTION_PICK);

                pickPhoto.setType("image/*");

                startActivityForResult(pickPhoto, GlobalConstant.REQUEST_CODE_22); // TOOD:
            }
        });

        buttonCamera.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogLoader.dismiss();

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getTempUri());
                cameraIntent.putExtra("return-data", true);

                startActivityForResult(cameraIntent, GlobalConstant.REQUEST_CODE_1888);  // TODO:
            }
        });

        dialogLoader.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onStop()
    {
        responseHandlerCHat = null;

        super.onStop();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("OnGetUnreadMessage","OnResumeFunctioncall");


        globalUtills_ = new GlobalUtills();

        if (GlobalUtills.YouTube_URL.length() > 2)  // TODO:
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new SendMsgAsyncOneToOne().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

            else
                new SendMsgAsyncOneToOne().execute("");
        }
/////////////////////////////////////////////
        refreshChat_ = true;
/////////////////////////////////////////////////
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);

        global_.setUser_id(preferences.getString("UserID", ""));
        if (GlobalUtills.flag_onPausedToResume == true){
            responseHandlerCHat = GlobalUtills.responseSaveHandler;
            GlobalUtills.flag_onPausedToResume = false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new GetunreadSingle(facebookID_, global_.getUser_id(), responseHandlerCHat).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        else
            new GetunreadSingle(facebookID_, global_.getUser_id(), responseHandlerCHat).execute();

        if (facebookID_.length() > 0)
            GlobalUtills.SingleChat_Sender = facebookID_;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(0);

//        sharedChatDataUnread_ = getSharedPreferences("Chat", Context.MODE_PRIVATE);
//

    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onPause()
    {
        Log.d("OnGetUnreadMessage","OnPauseFunctionCall");
        GlobalUtills.SingleChat_noti = true;
        GlobalUtills.SingleChat_Sender = "facebookID_";
        GlobalUtills.flag_onPausedToResume = true;
        GlobalUtills.responseSaveHandler = responseHandlerCHat;
        refreshChat_ = false;

        super.onPause();

    }

    DBhelperG db;

    //--------------------------------------------------------------------------------------------------------------------------
    // TODO: huge func
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("OnGetUnreadMessage","OnCreatFunctionCall");

        try
        {
            Global.showAdsIfNeeded(ChatOneToOne.this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.chat__one_to_one);


        layoutSendChat_ = (LinearLayout) findViewById(R.id.layoutSendChat);
        layoutSendChatBlocked_ = (LinearLayout) findViewById(R.id.layoutSendChatNO);
        scrollView_ = ((ScrollView) findViewById(R.id.uiC_scrllView_OneToOne));
        layoutChat_ = (LinearLayout) findViewById(R.id.layoutChat_OneToOne);
        layoutSmily_ = (FrameLayout) findViewById(R.id.linear_layout_smily_one_to_one);
        buttonSendChat_ = (ImageButton) findViewById(R.id.btnSentChat_OneToOne);
        emojiEditMessage_ = (EmojiconEditText) findViewById(R.id.edMsg_OneToOne);
        progressBarSendMessage_ = (ProgressBar) findViewById(R.id.uiC_progressBSendMsg);
        imageViewSmily_ = (ImageView) findViewById(R.id.img_view_smily_one_to_one);
        imageViewKeyboard_ = (ImageView) findViewById(R.id.img_view_keyboard_one_to_one);
        frameEmoji_ = (FrameLayout) findViewById(R.id.emojicons);
        imageViewTakePicture_ = (ImageView) findViewById(R.id.uiC_imgVtakePic);
        imageViewErase_ = (ImageView) findViewById(R.id.img_view_erase);

        generateNoTi_ = false;
        refreshChat_ = true;
        global_ = new Global();
        globalUtills_ = new GlobalUtills();
        GlobalUtills.YouTube_URL = "";

        Intent intentData = getIntent();

        name_ = intentData.getStringExtra("name");
        facebookID_ = intentData.getStringExtra("fbID") + "";
        phoneNumber_ = intentData.getStringExtra("ph");


        db = new DBhelperG(ChatOneToOne.this, "G" + facebookID_);


        GlobalUtills.SingleChat_Sender = facebookID_;

        SharedPreferences preferences = getSharedPreferences("fbID", MODE_PRIVATE);

        myFacebookID_ = "";
        myFacebookID_ = preferences.getString("fb", "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new Block_Unblock(ChatOneToOne.this, global_.getUser_id(), facebookID_, responseHandlerGEtBlocked).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        else
            new Block_Unblock(ChatOneToOne.this, global_.getUser_id(), facebookID_, responseHandlerGEtBlocked).execute();

        actionBarCommon_ = new ActionBarCommon(ChatOneToOne.this, null);  // TODO:
        actionBarCommon_ = (ActionBarCommon) findViewById(R.id.action_barC_OneToOne);

        actionBarCommon_.setActionText(name_ + "");
        actionBarCommon_.text().setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.ic_menu_info_details, 0);

        actionBarCommon_.setLayoutCenterClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialogOptions("" + name_, phoneNumber_, facebookID_);
            }
        });

        actionBarCommon_.setProfilePic(ChatOneToOne.this, facebookID());

        actionBarCommon_.setFblink(ChatOneToOne.this, facebookID());


        actionBarCommon_.leftImage().setImageResource(R.drawable.icon_back_arrow);

        actionBarCommon_.layoutLeft().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(emojiEditMessage_.getWindowToken(), 0);

                temporaryDate_ = "";
                refreshChat_ = false;

                GlobalUtills.SingleChat_noti = true;

                finish();
            }
        });

        actionBarCommon_.rightImage().setImageResource(R.drawable.delete);

        actionBarCommon_.layoutRight().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (globalUtills_.haveNetworkConnection(ChatOneToOne.this))
                {
                    if (messageOrNot_)
                    {
                        temporaryDate_ = "";

                        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(emojiEditMessage_.getWindowToken(), 0);

                        refreshChat_ = false;

                        new AsyncClearMsg().execute();
                    }
                    else  // TODO:
                        GlobalUtills.showToast("No messages..!", ChatOneToOne.this);
                }
                else
                    GlobalUtills.showToast("No network connection..!", ChatOneToOne.this);
            }
        });

        emojiEditMessage_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
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
            public void onClick(View view)
            {
                if (checkKeyboard_)
                {
                    checkKeyboard_ = false;

                    imageViewSmily_.setVisibility(View.INVISIBLE);
                    imageViewKeyboard_.setVisibility(View.VISIBLE);

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    frameEmoji_.setVisibility(View.VISIBLE);
                }
                else
                {
                    checkKeyboard_ = true;

                    imageViewSmily_.setVisibility(View.VISIBLE);
                    imageViewKeyboard_.setVisibility(View.INVISIBLE);

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.showSoftInput(emojiEditMessage_, InputMethodManager.SHOW_IMPLICIT);

                    frameEmoji_.setVisibility(View.GONE);
                }
            }
        });

        buttonSendChat_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (emojiEditMessage_.getText().toString().trim().equals(""))
                    GlobalUtills.showToast("nothing to send ..!", ChatOneToOne.this);

                else
                {
                    messageToSend_ = emojiEditMessage_.getText().toString().trim();

                    if (globalUtills_.haveNetworkConnection(ChatOneToOne.this))
                    {
                        GlobalUtills.YouTube_URL = "";
                        GlobalUtills.YouTube_VIdeoID = "";

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            new SendMsgAsyncOneToOne().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, messageToSend_);

                        else
                            new SendMsgAsyncOneToOne().execute(messageToSend_);
                    }
                    else  // TODO:
                        GlobalUtills.showToast("No internet connection..!", ChatOneToOne.this);
                }
            }
        });

        imageViewTakePicture_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (globalUtills_.haveNetworkConnection(getApplicationContext()))
                    chooseDialog();

                else
                    GlobalUtills.showToast("No network connection..!", ChatOneToOne.this);
            }
        });

        emojiEditMessage_.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count)
            {
                if (emojiEditMessage_.getText().toString().length() > 0)
                {
                    imageViewTakePicture_.setVisibility(View.GONE);
                    buttonSendChat_.setVisibility(View.VISIBLE);
                }
                else
                {
                    imageViewTakePicture_.setVisibility(View.VISIBLE);
                    buttonSendChat_.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence sequence, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });

//        SharedPreferences chatPreferences = getSharedPreferences("Chat", MODE_PRIVATE);

        chatDataList_ = db.getTopChatList(CHATLIST_SIZE);


        if (globalUtills_.haveNetworkConnection(ChatOneToOne.this))
        {
            if (chatDataList_.size() > 0)
                parseMessageData(chatDataList_, true, false);

            else
                new ReceiveMsgAsyncOneToOne().execute();
        }
        else
        {
            if (chatDataList_.size() > 0)  // TODO:
                new ShowMsgDataAsyncGroup(chatDataList_).execute();
        }

        setEmojiconFragment(false);

        animationErase_ = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.sequential);

        animationErase_.setAnimationListener(this);

        if (!shareImage_.isEmpty())
        {
            processBitmap(BitmapDecoder.decodeFile(shareImage_));

            GlobalUtills.showToast("Sharing Image....", ChatOneToOne.this);
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

        imageBytes_ = globalUtills_.BitMapToString(bitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new SendMsgAsyncOneToOne().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

        else
            new SendMsgAsyncOneToOne().execute("");

        shareImage_ = "";
    }


    //--------------------------------------------------------------------------------------------------------------------------
//    private int chatLength()
//    {
//        try
//        {
//            SharedPreferences chatPreferences = getSharedPreferences("Chat", MODE_PRIVATE);
//
//            if (chatPreferences.contains(facebookID_))
//            {
//                JSONArray jsonMessages = new JSONArray(chatPreferences.getString(facebookID_, "") + "");
//
//                return jsonMessages.length();  // TODO:
//            }
//            else  // TODO:
//                return 0;
//        }
//        catch (Exception error)
//        {
//            error.printStackTrace();
//
//            return 0;
//        }
//    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void chatDataFromJson(String jsonStrings)
    {
        try
        {
            JSONArray jsonMessages = new JSONArray(jsonStrings + "");
            Log.i("Receiver JOSN","Receive");

            if (chatDataList_.size() > 0)
                chatDataList_.clear();

//			int startFrom = 0;
//
//			if (jsonMessages.length() > 40)  // TODO:
//				startFrom = jsonMessages.length() - 40;  // TODO:

            for (int index = 0; index < jsonMessages.length(); ++index)
            {
                JSONObject jsonObject = jsonMessages.getJSONObject(index);

                String userId = jsonObject.getString("userId");
                String message = jsonObject.getString("message");
                String date = jsonObject.getString("date");
                String userName = jsonObject.getString("user_name");
                String imageURL = jsonObject.getString("image");
                String youtube = jsonObject.getString("youtube");
                Log.i("Receiver JOSN","Receive" + "   " + message);

                Chat_Single chat_single = new Chat_Single(userId, message, date, userName, imageURL, youtube, "", "");
                chatDataList_.add(chat_single);



                /***************By Harpreet****************************************/
                db.createTable("G" + facebookID_);
                /******************************************************/
                db.insertRecords(chat_single);


            }
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
//    private void chatDataFromJsonLoadMore()
//    {
//        try
//        {
//            if (chatDataList_.size() > 0)
//                chatDataList_.clear();
//
//            SharedPreferences chatPreferences = getSharedPreferences("Chat", MODE_PRIVATE);
//
//            if (chatPreferences.contains(facebookID_))
//            {
//                JSONArray jsonMessages = new JSONArray(chatPreferences.getString(facebookID_, "") + "");
//
//                for (int index = 0; index < jsonMessages.length() - 40; ++index)  // TODO:
//                {
//                    JSONObject jsonObject = jsonMessages.getJSONObject(index);
//
//                    String userId = jsonObject.getString("userId");
//                    String message = jsonObject.getString("message");
//                    String date = jsonObject.getString("date");
//                    String userName = jsonObject.getString("user_name");
//                    String imageURL = jsonObject.getString("image");
//                    String youtube = jsonObject.getString("youtube");
//
//                    chatDataList_.add(new Chat_Single(userId, message, date, userName, imageURL, youtube, "", ""));
//                }
//            }
//        }
//        catch (Exception error)
//        {
//            error.printStackTrace();
//        }
//    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void setEmojiconFragment(boolean useSystemDefault)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private Uri getTempUri()
    {
        return Uri.fromFile(getTempFile());
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private File getTempFile()  // TODO: c/p
    {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            File file = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);

            try
            {
                file.createNewFile();
            }
            catch (IOException error)
            {
            }

            return file;  // TODO: invalid file can be returned.
        }
        else  // TODO:
            return null;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void dialogOptions(final String name, final String phoneNumber, final String facebookID)
    {
        final Dialog dialog = globalUtills_.prepararDialog(ChatOneToOne.this, R.layout.profile_info_other);

        dialog.setCanceledOnTouchOutside(true);

        final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBarG);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();

        stars.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.parseColor("#f4d334"), PorterDuff.Mode.SRC_ATOP);

        com.app.messenger.Facebook_ProfilePictureView_rounded pic = (com.app.messenger.Facebook_ProfilePictureView_rounded) dialog.findViewById(R.id.ImageV_user);

        pic.setProfileId(facebookID);

        TextView textViewName = (TextView) dialog.findViewById(R.id.txtvProfileNameOther);
        TextView textViewPhone = (TextView) dialog.findViewById(R.id.txtv_phoneCAllother);

        textViewName.setText("" + name);
        textViewPhone.setText("" + com.app.util.GlobalConstant.hideNumber(phoneNumber));

        textViewPhone.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent callIntent = new Intent(Intent.ACTION_CALL);

                callIntent.setData(Uri.parse("tel:" + phoneNumber));

                startActivity(callIntent);
            }
        });

        final TextView avarageRating = (TextView) dialog.findViewById(R.id.textViewAVG);
        final RatingBar avarageRatingBar = (RatingBar) dialog.findViewById(R.id.ratingBarAVG);

        Handler responseHandlerRatings = new Handler()
        {
            public void handleMessage(Message message)
            {
                try
                {
                    Bundle bundle = message.getData();
                    String ratings = bundle.getString("ratings");

                    avarageRating.setText(ratings);

                    avarageRatingBar.setRating(Float.parseFloat(ratings));

                    ratingBar.setRating(Float.parseFloat(bundle.getString("user_rating")));
                }
                catch (Exception error)
                {
                }
                catch (Error error)
                {
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            new GetRatings(global_.getUser_id(), facebookID_, responseHandlerRatings).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        else
            new GetRatings(global_.getUser_id(), facebookID_, responseHandlerRatings).execute();

        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new Set_Rating(global_.getUser_id(), facebookID_, rating + "").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                else
                    new Set_Rating(global_.getUser_id(), facebookID_, rating + "").execute();
            }
        });

        TextView textViewFacebook = (TextView) dialog.findViewById(R.id.txtV_FB);

        textViewFacebook.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(globalUtills_.getOpenFacebookIntent(ChatOneToOne.this, facebookID));
            }
        });

        Button buttonBlock = (Button) dialog.findViewById(R.id.btnBlocked);

        if (blocked_.equals("B_other"))
        {
            buttonBlock.setText("You are Blocked..!");
            buttonBlock.setEnabled(false);

            textViewFacebook.setEnabled(false);
            textViewPhone.setEnabled(false);
            ratingBar.setEnabled(false);

            blockStat_ = "U";  // TODO: use enums
        }
        else if (blocked_.equals("B"))
        {
            buttonBlock.setText("Unblock");

            blockStat_ = "U";
        }
        else
        {
            buttonBlock.setText("Block");

            blockStat_ = "B";
        }

        buttonBlock.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                new Block_Unblock(ChatOneToOne.this, global_.getUser_id(), facebookID_, "" + blockStat_, responseHandlerGEtBlocked).execute();
                dialog.dismiss();
            }
        });

        Button buttonShowGroups = (Button) dialog.findViewById(R.id.btnShow_groups);

        buttonShowGroups.setText(name + "'s" + " Groups");

        buttonShowGroups.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent gotoHangoutGroups = new Intent(ChatOneToOne.this, HangoutFriendGroup.class);

                gotoHangoutGroups.putExtra("FrndID", facebookID + "");

                startActivity(gotoHangoutGroups);
            }
        });

        dialog.show();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // ASYNC CLASS TO SEND MSGS
    public class SendMsgAsyncOneToOne extends AsyncTask<String, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        private String youtubeThumbnail = "";

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            if (!(GlobalUtills.YouTube_URL.equals("")))
            {
                youtubeThumbnail = GlobalUtills.YouTube_URL.substring(8, GlobalUtills.YouTube_URL.length());
                youtubeThumbnail = youtubeThumbnail + "," + GlobalUtills.YouTube_VIdeoID;

                GlobalUtills.YouTube_URL = "";
                GlobalUtills.YouTube_VIdeoID = "";

                messageToSend_ = GlobalUtills.youtubeTitile;
                Log.i("messageToSend","messageToSend" + "        "  +  messageToSend_);
            }

            if (imageBytes_.equals(""))
            {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(calendar.getTime());

                Chat_Single chat = new Chat_Single(global_.getUser_id(), messageToSend_, formattedDate, "", "", "" + youtubeThumbnail, facebookID_, "");
                List<Chat_Single> chatList = new ArrayList<Chat_Single>();
                chatList.add(chat);
                Log.i("messageToSend","messageToSend" + "        "  +  messageToSend_);

                try
                {
                    Log.i("messageToSend","messageToSend" + "        "  +  chat.getMessage());
                    /***************By Harpreet****************************************/
                    db.createTable("G" + facebookID_);
                    /******************************************************/
                    db.insertRecords(chat);


//                    SharedPreferences sharedChatDataS = getSharedPreferences("Chat", MODE_PRIVATE);
//                    JSONArray jsonOld = null;

//                    if (sharedChatDataS.contains(facebookID_))
//                    {
//                        jsonOld = new JSONArray(sharedChatDataS.getString(facebookID_, ""));
//
//                        JSONObject json = new JSONObject();
//
//                        json.put("userId", "" + global_.getUser_id());
//                        json.put("message", "" + messageToSend_);
//                        json.put("date", "" + formattedDate);
//                        json.put("user_name", "G");
//                        json.put("image", "" + "");
//                        json.put("youtube", "" + youtubeThumbnail);
//
//                        jsonOld.put(json);
//
//                        Editor editShared = sharedChatDataS.edit();
//
//                        editShared.putString(facebookID_, jsonOld.toString());
//                        editShared.apply();
//                    }
//                    else
//                    {
//                        // TODO: copy/paste
//                        jsonOld = new JSONArray();
//
//                        JSONObject json = new JSONObject();
//
//                        json.put("userId", "" + global_.getUser_id());
//                        json.put("message", "" + messageToSend_);
//                        json.put("date", "" + formattedDate);
//                        json.put("user_name", "G");
//                        json.put("image", "" + "");
//                        json.put("youtube", "" + youtubeThumbnail);
//
//                        jsonOld.put(json);
//
//                        Editor editShared = sharedChatDataS.edit();
//
//                        editShared.putString(facebookID_, jsonOld.toString());
//                        editShared.apply();
//                    }
                }
                catch (Exception error)
                {
                }

                parseMessageData(chatList, false, false);
            }
            else  // TODO:
            {
                progressBarSendMessage_.setVisibility(View.VISIBLE);

                buttonSendChat_.setEnabled(false);
            }

            emojiEditMessage_.setText("");

            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            String message = params[0];
            Log.i("messageToSend","messageToSend" + "        "  +  message);
            String jsonString = "";

            try
            {
                // TODO:
                List<NameValuePair> param = new ArrayList<>();

                if (imageBytes_.equals(""))
                {
                    param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
                    param.add(new BasicNameValuePair("mtype", "save_single_chat"));
                }

                param.add(new BasicNameValuePair("user_id", global_.getUser_id() + ""));
                param.add(new BasicNameValuePair("facebook_id", facebookID_));
                param.add(new BasicNameValuePair("message", message + ""));
                param.add(new BasicNameValuePair("image", imageBytes_ + ""));
                param.add(new BasicNameValuePair("youtube", youtubeThumbnail));

                if (imageBytes_.equals(""))
                {
                    String url = "" + GlobalConstant.URL;
                    String paramString = URLEncodedUtils.format(param, "UTF-8");
                    Log.i("messageToSend_parse","messageToSend_parse" + "        "  +  paramString);

                    url += "?" + paramString;

                    System.out.println(url);

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpEntity httpEntity = null;
                    HttpResponse httpResponse = null;
                    HttpGet httpGet = new HttpGet(url);

                    httpResponse = httpClient.execute(httpGet);
                    httpEntity = httpResponse.getEntity();
                    jsonString = EntityUtils.toString(httpEntity);
                }
                else
                {
                    WebServiceHandler webServiceHandler = new WebServiceHandler();

                    jsonString = webServiceHandler.makeServiceCallSendchat(GlobalConstant.URLSendMSG, WebServiceHandler.POST, param);
                }
            }
            catch (Exception error)
            {
                return "error";  // TODO:
            }
            catch (OutOfMemoryError error)
            {
                return "error";
            }

            return jsonString;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                if (result.contains("Success"))
                {
                    if (!imageBytes_.equals(""))
                    {
                        messageOrNot_ = true;

                        try
                        {
                            JSONObject jsonImage = new JSONObject(result);
                            String imageURL = jsonImage.getString("image");
//                            SharedPreferences chatPreferences = getSharedPreferences("Chat", MODE_PRIVATE);
//                            JSONArray jsonOld = null;

//                            if (chatPreferences.contains(facebookID_))  // TODO: copy/paste
//                            {
//                                jsonOld = new JSONArray(chatPreferences.getString(facebookID_, ""));

//                                JSONObject json = new JSONObject();
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = dateFormat.format(calendar.getTime());

//                                json.put("userId", "" + global_.getUser_id());
//                                json.put("message", "" + "");
//                                json.put("date", "" + formattedDate);
//                                json.put("user_name", "G");
//                                json.put("image", "" + imageURL);
//                                json.put("youtube", "" + youtubeThumbnail);
//
//                                jsonOld.put(json);

//                                Editor editShared = chatPreferences.edit();

//                                editShared.putString(facebookID_, jsonOld.toString());
//                                editShared.apply();

                            buttonSendChat_.setEnabled(true);

                            progressBarSendMessage_.setVisibility(View.GONE);

                            Chat_Single chat = new Chat_Single(global_.getUser_id(), "", formattedDate, "", imageURL, "", facebookID_, "");
                            List<Chat_Single> chatList = new ArrayList<Chat_Single>();

                            chatList.add(chat);

                            parseMessageData(chatList, false, false);


                            /***************By Harpreet****************************************/
                            db.createTable("G" + facebookID_);
                            /******************************************************/
                            db.insertRecords(chat);


//                            }
//                            else  // TODO: copy/paste
//                            {
//                                jsonOld = new JSONArray();

//                                JSONObject json = new JSONObject();
//                                Calendar calendar = Calendar.getInstance();
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                String formattedDate = dateFormat.format(calendar.getTime());
//
//                                json.put("userId", "" + global_.getUser_id());
//                                json.put("message", "");
//                                json.put("date", "" + formattedDate);
//                                json.put("user_name", "G");
//                                json.put("image", "" + imageURL);
//                                json.put("youtube", "");
//
//                                jsonOld.put(json);

//                                Editor editSared = chatPreferences.edit();
//
//                                editSared.putString(facebookID_, jsonOld.toString());
//                                editSared.apply();

//                                buttonSendChat_.setEnabled(true);

//                                Chat_Single chat = new Chat_Single(global_.getUser_id(), messageToSend_, formattedDate, "", imageURL, "", facebookID_, "");
//                                List<Chat_Single> chatList = new ArrayList<Chat_Single>();
//
//                                chatList.add(chat);
//
//                                parseMessageData(chatList, false, false);

//                                progressBarSendMessage_.setVisibility(View.GONE);
//                            }

                            imageBytes_ = "";
                        }
                        catch (Exception error)
                        {
                            db.dropTable("G" + facebookID_);
                            error.printStackTrace();
                        }
                    }
                }
                else  // TODO:
                {
                    buttonSendChat_.setEnabled(true);

                    progressBarSendMessage_.setVisibility(View.GONE);

                    GlobalUtills.showToast("Error..!", ChatOneToOne.this);
                }
            }
            catch (Exception error)
            {
                error.printStackTrace();

                imageBytes_ = "";

                buttonSendChat_.setEnabled(true);

                progressBarSendMessage_.setVisibility(View.GONE);

                db.dropTable("G" + facebookID_);
//                SharedPreferences chatPreferences = getSharedPreferences("Chat", MODE_PRIVATE);
//                Editor editShared = chatPreferences.edit();
//
//                editShared.remove(facebookID_);
//                editShared.apply();
            }

            super.onPostExecute(result);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // RECEIVE MSGS FIRST TIME
    public class ReceiveMsgAsyncOneToOne extends AsyncTask<String, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        private boolean localData_ = false;
        private TransparentProgressDialog progressDialog_;

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressDialog_ = new TransparentProgressDialog(ChatOneToOne.this, R.drawable.loading_spinner_icon);

            localData_ = !chatDataList_.isEmpty();
//            if (chatDataList_.size() > 0)
//            {
//                localData_ = true;
//
//
//            }
//            else
//            {
//                localData_ = false;
//            }
            progressDialog_.show();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            String jsonString = "";

            if (!localData_)
            {
                List<NameValuePair> param = new ArrayList<NameValuePair>();

                param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));

                param.add(new BasicNameValuePair("mtype", "get_single_chat"));
                param.add(new BasicNameValuePair("user_id", global_.getUser_id() + ""));
                param.add(new BasicNameValuePair("facebook_id", facebookID_ + ""));
                param.add(new BasicNameValuePair("timezone", Global.timeZone));


                WebServiceHandler webServiceHandler = new WebServiceHandler();

                jsonString = webServiceHandler.makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
            }

            return jsonString;
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
                    parseMessageData(chatDataList_, true, false);
                    if (progressDialog_.isShowing())
                        progressDialog_.dismiss();
                }
                else
                {
                    JSONObject jsonObject = new JSONObject(result);
                    String responseMessage = jsonObject.getString("Message");

                    if (responseMessage.equalsIgnoreCase("Success"))
                    {
                        messageOrNot_ = true;

                        final String messages = jsonObject.getString("msgInfo");
//						SharedPreferences chatPreferences = getSharedPreferences("Chat", MODE_PRIVATE);
//						Editor editShared = chatPreferences.edit();
//
//						editShared.putString(facebookID_, messages);
//						editShared.apply();
//
//						chatDataFromJson(messages);
//
//						parseMessageData(chatDataList_, true, false);

                        new AsyncTask<Void, Void, Void>()
                        {
                            @Override
                            protected Void doInBackground(Void... params)
                            {
                                Log.i("ReceiveMessage", "ReceiveMessage" + "    " + messages);
                                chatDataFromJson(messages);
                                parseMessageData(chatDataList_, true, false);
                                return null;
                            }

                        }.execute();


                        if (progressDialog_.isShowing())
                            progressDialog_.dismiss();
                    }
                    else
                    {
                        if (progressDialog_.isShowing())
                            progressDialog_.dismiss();

                        System.out.println(result + "error");
                    }
                }

                if (progressDialog_.isShowing())
                    progressDialog_.dismiss();
            }
            catch (Exception error)
            {
                if (progressDialog_.isShowing())
                    progressDialog_.dismiss();

                error.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    // ASYNC CLASS TO CLEAR MSGS
    public class AsyncClearMsg extends AsyncTask<String, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            imageViewErase_.bringToFront();
            imageViewErase_.setVisibility(View.VISIBLE);
            imageViewErase_.startAnimation(animationErase_);

            refreshChat_ = false;

            super.onPreExecute();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            // TODO:
            List<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));

            param.add(new BasicNameValuePair("mtype", "dlt_personal_chat"));
            param.add(new BasicNameValuePair("sender_id", myFacebookID_ + ""));
            param.add(new BasicNameValuePair("receiver_id", facebookID_));

            WebServiceHandler webServiceHandler = new WebServiceHandler();

            return webServiceHandler.makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(result);
                String message_string = jsonObject.getString("Message");

                if (message_string.equalsIgnoreCase("Success"))
                {
                    new CountDownTimer(4000, 1000)  // TODO:
                    {
                        @Override
                        public void onTick(long millisUntilFinished)
                        {
                        }

                        @Override
                        public void onFinish()
                        {
                            chatDataList_.clear();

                            db.dropTable("G" + facebookID_);

                            imageViewErase_.setVisibility(View.GONE);
                            imageViewErase_.setAnimation(null);

                            layoutChat_.removeAllViews();

                            messageOrNot_ = false;

                            GlobalUtills.showToast("Messages deleted..!", ChatOneToOne.this);
                        }
                    }.start();
                }
                else  // TODO:
                    GlobalUtills.showToast("Error while deleting messages.", ChatOneToOne.this);

                refreshChat_ = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new GetunreadSingle(facebookID_, global_.getUser_id(), responseHandlerCHat).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    new GetunreadSingle(facebookID_, global_.getUser_id(), responseHandlerCHat).execute();
            }
            catch (Exception error)
            {
                GlobalUtills.showToast("Error while deleting messages.", ChatOneToOne.this);

                imageViewErase_.setVisibility(View.GONE);
                imageViewErase_.setAnimation(null);

                error.printStackTrace();
            }

            super.onPostExecute(result);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------------------------------
    class ShowMsgDataAsyncGroup extends AsyncTask<String, Chat_Single, String>
    {
        private TransparentProgressDialog dialog_;
        List<Chat_Single> listChatData = null;


        //----------------------------------------------------------------------------------------------------------------------
        public ShowMsgDataAsyncGroup(List<Chat_Single> listChatData)
        {

            dialog_ = new TransparentProgressDialog(ChatOneToOne.this, R.drawable.loading_spinner_icon);

            dialog_.show();


            this.listChatData = listChatData;


        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
//            chatDataListGroup_ = db.getAfterChatList(CHATLIST_SIZE);
//            Collections.reverse(chatDataListGroup_);


//            showChat(values[0], g, chatDataListGroup_.size(), false, true);

            parseMessageData(listChatData, (db.getSizeOfChat() > CHATLIST_SIZE * LOAD_MORE_TIMES), true);


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
}
