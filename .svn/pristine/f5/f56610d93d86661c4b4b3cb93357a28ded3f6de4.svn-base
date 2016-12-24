//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


//==============================================================================================================================
public class UponInstallActivity extends Activity
{

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upon_install);

        adsListView_ = (ListView) findViewById(R.id.ads_list_view);
        skipButton_  = (Button)   findViewById(R.id.skip_button);

        skipButton_.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(UponInstallActivity.this, PhoneNumberRegistertationScreen.class));

                finish();
            }
        });

        InAppAds.showNativeAds(Global.nativeAdsProvider(), UponInstallActivity.this, adsListView_,
                               R.layout.upon_install_inflater);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onResume()
    {
        super.onResume();

        InAppAds.resumeToNativeAds(Global.nativeAdsProvider());
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private ListView adsListView_ = null;
    private Button   skipButton_  = null;
}
