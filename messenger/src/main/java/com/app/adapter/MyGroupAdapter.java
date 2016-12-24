package com.app.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.messenger.BadgeView;
import com.app.messenger.GlobalConstant;
import com.app.messenger.GroupChat;
import com.app.messenger.GroupUsers;
import com.app.messenger.GroupsFragment;
import com.app.messenger.R;
import com.app.model.ListALLGroups;
import com.app.util.GlobalUtills;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.Group_visibility;
import com.app.webserviceshandler.SetSilent;
import com.app.webserviceshandler.WebServiceHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyGroupAdapter extends BaseAdapter
{
	Context                  context;
	ArrayList<ListALLGroups> list_of_allgroup;

	String                                    UsrId;
	GlobalUtills                              globalUtills;
	com.app.util.RoundedCornersGaganImageView group_image;
	ImageButton                               img_btn_delete;
	LinearLayout                              ll_chat;
	TextView                                  Group_name, /* group_created_time */
			groupType, tvtotalMembers, tvTotalmsgs, tvTotalnew;
	RelativeLayout badge_layout;

	ImageView imgVisibilityStatus;


	public MyGroupAdapter(Context context, ArrayList<ListALLGroups> list_of_allgroup)
	{
		this.list_of_allgroup = list_of_allgroup;
		this.context = context;

		globalUtills = new GlobalUtills();

		SharedPreferences preferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);

		UsrId = preferences.getString("UserID", "");

	}

	@Override
	public int getCount()
	{

		return list_of_allgroup.size();
	}

	@Override
	public Object getItem(int position)
	{

		return null;
	}

	@Override
	public long getItemId(int position)
	{

		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent)
	{

		try
		{

			final ListALLGroups dataGroups = list_of_allgroup.get(position);


			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.group_inflator, null);
			group_image = (com.app.util.RoundedCornersGaganImageView) view.findViewById(R.id.world_group_image);
			Group_name = (TextView) view.findViewById(R.id.group_name);
			imgVisibilityStatus = (ImageView) view.findViewById(R.id.imgVisibilityStatus);//New
			img_btn_delete = (ImageButton) view.findViewById(R.id.img_btn_delete);
			ll_chat = (LinearLayout) view.findViewById(R.id.linear_layout_chat);
			tvtotalMembers = (TextView) view.findViewById(R.id.tvMembersMygroup);
			tvTotalmsgs = (TextView) view.findViewById(R.id.tvtotalmsgsMygroup);
			tvTotalnew = (TextView) view.findViewById(R.id.tvTotalnewMygroup);
			groupType = (TextView) view.findViewById(R.id.groupType);
			badge_layout = (RelativeLayout) view.findViewById(R.id.badge_indicator_group_chat);
			GlobalUtills.badgeSenderGroupChat = new BadgeView(context, badge_layout);


			if (dataGroups.getGroupIsVisible().equals("Y"))
			{
				imgVisibilityStatus.setBackgroundResource(R.drawable.eyes_groups);
			}
			else
			{
				imgVisibilityStatus.setBackgroundResource(R.drawable.eyes_groups_close);
			}


			if (position % 2 != 0)
			{
				view.setBackgroundColor(Color.parseColor("#ebebeb"));
			}
			else
			{
				view.setBackgroundColor(Color.parseColor("#ffffff"));
			}


			ll_chat.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{

					GlobalUtills.joinORadd_group = true;

					if (GlobalUtills.badgeGroup != null)
					{
						if (GlobalUtills.badgeGroup.isShown())
						{
							GlobalUtills.msgCountGroup = "";
							GlobalUtills.badgeGroup.toggle();

							SharedPreferences sharedPref = context.getSharedPreferences("login", Context.MODE_PRIVATE);
							Editor editorPref = sharedPref.edit();
							editorPref.remove("notification_flag_mychat");
							editorPref.commit();
						}

					}

					GlobalUtills.mygroups_savelocal = true;
					Intent gotoChatting = new Intent(context, GroupChat.class);
					gotoChatting.putExtra("groupID", dataGroups.getGroupId() + "");
					gotoChatting.putExtra("groupName", dataGroups.getName() + "");
					gotoChatting.putExtra("groupImage", dataGroups.getImage() + "");

					context.startActivity(gotoChatting);
					if (!(dataGroups.getUnread().equals("0")))
					{
						dataGroups.setUnread("0");

						GroupsFragment.groupAdapter().notifyDataSetChanged();
					}

				}
			});

			ll_chat.setId(position);
			ll_chat.setOnLongClickListener(new OnLongClickListener()
			{

				@Override
				public boolean onLongClick(View arg0)
				{


					try
					{
						final Dialog dialog = globalUtills.prepararDialog(context, R.layout.dialog_three_options);

						TextView title = (TextView) dialog.findViewById(R.id.txtVmainTitle);
						title.setText("Get Groupy");

						TextView subheading = (TextView) dialog.findViewById(R.id.txtVsubheading);
						subheading.setTextSize(15);
						subheading.setText("Select an option for group");

						Button      btn_Chat   = (Button) dialog.findViewById(R.id.btnChat);
						Button      btn_call   = (Button) dialog.findViewById(R.id.btncall);
						Button      btn_groups = (Button) dialog.findViewById(R.id.btngroups);
						ImageButton btn_close  = (ImageButton) dialog.findViewById(R.id.btnCloseDialog);

						btn_close.setVisibility(View.GONE);

						btn_call.setText("Silent");
						btn_Chat.setText("Leave");


//					SharedPreferences preferences = context.getSharedPreferences("Chat", Context.MODE_PRIVATE);
						final boolean visibility;

						if (dataGroups.getGroupIsVisible().equals("Y"))
						{
							btn_groups.setText("Invisible");
							visibility = true;
						}
						else
						{
							btn_groups.setText("Visible");
							visibility = false;
						}

						final String silentFlag;

						if (dataGroups.getGroupIsSilent().equals("N"))
						{

							btn_call.setText("Silent");
							silentFlag = "Y";
						}
						else
						{
							btn_call.setText("Notified");
							silentFlag = "";
						}


						btn_groups.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{

								dialog.dismiss();
								new Group_visibility(context, UsrId, dataGroups.getGroupId(), visibility).execute();

							}
						});

						btn_call.setOnClickListener(new OnClickListener()
						{

							@Override
							public void onClick(View v)
							{
								dialog.dismiss();
								new SetSilent(context, UsrId, dataGroups.getGroupId(), silentFlag).execute();
							}
						});


						btn_Chat.setOnClickListener(new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								dialog.dismiss();
								new RemoveGroup(dataGroups.getGroupId()).execute();
							}
						});


						dialog.show();
					} catch (Exception e)
					{
						e.printStackTrace();
					}

					// end dialog

					return true;
				}
			});


			Group_name.setText(dataGroups.getName());
			// group_created_time.setText(globalUtills.parseDateMYgroups(list_of_allgroup.get(position).getAddedDate()));
			// group_created_time.setSelected(true);
			if (dataGroups.getType().equals("P"))
			{
				groupType.setText("Public");
				groupType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.vault, 0);
			}
			else
			{
				groupType.setText("Private");
				groupType.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.lock, 0);
			}


			if (!(dataGroups.getUnread().equals("") || dataGroups.getUnread().equals("0")))
			{
				GlobalUtills.badgeSenderGroupChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
				GlobalUtills.badgeSenderGroupChat.setText(dataGroups.getUnread());
				GlobalUtills.badgeSenderGroupChat.show();
			}


			tvtotalMembers.setText("Members:" + dataGroups.getTotalmembers());
			tvTotalmsgs.setText("Total Msgs:" + dataGroups.getTotalmsgs());
			tvTotalnew.setText("Total New:" + dataGroups.getTotalnew());


			tvtotalMembers.setId(position);

			tvtotalMembers.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent gotoGChatting = new Intent(context, GroupUsers.class);

					gotoGChatting.putExtra("groupID", dataGroups.getGroupId());
					gotoGChatting.putExtra("groupImage", dataGroups.getImage());
					gotoGChatting.putExtra("groupName", dataGroups.getName());
					context.startActivity(gotoGChatting);
				}
			});


			group_image.setRadius(120);
			Picasso.with(context)
					.load(dataGroups.getImage())
					.placeholder(R.drawable.group_image_public)
					.error(R.drawable.group_image_public)
					.into(group_image);
//			if (!dataGroups.getImage().equalsIgnoreCase(""))
//			{
//
//				group_image.setImageUrl(context, dataGroups.getImage());
//
//			}
//			else
//			{
//				group_image.setImageResource(R.drawable.group_image_public);
//			}

		} catch (Exception e)
		{
			e.printStackTrace();
		} catch (Error e)
		{

		}

		return view;
	}

	// asyn class to remove group
	class RemoveGroup extends AsyncTask<String, String, String>
	{
		TransparentProgressDialog dialog;
		String         response_RemoveGroup;
		String messaString, group_id = "";


		public RemoveGroup(String group_id)
		{
			this.group_id = group_id;

		}

		@Override
		protected String doInBackground(String... params)
		{

			ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));

			param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "remove_group_user"));
			param.add(new BasicNameValuePair("userid", UsrId));
			param.add(new BasicNameValuePair("groupid", group_id));

			// post_type=post&mtype=remove_group_user&userid= &groupid=

			try
			{
				WebServiceHandler web = new WebServiceHandler();

				response_RemoveGroup = web.makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);

			} catch (Exception exception)
			{
				exception.printStackTrace();
			}

			return response_RemoveGroup;

		}

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			dialog = new TransparentProgressDialog(context, R.drawable.loading_spinner_icon);

			dialog.show();

		}

		protected void onPostExecute(String result)
		{

			try
			{
				JSONObject jsonObject = new JSONObject(result);
				messaString = jsonObject.getString("Message");
				if (messaString.equalsIgnoreCase("Success"))
				{


					SharedPreferences sharedChatDataS = context.getSharedPreferences("Chat", Context.MODE_PRIVATE);
					Editor editSHared = sharedChatDataS.edit();
					editSHared.remove(group_id);
					editSHared.commit();
					Intent intent = new Intent(com.app.util.GlobalConstant.BROADCAST_UPDATELIST_MYGROUPS);
					context.sendBroadcast(intent);

					GlobalUtills.showToast("Group left successfully.!", context);

					// Intent intent = new Intent(context,
					// com.app.messenger.Tab.class);
					// context.startActivity(intent);
				}
				else
				{
					GlobalUtills.showToast("Oops..!An error has been encountered..!", context);
				}

				if (dialog.isShowing())
				{
					dialog.dismiss();
				}

			} catch (JSONException e)
			{
				if (dialog.isShowing())
				{
					dialog.dismiss();
				}
				e.printStackTrace();
			}
			if (dialog.isShowing())
			{
				dialog.dismiss();
			}

			super.onPostExecute(result);
		}
	}
}