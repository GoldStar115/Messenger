package com.app.util;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.messenger.R;

public class TransparentProgressDialog extends Dialog {

	private ImageView iv;

	public TransparentProgressDialog(Context context, int resourceIdOfImage) {
		super(context, R.style.TransparentProgressDialog);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		
		
			 resourceIdOfImage = R.drawable.loading_spinner_icon;
		
		 
		 
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		iv = new ImageView(context);
		iv.setImageResource(resourceIdOfImage);
		layout.addView(iv, params);
		addContentView(layout, params);
	}
	
	
	
	public TransparentProgressDialog(Context context, int resourceIdOfImage,boolean radar) {
		super(context, R.style.TransparentProgressDialog);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		
		
			 resourceIdOfImage = R.layout.radar;
			 LinearLayout layoutMsgContainer = new LinearLayout(context);
			
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			 
			 View layout = LayoutInflater.from(context).inflate(resourceIdOfImage, layoutMsgContainer);

		 
			 
		 
		
		iv = (ImageView)layout.findViewById(R.id.imgRadar);
		
		addContentView(layout, params);
	}
	
	
	
	
	
	
	
	
	
	

	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f,
				Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
				.5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(700);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
	}
}
