//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.util.GlobalUtills;


//==============================================================================================================================
public class ActivityInTab extends FragmentActivity
{

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed()
    {
        openLogoutAlert();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_tab);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    protected void navigateTo(Fragment fragment, int value)
    {
        FragmentManager     manager     = getSupportFragmentManager();
        FragmentTransaction translation = manager.beginTransaction();

        translation.replace(R.id.contentintab, fragment);

        // Add this transaction to the back stack, so when the user press back, it rollbacks.
        translation.addToBackStack(null);
        translation.commit        ();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void openLogoutAlert()
    {
        GlobalUtills globalUtills = new GlobalUtills();

        final Dialog dialog = globalUtills.prepararDialog(ActivityInTab.this, R.layout.dialog_three_options);

        TextView title = (TextView) dialog.findViewById(R.id.txtVmainTitle);
        title.setText("Exit..!");

        TextView subheading = (TextView) dialog.findViewById(R.id.txtVsubheading);
        subheading.setText("Are you sure,you want to exit from app..?");

        Button      buttonChat   = (Button)      dialog.findViewById(R.id.btnChat);
        Button      buttonCall   = (Button)      dialog.findViewById(R.id.btncall);
        Button      buttonGroups = (Button)      dialog.findViewById(R.id.btngroups);
        ImageButton buttonClose  = (ImageButton) dialog.findViewById(R.id.btnCloseDialog);

        buttonGroups.setVisibility(View.GONE);
        buttonClose.setVisibility(View.GONE);

        buttonCall.setText("Yes");
        buttonChat.setText("No");

        buttonChat.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        buttonCall.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
