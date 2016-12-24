package com.app.messenger;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.app.util.GlobalUtills;
import com.app.util.RoundedCornersGaganImageView;
import com.app.webserviceshandler.GetUserGroup;


public class GroupUsers extends Activity
{

	//---------------------------------------------------------------------------------------------------------------------------
	private ActionBarCommon actionBarCommon_;
	private ListView        listViewUserInfo_;
	private String          groupID_ = "", groupImage, groupName;
	private com.app.util.RoundedCornersGaganImageView groupImage_;
	private TextView                                  editGroupName_, txtUpdate;
	private ImageButton buttonSave_;
	private String imgBytes_ = "";
	private String myFbId_   = "";
	private GlobalUtills globalUtills_;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		setContentView(R.layout.activity_group_users_info);

		globalUtills_ = new GlobalUtills();

		listViewUserInfo_ = (ListView) findViewById(R.id.listVUsersinfo);
		groupImage_ = (RoundedCornersGaganImageView) findViewById(R.id.ImgGrupPIc);
		editGroupName_ = (TextView) findViewById(R.id.edGRoupNAme);
		txtUpdate = (TextView) findViewById(R.id.txtUpdate);
		txtUpdate.setVisibility(View.INVISIBLE);
		buttonSave_ = (ImageButton) findViewById(R.id.btnEditPic);
		actionBarCommon_ = new ActionBarCommon(GroupUsers.this, null);
		actionBarCommon_ = (ActionBarCommon) findViewById(R.id.action_barUsersInfo);


		actionBarCommon_.setActionText("Group Info");
		actionBarCommon_.leftImage().setImageResource(R.drawable.icon_back_arrow);
		actionBarCommon_.layoutLeft().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});



//		getting values from intent
		Intent intentData = getIntent();
		groupID_ = intentData.getStringExtra("groupID");
		groupImage = intentData.getStringExtra("groupImage");
		groupName = intentData.getStringExtra("groupName");
//		getting values from intent end




		groupImage_.setRadius(120);
		if (!groupImage.equalsIgnoreCase(""))
		{
			groupImage_.setImageUrl(GroupUsers.this, groupImage);
		}
		else
		{
			groupImage_.setImageResource(R.drawable.group_image_public);
		}
		editGroupName_.setText(groupName);
		groupImage_.setEnabled(false);
		editGroupName_.setEnabled(false);
		buttonSave_.setVisibility(View.GONE);


		SharedPreferences sharedPref = getSharedPreferences("fbID", MODE_PRIVATE);
		myFbId_ = sharedPref.getString("fb", "");


		SharedPreferences sharedPrefUsrID = getSharedPreferences("login", Context.MODE_PRIVATE);
		final String userID = sharedPrefUsrID.getString("UserID", "");


		SharedPreferences preferences = getSharedPreferences("Chat", Context.MODE_PRIVATE);
		boolean           visibility  = preferences.getBoolean("V" + groupID_, true);

		if (visibility)
			actionBarCommon_.rightImage().setImageResource(R.drawable.eyes_groups);

		else
			actionBarCommon_.rightImage().setImageResource(R.drawable.eyes_groups_close);

		actionBarCommon_.layoutRight().setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				globalUtills_.groupVisiblty(groupID_, userID, GroupUsers.this, actionBarCommon_.rightImage());
			}
		});


		new GetUserGroup(groupID_, myFbId_, GroupUsers.this, listViewUserInfo_).execute();


	}




}
