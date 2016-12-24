//==============================================================================================================================
package com.app.messenger;

//==============================================================================================================================

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.model.FriendInfo;
import com.app.util.GlobalUtills;
import com.google.android.gcm.GCMBaseIntentService;

//------------------------------------------------------------------------------------------------------------------------------
import org.json.JSONArray;
import org.json.JSONObject;

//==============================================================================================================================
public class GCMIntentService extends GCMBaseIntentService
{

    // --------------------------------------------------------------------------------------------------------------------------
    public GCMIntentService()
    {
        super(CommonUtilities.SENDER_ID);
    }

    // --------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onError(Context context, String errorId)
    {
        Log.i(TAG, "Received error: " + errorId);
    }

    // --------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onRegistered(Context context, String registrationId)
    {

    }

    // --------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onUnregistered(Context context, String registrationId)
    {

    }

    // --------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onMessage(final Context context, Intent intent)
    {
        try
        {
            String message = intent.getStringExtra("message");

//            System.out.println("+++++++++++++++GCM message++++++++++++++++++++++" + message);

            final JSONObject jsonNoTi = new JSONObject(message);  // TODO: what is "NoTi"-->notification JSON

            flag_ = Integer.valueOf(jsonNoTi.getString("flag"));

            String messageNoTi = "";

            GlobalUtills.IntentWHeretoGo = flag_;

            SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
            Editor editorPref = sharedPref.edit();

            if (flag_ == 0)
            {
                GlobalUtills.msgCountSingle = jsonNoTi.getString("count");
                count_ = jsonNoTi.getString("count1");

                ChatFragment.setNewMessage(true);

                JSONArray temporaryJsonArray = jsonNoTi.getJSONArray(GlobalConstant.FACE_BOOK_ID);

                GlobalUtills.single_msg_sender.clear();

                final String temporaryFacebookID = (String) temporaryJsonArray.get(0);

                GlobalUtills.single_msg_sender.add(temporaryFacebookID);

                messageNoTi = "You have " + GlobalUtills.msgCountSingle + " new messages from " +
                        jsonNoTi.getJSONArray("display_name").getString(0);

                if (ChatOneToOne.refreshChat())
                {
                    if (ChatOneToOne.facebookID().contains(GlobalUtills.single_msg_sender.get(0)))
                        ChatOneToOne.setGenerateNoTi(true);

                    else
                    {
                        if (GlobalUtills.allNotification)
                            generateNotification(context, messageNoTi);

                        editorPref.putBoolean("notification_flag_single_chat", true);
                        editorPref.putString("msgcount", GlobalUtills.msgCountSingle);
                        editorPref.commit();
                    }
                }
                else
                {
                    if (GlobalUtills.allNotification)
                        generateNotification(context, messageNoTi);

                    editorPref.putBoolean("notification_flag_single_chat", true);
                    editorPref.putString("msgcount", GlobalUtills.msgCountSingle);
                    editorPref.commit();
                }

                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            if (GlobalUtills.badge1 != null)
                            {
                                GlobalUtills.badge1.setText("" + GlobalUtills.msgCountSingle);

                                if (!GlobalUtills.badge1.isShown())
                                    GlobalUtills.badge1.toggle();
                            }

                            if (ChatFragment.newSingleMessage())
                            {
                                if (ChatFragment.groupMember().equals("C"))
                                {
                                    boolean addchat = true;

                                    for (int index = 0; index < ChatFragment.recentChatFriendList().size(); ++index)
                                    {
                                        if (GlobalUtills.single_msg_sender.get(0).toString().contains(
                                                ChatFragment.recentChatFriendList().get(index).getId()))
                                        {
                                            FriendInfo update = new FriendInfo();

                                            update.setId(ChatFragment.recentChatFriendList().get(index).getId());
                                            update.setName(ChatFragment.recentChatFriendList().get(index).getName());
                                            update.setMobileNumber(ChatFragment.recentChatFriendList().get(index)
                                                    .getMobile_no());
                                            update.setUnread_count(count_ + "");

                                            ChatFragment.recentChatFriendList().remove(index);
                                            ChatFragment.recentChatFriendList().add(0, update);

                                            addchat = false;

                                            break;
                                        }
                                    }

                                    if (addchat)
                                    {
                                        FriendInfo updateNew = new FriendInfo();

                                        updateNew.setId(GlobalUtills.single_msg_sender.get(0));
                                        updateNew.setName(jsonNoTi.getJSONArray("display_name").getString(0));
                                        updateNew.setMobileNumber(jsonNoTi.getJSONArray("user_telephone").getString(0));
                                        updateNew.setUnread_count(count_ + "");

                                        ChatFragment.recentChatFriendList().add(0, updateNew);
                                    }

                                    ChatFragment.recentChatAdapter().notifyDataSetChanged();
                                }

                                else if (ChatFragment.groupMember().equals("PH"))
                                {
                                    for (int index = 0; index < ChatFragment.completeFriendsList().size(); ++index)
                                    {
                                        if (!ChatFragment.completeFriendsList().get(index).getId().equals(""))
                                        {
                                            if (GlobalUtills.single_msg_sender.get(0).toString().contains(
                                                    ChatFragment.completeFriendsList().get(index).getId()))
                                            {
                                                FriendInfo update = new FriendInfo();

                                                update.setId(ChatFragment.completeFriendsList().get(index).getId());
                                                update.setName(ChatFragment.completeFriendsList().get(index).getName());
                                                update.setMobileNumber(ChatFragment.completeFriendsList().get(index)
                                                        .getMobile_no());
                                                update.setUnread_count(count_ + "");

                                                ChatFragment.completeFriendsList().remove(index);
                                                ChatFragment.completeFriendsList().add(0, update);

                                                break;
                                            }
                                        }
                                    }

                                    ChatFragment.phoneContactAdapter().notifyDataSetChanged();
                                }
                                else if (ChatFragment.groupMember().equals("FB_M"))
                                {
                                    for (int index = 0; index < ChatFragment.usersList().size(); ++index)
                                    {
                                        if (GlobalUtills.single_msg_sender.get(0).toString().contains(
                                                ChatFragment.usersList().get(index).getId()))
                                        {
                                            FriendInfo update = new FriendInfo();

                                            update.setId(ChatFragment.usersList().get(index).getId());
                                            update.setName(ChatFragment.usersList().get(index).getName());
                                            update.setMobileNumber(ChatFragment.usersList().get(index).getMobile_no());
                                            update.setUnread_count(count_ + "");

                                            ChatFragment.usersList().remove(index);
                                            ChatFragment.usersList().add(0, update);

                                            break;
                                        }
                                    }

                                    ChatFragment.socialUsers().notifyDataSetChanged();
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        catch (Error e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if (flag_ == 1)
            {
                GlobalUtills.msgCountGroup = "";
                GlobalUtills.msgCountGroup = jsonNoTi.getString("count");

                final String messagesCount = jsonNoTi.getString("tcount");

                GlobalUtills.group_msg_sender.clear();

                final String temporaryGroupID = jsonNoTi.getString(GlobalConstant.GROUP_ID);

                GlobalUtills.group_msg_sender.add(temporaryGroupID);

                messageNoTi = "You have " + GlobalUtills.msgCountGroup + " new message(s) in your groups.";

                if (GroupChat.refreshChat())
                {
                    if (GroupChat.groupID().contains(temporaryGroupID))
                        ChatOneToOne.setGenerateNoTi(true);

                    else
                    {
                        if (GlobalUtills.allNotification)
                            generateNotification(context, messageNoTi);

                        editorPref.putBoolean("notification_flag_mychat", true);
                        editorPref.putString("msgcountG", GlobalUtills.msgCountGroup);
                        editorPref.commit();
                    }
                }
                else
                {
                    if (GlobalUtills.allNotification)
                        generateNotification(context, messageNoTi);

                    editorPref.putBoolean("notification_flag_mychat", true);
                    editorPref.putString("msgcountG", GlobalUtills.msgCountGroup);
                    editorPref.commit();
                }

                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            if (GlobalUtills.badgeGroup != null)
                            {
                                GlobalUtills.badgeGroup.setText("" + GlobalUtills.msgCountGroup);

                                if (!GlobalUtills.badgeGroup.isShown())
                                    GlobalUtills.badgeGroup.toggle();
                            }

                            if (GroupsFragment.newGroupMsg())
                            {

                                Intent intent = new Intent(com.app.util.GlobalConstant.BROADCAST_UPDATELIST_MYGROUPS);
                                context.sendBroadcast(intent);
//                                for (int index = 0; index < GroupsFragment.allGroupsList().size(); ++index)
//                                {
//                                    if (temporaryGroupID.contains(GroupsFragment.allGroupsList().get(index).getGroupId()))
//                                    {
//                                        ListALLGroups allGroups = new ListALLGroups();
//
//                                        allGroups.setTotalmembers(GroupsFragment.allGroupsList().get(index).getTotalmembers());
//                                        allGroups.setTotalmsgs   (GroupsFragment.allGroupsList().get(index).getTotalmsgs());
//                                        allGroups.setTotalnew    (GroupsFragment.allGroupsList().get(index).getTotalnew());
//                                        allGroups.setGroupId     (GroupsFragment.allGroupsList().get(index).getGroupId());
//                                        allGroups.setUserId      (GroupsFragment.allGroupsList().get(index).getUserId());
//                                        allGroups.setName        (GroupsFragment.allGroupsList().get(index).getName());
//                                        allGroups.setImage       (GroupsFragment.allGroupsList().get(index).getImage());
//                                        allGroups.setAddedDate   (GroupsFragment.allGroupsList().get(index).getAddedDate());
//                                        allGroups.setType        (GroupsFragment.allGroupsList().get(index).getType());
//                                        allGroups.setUnread      (messagesCount);
//
//                                        GroupsFragment.allGroupsList().remove(index);
//                                        GroupsFragment.allGroupsList().add   (0, allGroups);
//
//                                        break;
//                                    }
//                                }
//
//                                GroupsFragment.groupAdapter().notifyDataSetChanged();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        catch (Error e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if (flag_ == 2)  // TODO: mn
            {
                messageNoTi = jsonNoTi.getString("msg");

                editorPref.putBoolean("notification_flag_adgroup", true);
                editorPref.commit();
            }
            else if (flag_ == 3)
            {
                try
                {
                    GlobalUtills.msgCountSingle = "";
                    messageNoTi = jsonNoTi.getString("msg");  // TODO: "NoTi"?

                    String facebookID = jsonNoTi.getString(GlobalConstant.FACE_BOOK_ID);

                    SharedPreferences sharedChatDataS = context.getSharedPreferences("Chat", Context.MODE_PRIVATE);
                    Editor editSHared = sharedChatDataS.edit();

                    editSHared.remove(facebookID);
                    editSHared.commit();

                    if (GlobalUtills.badge1 != null && GlobalUtills.badge1.isShown())
                        GlobalUtills.badge1.toggle();

                    if (ChatOneToOne.refreshChat())
                    {
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable()
                        {
                            public void run()
                            {
                                ChatOneToOne c = new ChatOneToOne();

                                c.clearChat(context);
                            }
                        });
                    }
                }
                catch (Exception e)
                {
                    //e.printStackTrace();
                    Log.e("Exception ex",""+e.toString());
                }
            }
            else if (flag_ == 4)  // TODO:
            {
                messageNoTi = jsonNoTi.getString("msg");

                editorPref.putBoolean("notification_flag_add_menber", true);
                editorPref.commit();
            }
//            else if (flag_ == 5)
//            {
            // TODO case which was now removed :
//            }
            else if (flag_ == 6)
            {
                messageNoTi = jsonNoTi.getString("msg");

                editorPref.putBoolean("notification_flag_approve_request", true);
                editorPref.commit();
            }
            else if (flag_ == 7)
            {
                messageNoTi = jsonNoTi.getString("msg");

                editorPref.putBoolean("notification_flag_join_gorup", true);
                editorPref.commit();
            }
            else if (flag_ == 8)
            {
                messageNoTi = jsonNoTi.getString("msg");

                editorPref.putBoolean("notification_flag_approve_join_gorup", true);
                editorPref.commit();
            }

            if (GlobalUtills.allNotification)
            {
                if (flag_ == 3)  // TODO Delete chat notification:
                {
                    GlobalUtills.delete_chat_notification = false;

                    generateNotification(context, messageNoTi);
                }

                if (flag_ == 2 || flag_ == 4 || flag_ == 5 || flag_ == 6 || flag_ == 7 || flag_ == 8)
                    generateNotification(context, messageNoTi);
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, "Inside Exception onMessage -> " + e.toString());
        }
        catch (Error e)
        {
           // e.printStackTrace();
            Log.e("Exception ex",""+e.toString());
        }
        catch (Throwable e)
        {
          //  e.printStackTrace();
            Log.e("Exception ex",""+e.toString());

        }
    }

    // --------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onDeletedMessages(Context context, int total)
    {
        Log.i(TAG, "Received deleted messages notification");
    }

    // --------------------------------------------------------------------------------------------------------------------------
    @Override
    protected boolean onRecoverableError(Context context, String errorId)
    {
        Log.i(TAG, "Received recoverable error: " + errorId);

        return super.onRecoverableError(context, errorId);
    }

//    --------------------------------------------------------

    private static void checkAppIcon(final Context context)
    {
        try
        {


            if (!GlobalUtills.isApplicationRunningBackground(context))
            {
                final SharedPreferences sharedPref = context.getSharedPreferences("login", MODE_PRIVATE);

                if (!sharedPref.contains("icon"))
                {

                    new Handler().post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Editor ed = sharedPref.edit();

                            ed.putInt("icon", flag_);
                            ed.apply();
                            AppIconManager.setBadgeValue(context, Integer.parseInt(GlobalUtills.msgCountSingle));
//                            AppIconManager.showAppropriateIcon(context, Integer.parseInt(GlobalUtills.msgCountSingle));
                        }
                    });

                }
            }
        }
        catch (Exception | Error e)
        {
           // e.printStackTrace();
            Log.e("Exception is",e.toString());
        }
    }


    // --------------------------------------------------------------------------------------------------------------------------
    private static void generateNotification(Context context, String message)
    {


        checkAppIcon(context);


        try
        {
            int icon = R.drawable.app_icon_g;

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String title = "Get Groupy";

            Intent notificationIntent;
            PendingIntent intent;

            if (GlobalUtills.isApplicationRunningBackground(context))
            {
                notificationIntent = new Intent(context, BlankActivity.class);
            }
            else
            {
                notificationIntent = new Intent(context, SplashBro.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (defaultSound == null)
            {
                defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

                if (defaultSound == null)
                    defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }

            Builder builder = new Notification.Builder(context).setContentTitle(title).setContentText(message)
                    .setContentIntent(intent).setSmallIcon(icon)
                    .setLights(Color.GREEN, 1, 2).setAutoCancel(true)
                    .setSound(defaultSound);
            Notification not = new Notification.BigTextStyle(builder).bigText(message).build();

            not.defaults |= Notification.DEFAULT_VIBRATE;
            not.defaults |= Notification.DEFAULT_SOUND;

            notificationManager.notify(flag_, not);


        }
        catch (Exception e)
        {
            //e.printStackTrace();
            Log.e("Exception is",e.toString());
        }
    }

    // --------------------------------------------------------------------------------------------------------------------------
    private static final String TAG = "GCMIntentService";

    // --------------------------------------------------------------------------------------------------------------------------
    private static int flag_ = 0;

    // --------------------------------------------------------------------------------------------------------------------------
    private String count_ = ""; // TODO: why String?
}
