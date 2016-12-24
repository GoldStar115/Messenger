//==============================================================================================================================
package com.app.adapter;


//==============================================================================================================================
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.messenger.BadgeView;
import com.app.messenger.Facebook_ProfilePictureView_rounded;
import com.app.messenger.GlobalConstant;
import com.app.messenger.R;
import com.app.model.FriendInfo;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;


//==============================================================================================================================
public class ChatContactAdapter extends BaseAdapter
{

    //--------------------------------------------------------------------------------------------------------------------------
    public ChatContactAdapter(Context context, ArrayList<FriendInfo> allFriendList)
    {
        this.context_      = context;
        this.contactsList_ = allFriendList;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public int getCount()
    {
        return contactsList_.size();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public Object getItem(int position)
    {
        return null;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView   = inflater.inflate(R.layout.friend_adapter, null);
        badgeLayout_  = (RelativeLayout) convertView.findViewById(R.id.badge_indicator_single_chat);
        icon_         = (ImageView)      convertView.findViewById(R.id.imgV_ph_FB);
        name_         = (TextView)       convertView.findViewById(R.id.Friend_name);
        phoneNumber_  = (TextView)       convertView.findViewById(R.id.txtV_FBphno);
        freindsImage_ = (Facebook_ProfilePictureView_rounded)
                                         convertView.findViewById(R.id.friends_title_image);
        checkButton_  = (RadioButton)    convertView.findViewById(R.id.friend_check_box);

        checkButton_.setVisibility(View.GONE);

        badgeLayout_ = (RelativeLayout) convertView.findViewById(R.id.badge_indicator_single_chat);

        if (contactsList_.get(position).getId().equals(""))
        {
            freindsImage_.setVisibility(View.GONE);

            name_.setText(contactsList_.get(position).getName());
            phoneNumber_.setText(contactsList_.get(position).getMobile_no());
        }
        else
        {
            name_.setText(contactsList_.get(position).getName());
            phoneNumber_.setText(contactsList_.get(position).getMobile_no());

            freindsImage_.setProfileId(contactsList_.get(position).getId());

            icon_.setImageResource(R.drawable.app_icon_g);
        }

        if (!(contactsList_.get(position).getUnread_count().equalsIgnoreCase("0")))
        {
            String unreadCount = contactsList_.get(position).getUnread_count();

            BadgeView badge = new BadgeView(context_, badgeLayout_);

            badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badge.setText         (unreadCount + "");
            badge.show            ();
        }

        convertView.setBackgroundColor(position % 2 != 0 ? GlobalConstant.COLOR_GREY : GlobalConstant.COLOR_WHITE);

        return convertView;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private ArrayList<FriendInfo>               contactsList_ = new ArrayList<FriendInfo>();
    private TextView                            name_;
    private TextView                            phoneNumber_;
    private RadioButton                         checkButton_;
    private RelativeLayout                      badgeLayout_;
    private Facebook_ProfilePictureView_rounded freindsImage_;
    private ImageView                           icon_;
    private Context                             context_;
}
