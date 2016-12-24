//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.gaganimage.RoundedCornersFB;
import com.app.util.GlobalUtills;
import com.app.util.RoundedCornersGaganImageView;


//==============================================================================================================================
public class ActionBarCommon extends RelativeLayout
{

	Facebook_ProfilePictureView_rounded profilePic;
	//--------------------------------------------------------------------------------------------------------------------------
	private TextView  text_;
	private TextView  leftText_;
	private TextView  rightText_;
	private TextView  textGroupmembers_;
	private ImageView leftImage_,fbIcon;
	private ImageView      rightImage_;
	private RelativeLayout layoutLeft_;
	private RelativeLayout layoutRight_;
	private LinearLayout   layoutScreenCenter_;
	private Context        context_;

	//--------------------------------------------------------------------------------------------------------------------------
	public ActionBarCommon(Context context, AttributeSet attributes)
	{
		super(context, attributes);

		context_ = context;

		LayoutInflater layoutInflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.actionbar, null);

		addView(relativeLayout);

		text_ = (TextView) relativeLayout.findViewById(R.id.screen_title);
		rightText_ = (TextView) relativeLayout.findViewById(R.id.right_text);
		textGroupmembers_ = (TextView) relativeLayout.findViewById(R.id.txtVgroupmembers);
		leftText_ = (TextView) relativeLayout.findViewById(R.id.left_text);
		leftImage_ = (ImageView) relativeLayout.findViewById(R.id.left_button);
		rightImage_ = (ImageView) relativeLayout.findViewById(R.id.right_button);
		profilePic = (Facebook_ProfilePictureView_rounded) relativeLayout.findViewById(R.id.profilePic);
		fbIcon= (ImageView) relativeLayout.findViewById(R.id.fbIcon);

		layoutLeft_ = (RelativeLayout) findViewById(R.id.action_bar_layout_leftbutton);
		layoutRight_ = (RelativeLayout) findViewById(R.id.action_bar_layout_Rightbutton);
		layoutScreenCenter_ = (LinearLayout) findViewById(R.id.screen__center_layout);
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public void setActionText(String title)
	{
		text_.setText(title);
		text_.setSelected(true);
	}




//	=----------------------------------------------------------------------------------------------------------------------------

	public void setFblink(final Context con,final String fbId)
	{
		fbIcon.setVisibility(View.VISIBLE);
		fbIcon.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				con.startActivity(new GlobalUtills().getOpenFacebookIntent(con, fbId));
			}
		});

	}




	//    =================================================================================================================
	public void setProfilePic(Context con,String imgUrl)
	{
		profilePic.setVisibility(View.VISIBLE);
		profilePic.setProfileId(imgUrl);

	}

	//--------------------------------------------------------------------------------------------------------------------------
	public void setActionTextMembersNameAndShow(String title)
	{
		textGroupmembers_.setVisibility(View.VISIBLE);
		textGroupmembers_.setSelected(true);
		textGroupmembers_.setText(title);
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public void setActionTextLeft(String title)
	{
		leftText_.setText(title);
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public void setActionTextRight(String title)
	{
		rightText_.setText(title);
	}

	public void createActionTextRight()
	{
		rightText_.setBackgroundResource(R.drawable.add_newgroup);
		rightText_.setGravity(Gravity.CENTER);
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public void setLayoutLeftClickListener(OnClickListener leftClick)
	{
		layoutLeft_.setOnClickListener(leftClick);
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public void setLayoutRightClickListener(OnClickListener rightClick)
	{
		layoutRight_.setOnClickListener(rightClick);
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public void setLayoutCenterClickListener(OnClickListener centerClick)
	{
		layoutScreenCenter_.setOnClickListener(centerClick);
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public TextView text()
	{
		return text_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public TextView leftText()
	{
		return leftText_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public TextView rightText()
	{
		return rightText_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public TextView textGroupmembers()
	{
		return textGroupmembers_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public ImageView leftImage()
	{
		return leftImage_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public ImageView rightImage()
	{
		return rightImage_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public RelativeLayout layoutLeft()
	{
		return layoutLeft_;
	}

	//--------------------------------------------------------------------------------------------------------------------------
	public RelativeLayout layoutRight()
	{
		return layoutRight_;
	}


}
