//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.app.adapter.MyGroupAdapter;
import com.app.model.ListALLGroups;
import com.app.util.GlobalUtills;
import com.app.util.NetworkCheck;
import com.app.util.TransparentProgressDialog;
import com.app.webserviceshandler.WebServiceHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------


//==============================================================================================================================
public class GroupsFragment extends Fragment
{

	//--------------------------------------------------------------------------------------------------------------------------
	private static ArrayList<ListALLGroups> allGroupsList_ = new ArrayList<>();
	private static boolean                  newGroupMsg_   = false;
	private static MyGroupAdapter  groupAdapter_;
	MyBroadcastReceiver mReceiver;
	//--------------------------------------------------------------------------------------------------------------------------
	private        ActionBarCommon actionBarCommon_;
	private        Global          global_;
	private        ListView        listViewWorldGroup_;
	private        RelativeLayout  removeText_;
	private        EditText        searchEditText_;
	private String searchText_ = "";
	private String visibility_ = "Y";
	private MyGroupsAsync getGroup_;
	//	private BadgeView     badgeEye_;
	private GlobalUtills  globalUtills_;
	private boolean mIsReceiverRegistered = false;

	//--------------------------------------------------------------------------------------------------------------------------
	public static ArrayList<ListALLGroups> allGroupsList()
	{
		return allGroupsList_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public static boolean newGroupMsg()
	{
		return newGroupMsg_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public static MyGroupAdapter groupAdapter()
	{
		return groupAdapter_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	// TODO: huge
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);

		container = (ViewGroup) inflater.inflate(R.layout.my_group_activity, container, false);
		actionBarCommon_ = new ActionBarCommon(getActivity(), null);



		Global.initAdmob(container);

		gettingValues(container);

		actionBarCommon_.setActionText("My Groups");
//		actionBarCommon_.setLayoutLeftClickListener(new OnClickListener(){ // TODO : Removed eye working
//			@Override
//			public void onClick(View v)
//			{
//				if (globalUtills_.haveNetworkConnection(getActivity()))
//				{
//					if (badgeEye_ != null)
//						badgeEye_.hide();
//
//					if (visibility_.equals("Y"))
//					{
//						visibility_ = "N";
//
//						actionBarCommon_.leftImage().setImageResource(R.drawable.eyes_groups_close);
//					}
//					else
//					{
//						visibility_ = "Y";
//
//						actionBarCommon_.leftImage().setImageResource(R.drawable.eyes_groups);
//					}
//
//					allGroupsList_.clear();
//
//					getGroup_ = new MyGroupsAsync();
//
//					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//						getGroup_.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
//
//					else
//						getGroup_.execute("");
//				}
//				else
//					GlobalUtills.showToast("No network connection..!", getActivity());
//			}
//		});

		actionBarCommon_.createActionTextRight();

		OnClickListener right_ClickListener = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				newGroupMsg_ = false;

				Intent intent = new Intent(getActivity(), AddGroupActivity.class);

				startActivity(intent);
			}
		};

		actionBarCommon_.setLayoutRightClickListener(right_ClickListener);

		removeText_.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				searchEditText_.setText("");

				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(searchEditText_.getWindowToken(), 0);

				getGroup_ = new MyGroupsAsync();

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					getGroup_.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

				else
					getGroup_.execute("");

				removeText_.setVisibility(View.INVISIBLE);
			}
		});

		searchEditText_.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				removeText_.setVisibility(View.VISIBLE);
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

		searchEditText_.setOnEditorActionListener(new OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				removeText_.setVisibility(View.VISIBLE);

				if (actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					if (!NetworkCheck.getConnectivityStatusString(getActivity()).equalsIgnoreCase("true"))
						NetworkCheck.openInternetDialog(getActivity());

					else
					{
						searchText_ = searchEditText_.getText().toString().trim();

						if (searchText_.length() > 0)
						{
							InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

							imm.hideSoftInputFromWindow(searchEditText_.getWindowToken(), 0);

							getGroup_ = new MyGroupsAsync();

							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
								getGroup_.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, searchText_);

							else
								getGroup_.execute(searchText_);
						}
						else  // TODO:
							GlobalUtills.showToast("Please enter a keyword..!", getActivity());
					}
				}


				return false;
			}
		});

		return container;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		global_ = new Global();
	}

	//--------------------------------------------------------------------------------------------------------------------------
	@Override
	public void onResume()
	{
		super.onResume();

		globalUtills_ = new GlobalUtills();
		newGroupMsg_ = true;

		searchEditText_.setText("");

		removeText_.setVisibility(View.INVISIBLE);

		GlobalUtills.joinORadd_group = true;

		SharedPreferences sharedPrefG = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

		global_.setUser_id(sharedPrefG.getString("UserID", ""));
//		new CountDownTimer(1500, 300)  // TODO : removed eye option
//		{
//			String visibilityTemp = "Y";
//
//			@Override
//			public void onTick(long millisUntilFinished)
//			{
//				if (visibilityTemp.equals("Y"))
//				{
//					visibilityTemp = "N";
//
//					actionBarCommon_.leftImage().setImageResource(R.drawable.eyes_groups_close);
//				}
//				else
//				{
//					visibilityTemp = "Y";
//
//					actionBarCommon_.leftImage().setImageResource(R.drawable.eyes_groups);
//				}
//			}
//
//			@Override
//			public void onFinish()
//			{
//				if (visibility_.equals("Y"))
//					actionBarCommon_.leftImage().setImageResource(R.drawable.eyes_groups);
//
//				else
//					actionBarCommon_.leftImage().setImageResource(R.drawable.eyes_groups_close);
//			}
//		}.start();

		if (globalUtills_.haveNetworkConnection(getActivity()))
		{
			getGroup_ = new MyGroupsAsync();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				getGroup_.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

			else
				getGroup_.execute("");
		}

		if (GlobalUtills.badgeGroup != null)
		{
			if (GlobalUtills.badgeGroup.isShown())
			{
				GlobalUtills.badgeGroup.toggle();

				SharedPreferences sharedPref = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
				Editor editorPref = sharedPref.edit();

				editorPref.remove("notification_flag_mychat");
				editorPref.commit();
			}
		}


		if (!mIsReceiverRegistered)
		{
			if (mReceiver == null)
				mReceiver = new MyBroadcastReceiver();
			getActivity().registerReceiver(mReceiver, new IntentFilter(com.app.util.GlobalConstant.BROADCAST_UPDATELIST_MYGROUPS));

			mIsReceiverRegistered = true;
		}


	}

	//--------------------------------------------------------------------------------------------------------------------------
	@Override
	public void onPause()
	{
		newGroupMsg_ = false;

		getGroup_.cancel(true);


		if (mIsReceiverRegistered)
		{
			getActivity().unregisterReceiver(mReceiver);
			mReceiver = null;
			mIsReceiverRegistered = false;
		}


		super.onPause();
	}

	//--------------------------------------------------------------------------------------------------------------------------
	@Override
	public void onDestroy()
	{
		newGroupMsg_ = false;

		super.onDestroy();
	}

//	reciver end

	//--------------------------------------------------------------------------------------------------------------------------
	private void gettingValues(ViewGroup container)
	{
		newGroupMsg_ = true;

		listViewWorldGroup_ = (ListView) container.findViewById(R.id.list_my_group);

		listViewWorldGroup_.setScrollingCacheEnabled(false);

		actionBarCommon_ = (ActionBarCommon) container.findViewById(R.id.action_bar);
		searchEditText_ = (EditText) container.findViewById(R.id.search_edit_text);
		removeText_ = (RelativeLayout) container.findViewById(R.id.layout_cencel_img);
	}

	//	reciver to update list after any change
	private class MyBroadcastReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			getGroup_ = new MyGroupsAsync();

			getGroup_.execute("");

		}
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public class MyGroupsAsync extends AsyncTask<String, Void, String>
	{

		//----------------------------------------------------------------------------------------------------------------------
		String                    message_;
		String                    emailID_;
		TransparentProgressDialog progressDialog_;
		String                    searchTag_;
		String  pointer_ = "";
		boolean running_ = true;

		//----------------------------------------------------------------------------------------------------------------------
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();

			progressDialog_ = new TransparentProgressDialog(getActivity(), R.drawable.loading_spinner_icon);

			if (allGroupsList_.isEmpty())
				progressDialog_.show();
		}

		//----------------------------------------------------------------------------------------------------------------------
		@Override
		protected String doInBackground(String... params)
		{
			if (!running_)
				return "";

			searchTag_ = params[0];

			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "ingroup"));
			param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
			param.add(new BasicNameValuePair(GlobalConstant.SEARCH_KEYWOARD, searchTag_));
			param.add(new BasicNameValuePair("timezone", com.app.util.GlobalConstant.time_zone));
			param.add(new BasicNameValuePair(GlobalConstant.GROUP_USERID, global_.getUser_id()));
			param.add(new BasicNameValuePair("visibility", ""));

			WebServiceHandler web        = new WebServiceHandler();
			String            jsonString = web.makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);

			if (jsonString.equalsIgnoreCase("Error! Try Again") || jsonString.equals("Slow"))
				return jsonString;

			else
			{
				try
				{
					JSONObject jsonObject = new JSONObject(jsonString);

					message_ = jsonObject.getString("Message");

					if (message_.equals("Success"))
					{
						pointer_ = jsonObject.getString("countvisibilitymsg");

						allGroupsList_.clear();

						JSONArray groupListInfo = jsonObject.getJSONArray("info");

						if (groupListInfo.length() > 0)
						{
							for (int i = 0; i < groupListInfo.length(); ++i)
							{
								ListALLGroups allGroups = new ListALLGroups();
								JSONObject objectInfo = groupListInfo.getJSONObject(i);

								allGroups.setTotalmembers(objectInfo.getString("totalMembers"));
								allGroups.setTotalmsgs(objectInfo.getString("groupMessageCount"));
								allGroups.setTotalnew(objectInfo.getString("groupLastMessageCount"));
								allGroups.setGroupId(objectInfo.getString("groupId"));
								allGroups.setUserId(objectInfo.getString("groupAdminId"));
								allGroups.setName(objectInfo.getString("groupName"));
								allGroups.setImage(objectInfo.getString("groupImage"));
								allGroups.setAddedDate(objectInfo.getString("groupAddedDate"));
								allGroups.setUnread(objectInfo.getString("groupUnreadMessageCount"));
								allGroups.setType(objectInfo.getString("groupType"));

								allGroups.setGroupIsVisible(objectInfo.getString("groupIsVisible"));
								allGroups.setGroupIsSilent(objectInfo.getString("groupIsSilent"));


								allGroupsList_.add(allGroups);
							}
						}
					}
					else  // TODO:
						return message_;
				} catch (JSONException e)
				{

					e.printStackTrace();
				}

				return message_;
			}
		}

		//----------------------------------------------------------------------------------------------------------------------
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);

			try
			{
				if (result.equals("Error! Try Again"))
				{
					if (progressDialog_.isShowing())
						progressDialog_.dismiss();

					Log.e("post of group", "" + result);

					GlobalUtills.showToast("No network connection..!", getActivity());  // TODO:
				}
				else if (result.equals("Slow"))
				{
					if (progressDialog_.isShowing())
						progressDialog_.dismiss();

					GlobalUtills.showToast("Error in connecting..!", getActivity());
				}
				else
				{
					if (message_.equals("Success"))
					{
						if (progressDialog_.isShowing())
							progressDialog_.dismiss();

//						if (badgeEye_ == null)
//							badgeEye_ = new BadgeView(getActivity(), actionBarCommon_.layoutLeft());

//						if (pointer_.equals("0") || pointer_.equals(""))
//							badgeEye_.hide();

//						else
//						{
//							badgeEye_.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//							badgeEye_.setText("" + pointer_);
//							badgeEye_.show();
//						}

						if (allGroupsList_.size() > 0)
						{
							if (groupAdapter_ == null)
							{
								groupAdapter_ = new MyGroupAdapter(getActivity(), allGroupsList_);

								listViewWorldGroup_.setAdapter(groupAdapter_);
							}

							else if (listViewWorldGroup_.getAdapter() != groupAdapter_)
								listViewWorldGroup_.setAdapter(groupAdapter_);

							else  // TODO:
								listViewWorldGroup_.invalidateViews();
						}
						else
						{
							if (searchTag_.equals(""))
							{
								// TODO:
							}
							else
								allGroupsList_.clear();

							Toast.makeText(getActivity(), "No New Groups Found..!", Toast.LENGTH_LONG).show();  // TODO:

							try
							{
								groupAdapter_ = new MyGroupAdapter(getActivity(), allGroupsList_);

								listViewWorldGroup_.setAdapter(groupAdapter_);
							} catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					}
					else
					{
						GlobalUtills.showToast("No Result Found..!", getActivity());

						if (progressDialog_.isShowing())
							progressDialog_.dismiss();
					}
				}
			} catch (Exception e)
			{
				if (progressDialog_.isShowing())
					progressDialog_.dismiss();

				e.printStackTrace();
			}
		}

		//----------------------------------------------------------------------------------------------------------------------
		@Override
		protected void onCancelled()
		{
			running_ = false;

			super.onCancelled();
		}

	}
}
