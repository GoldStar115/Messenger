//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.Set;


//==============================================================================================================================
public class FriendsTabBar extends TabActivity implements OnTabChangeListener // TODO:
{

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.friend_tablayout);

        context_         = FriendsTabBar.this;
        global_          = new Global();
        actionBarCommon_ = new ActionBarCommon(context_, null);  // TODO:
        actionBarCommon_ = (ActionBarCommon) findViewById(R.id.action_bar);

        actionBarCommon_.setActionText     ("Add Friends");
        actionBarCommon_.setActionTextRight("Done");

        view1_ = (View) findViewById(R.id.viewbtncontactm);
        view2_ = (View) findViewById(R.id.viewbtngroupm);
        view3_ = (View) findViewById(R.id.viewbtnaround);

        actionBarCommon_.layoutRight().setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Set<Integer> keys = global_.getHashMapGroupmates().keySet();  // TODO:

                for (Integer key : keys)
                    global_.hashMap_ids.put(global_.hashMap_ids.size()+1, global_.hashMap_idsGroupmates.get(key));

                Set<Integer> keyAround = global_.getHashMaparoundme().keySet();

                for (Integer keyss : keyAround)
                    global_.hashMap_ids.put(global_.hashMap_ids.size()+1, global_.hashMap_idsaroundme.get(keyss));

                finish();
            }
        });

        tabHost_ = getTabHost();

        tabHost_.setOnTabChangedListener(this);

        TabHost.TabSpec spec;
        Intent          intent;

        // TAB1
        intent = new Intent().setClass(this, ShowingSocialsFreinds.class);
        spec   = tabHost_.newTabSpec("First").setContent(intent);

        spec.setIndicator("Contacts");

        tabHost_.addTab(spec);

        // TAB2
        intent = new Intent().setClass(this, UsersAroundMe.class);
        spec   = tabHost_.newTabSpec("Second").setContent(intent);

        spec.setIndicator("Around me");

        tabHost_.addTab(spec);

        // TAB3
        intent = new Intent().setClass(this, GroupMates.class);
        spec   = tabHost_.newTabSpec("Third").setContent(intent);

        spec.setIndicator("Group Mates");

        tabHost_.addTab(spec);
        tabHost_.getTabWidget().setCurrentTab(0);

        for (int index = 0; index < tabHost_.getTabWidget().getChildCount(); ++index)
        {
            tabHost_.getTabWidget().getChildAt(index).setBackgroundResource(R.color.light_grey);

            TextView tab_title = (TextView) tabHost_.getTabWidget().getChildAt(index).findViewById(android.R.id.title);

            tab_title.setPadding(2, 2, 2, 2);

            LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            if (index==0)
                tab_title.setTextColor(getResources().getColor(R.color.pinkNew));

            else
                tab_title.setTextColor(getResources().getColor(R.color.black));

            tab_title.setLayoutParams(layoutParams);
            tab_title.setGravity     (Gravity.CENTER);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onTabChanged(String tabId)
    {
        for (int index = 0; index < tabHost_.getTabWidget().getChildCount(); ++index)
        {
            TextView tabTitle = (TextView) tabHost_.getTabWidget().getChildAt(index).findViewById(android.R.id.title);

            tabTitle.setPadding(2, 2, 2, 2);

            LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            tabTitle.setTextColor   (getResources().getColor(R.color.black));
            tabTitle.setLayoutParams(layoutParams);
            tabTitle.setGravity     (Gravity.CENTER);
        }

        view1_.setVisibility(View.INVISIBLE);
        view2_.setVisibility(View.INVISIBLE);
        view3_.setVisibility(View.INVISIBLE);

        Log.i("tabs", "CurrentTab: " + tabHost_.getCurrentTab());

        if (tabHost_.getCurrentTab() == 0)
        {
            view1_.setVisibility(View.VISIBLE);
            // TODO: c/p
            TextView tabTitle = (TextView) tabHost_.getTabWidget().getChildAt(0).findViewById(android.R.id.title);

            tabTitle.setPadding(2, 2, 2, 2);  // TODO: mn

            LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            tabTitle.setTextColor   (getResources().getColor(R.color.pinkNew));
            tabTitle.setLayoutParams(layoutParams);
            tabTitle.setGravity     (Gravity.CENTER);
        }
        else if (tabHost_.getCurrentTab() == 1)
        {
            // TODO: c/p
            TextView tabTitle = (TextView) tabHost_.getTabWidget().getChildAt(1).findViewById(android.R.id.title);

            tabTitle.setPadding(2, 2, 2, 2);

            LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            tabTitle.setTextColor   (getResources().getColor(R.color.pinkNew));
            tabTitle.setLayoutParams(layoutParams);
            tabTitle.setGravity     (Gravity.CENTER);

            view2_.setVisibility(View.VISIBLE);
        }
        else
        {
            // TODO: c/p
            TextView tabTitle = (TextView) tabHost_.getTabWidget().getChildAt(2).findViewById(android.R.id.title);

            tabTitle.setPadding(2, 2, 2, 2);

            LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            tabTitle.setTextColor   (getResources().getColor(R.color.pinkNew));
            tabTitle.setLayoutParams(layoutParams);
            tabTitle.setGravity     (Gravity.CENTER);

            view3_.setVisibility(View.VISIBLE);
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private TabHost         tabHost_;
    private Context         context_;
    private ActionBarCommon actionBarCommon_;
    private Global          global_;
    private View            view1_;  // TODO:
    private View            view2_;
    private View            view3_;
}
