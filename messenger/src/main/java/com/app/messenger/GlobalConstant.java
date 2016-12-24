//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.graphics.Color;


//==============================================================================================================================
public class GlobalConstant
{
    //--------------------------------------------------------------------------------------------------------------------------
    public static final String YOUTUBE_APIKEY="AIzaSyCd5MQ2Xqmy2xWsFqs-Uvj5ZPcjxCiRf44";

//    public static final String URL = "http://messenger.amebasoftware.com/webservice/get_posts/";
//    public static final String URLF = "http://messenger.amebasoftware.com/webservice/get_posts/?post_type=post&mtype=register";
//    public static final String URLSendMSG = "http://messenger.amebasoftware.com/webservice/get_posts/?post_type=post&mtype=save_single_chat";
//    public static final String URLSendMSGGroup = "http://messenger.amebasoftware.com/webservice/get_posts/?post_type=post&mtype=mychat";

//    http://groupylive.get-groupy.com/(NEW)

    public static final String URL = "http://groupylive.get-groupy.com/webservice/get_posts/";
    public static final String URLF = "http://groupylive.get-groupy.com/webservice/get_posts/?post_type=post&mtype=register";
    public static final String URLSendMSG = "http://groupylive.get-groupy.com/webservice/get_posts/?post_type=post&mtype=save_single_chat";
    public static final String URLSendMSGGroup = "http://groupylive.get-groupy.com/webservice/get_posts/?post_type=post&mtype=mychat";

//    public static final String URL = "http://groupylive.amebasoftware.com/webservice/get_posts/";
//    public static final String URLF = "http://groupylive.amebasoftware.com/webservice/get_posts/?post_type=post&mtype=register";
//    public static final String URLSendMSG = "http://groupylive.amebasoftware.com/webservice/get_posts/?post_type=post&mtype=save_single_chat";
//    public static final String URLSendMSGGroup = "http://groupylive.amebasoftware.com/webservice/get_posts/?post_type=post&mtype=mychat";

//    public static final String URL = "http://ameba.get-groupy.com/webservice/get_posts/";
//    public static final String URLF = "http://ameba.get-groupy.com/webservice/get_posts/?post_type=post&mtype=register";
//    public static final String URLSendMSG = "http://ameba.get-groupy.com/webservice/get_posts/?post_type=post&mtype=save_single_chat";
//    public static final String URLSendMSGGroup = "http://ameba.get-groupy.com/webservice/get_posts/?post_type=post&mtype=mychat";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String SUCCESS   = "Success";
    public static final String FAILURE   = "Failure";
    public static final String USER_ID   = "userId";
    public static final String IS_SOCIAL = "issocial";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String POST_TYPE    = "post_type";
    public static final String U_TYPE       = "mtype";
    public static final String MESSAGE      = "Message";
    public static final String EMAIL        = "uemail";
    public static final String COUNTRY      = "ucountry";
    public static final String IMAGE_URL    = "img";
    public static final String FIRST_NAME   = "uname";
    public static final String FACE_BOOK_ID = "facebook_id";
    public static final String PHONE_NUMBER = "ph";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String GROUP_NAME      = "gname";
    public static final String GROUP_IMAGE     = "gimage";
    public static final String GROUP_TYPE      = "gtype";
    public static final String GROUP_USERS_ID  = "users";
    public static final String SEARCH_KEYWOARD = "keyword";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String GROUP_USERID = "uid";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String LATITUDE         = "latitude";
    public static final String LONGITUDE        = "longitude";
    public static final String UPDATION_USER_ID = "user_id";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String GROUP_ID     = "group_id";
    public static final String JOIN_USER_ID = "user_id";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final int COLOR_WHITE     = Color.parseColor("#ffffff");
    public static final int COLOR_GREY      = Color.parseColor("#ebebeb");
    public static final int COLOR_RED       = Color.parseColor("#fc6278");
    public static final int COLOR_BLACK     = Color.parseColor("#000000");
    public static final int COLOR_YELLOW    = Color.parseColor("#f4d334");
    public static final int COLOR_DARK_GREY = Color.parseColor("#3c3c3c");

    //--------------------------------------------------------------------------------------------------------------------------
    public static final int IMAGE_CONNERS_RADIUS = 160;

    //--------------------------------------------------------------------------------------------------------------------------
    public static final int REQUEST_CODE_22   =   22;  // give an appropriate name;
    public static final int REQUEST_CODE_1888 = 1888;  // give an appropriate name;

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String NOTIFICATION_ENTER_GROUP_NAME        = "Please enter the Group Name";
    public static final String NOTIFICATION_ENTER_SEARCH_KEYWORD    = "Please enter the search keyword";
    public static final String NOTIFICATION_SELECT_GROUP_TYPE       = "Please Select the Group Type";
    public static final String NOTIFICATION_ADD_AT_LEAST_ONE_FRIEND = "Please add at least one friend from facebook";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String HINT_ENTER_PASSWORD = "Please enter a Password";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String ERROR_GROUP_NOT_CREATED      = "Oops.., Group not created!";
    public static final String ERROR_NO_NETWORK_CONNECTION  = "No network connection!";
    public static final String ERROR_NO_NEAR_BY_GROUP_FOUND = "No groups found";
    public static final String ERROR_NO_NEAR_BY_USERS_FOUND = " No users found";

    public static final String ERROR_CODE_STRING            = "Error! Try Again";  // TODO: bad err code (String)

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String SUCCESS_GROUP_CREATED = "Group Created Successfully";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String REQUEST_PARAM_UP_GROUP = "?post_type=post&mtype=up_group";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final String TEXT_SELECTED_FB_FRIENDS = "Selected fb friends";
    public static final String TEXT_SELECTED_PHONES     = "Selected phone contacts";

    //--------------------------------------------------------------------------------------------------------------------------
    public static final int CHAT_ENTERS_PER_AD = 5 ;
}
