package com.app.adapter;

import java.util.ArrayList;

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

import com.app.messenger.BadgeView;
import com.app.messenger.Facebook_ProfilePictureView_rounded;
import com.app.messenger.Global;
import com.app.messenger.R;
import com.app.model.FriendInfo;

public class Chat_contact_adapter extends BaseAdapter {


	ArrayList<FriendInfo> list_of_contacts = new ArrayList<FriendInfo>();
	TextView name, Ph_no;
	RadioButton check_button;
	RelativeLayout paddingLayout;
	RelativeLayout badge_layout;
	Facebook_ProfilePictureView_rounded freinds_image;
	ImageView icon;
	Global global;
	Context context;


	public Chat_contact_adapter(Context context,ArrayList<FriendInfo> list_of_allfriend) {
		this.context = context;
		global = new Global();
		this.list_of_contacts = list_of_allfriend;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_of_contacts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {






			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.friend_adapter, null);


			badge_layout = (RelativeLayout) convertView
					.findViewById(R.id.badge_indicator_single_chat);

			icon=(ImageView)convertView.findViewById(R.id.imgV_ph_FB);
			name = (TextView) convertView.findViewById(R.id.Friend_name);
			Ph_no = (TextView) convertView.findViewById(R.id.txtV_FBphno);
			freinds_image = (Facebook_ProfilePictureView_rounded) convertView
					.findViewById(R.id.friends_title_image);
			check_button = (RadioButton) convertView
					.findViewById(R.id.friend_check_box);

				check_button.setVisibility(View.GONE);



			paddingLayout = (RelativeLayout) convertView
					.findViewById(R.id.top_container);
			badge_layout = (RelativeLayout) convertView
					.findViewById(R.id.badge_indicator_single_chat);






			if(list_of_contacts.get(position).getId().equals(""))
			{
				freinds_image.setVisibility(View.GONE);
				name.setText(list_of_contacts.get(position).getName());
				Ph_no.setText(list_of_contacts.get(position).getMobile_no());

			}
			else
			{

					name.setText(list_of_contacts.get(position).getName());
					Ph_no.setText(list_of_contacts.get(position).getMobile_no());
					freinds_image.setProfileId(list_of_contacts.get(position).getId());

				icon.setImageResource(R.drawable.app_icon_g);

			}






			if (!(list_of_contacts.get(position).getUnread_count().equalsIgnoreCase("0"))) {

				BadgeView bdge ;
				String unread_count = list_of_contacts.get(position)
						.getUnread_count();

				 bdge = new BadgeView(context,
							badge_layout);

					bdge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
					bdge.setText(unread_count
							+ "");
					bdge.show();
			}




		if(position%2!=0)
		{
			convertView.setBackgroundColor(Color.parseColor("#ebebeb"));
		}
		else
		{
			convertView.setBackgroundColor(Color.parseColor("#ffffff"));
		}




		return convertView;

	}

}
