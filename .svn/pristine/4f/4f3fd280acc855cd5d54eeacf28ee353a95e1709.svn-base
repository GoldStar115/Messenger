//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.model.FriendInfo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;


//==============================================================================================================================
public class Global
{

    //--------------------------------------------------------------------------------------------------------------------------
    public static void showAdsIfNeeded(Activity activity)
    {
        if (++chatEntersCount_ < GlobalConstant.CHAT_ENTERS_PER_AD)
            return;

        chatEntersCount_ = 0;

        if (currentInterstitialAdProvider_ != InAppAds.Provider.UNDEFINED)
            InAppAds.resetCallbacks(currentInterstitialAdProvider_, null);

        currentInterstitialAdProvider_ = chooseInterstitialAdsProvider();

        InAppAds.resetCallbacks(currentInterstitialAdProvider_, new AdsCallbacks()
        {
            @Override
            public void onAdClosed()
            {
                InAppAds.resetCallbacks(currentInterstitialAdProvider_, null);
            }

            @Override
            public void onAdError()
            {
                InAppAds.resetCallbacks(currentInterstitialAdProvider_, null);

                Log.e("Ads", "Error occurred");
            }
        });

        InAppAds.showInterstitialAd(currentInterstitialAdProvider_, activity);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static InAppAds.Provider chooseAdsProvider()
    {
        int index = ThreadLocalRandom.current().nextInt(InAppAds.PROVIDER_INDEX_BEGIN, InAppAds.PROVIDER_INDEX_END);

        if (index == InAppAds.PROVIDER_INDEX_APPNEXT)
            return InAppAds.Provider.APPNEXT;

        return InAppAds.Provider.MOBILECORE;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static void setNativeAdsProvider(InAppAds.Provider provider)
    {
        currentNativeAdProvider_ = provider;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public static InAppAds.Provider nativeAdsProvider()
    {
        return  currentNativeAdProvider_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static InAppAds.Provider getOppositeProvider(InAppAds.Provider provider)
    {
        if (provider == InAppAds.Provider.APPNEXT)
            return InAppAds.Provider.MOBILECORE;

        return InAppAds.Provider.APPNEXT;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private static InAppAds.Provider chooseInterstitialAdsProvider()
    {
        InAppAds.Provider provider = chooseAdsProvider();

        if (provider != currentInterstitialAdProvider_)
            interstitialAdProviderSeriesLength_ = 1;

        else if (interstitialAdProviderSeriesLength_ < MAXIMAL_INTERSTITIAL_AD_PROVIDER_SERIES_LENGTH)
            ++interstitialAdProviderSeriesLength_;

        else
        {
            provider = getOppositeProvider(provider);
            interstitialAdProviderSeriesLength_ = 1;
        }

        return provider;
    }

    public static void initAdmob(ViewGroup container)
	{

		AdView    adView    = (AdView) container.findViewById(R.id.adView);


		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}




	public static String	fbtoken	= "";
	public static String	user_id	= "";

	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String user_id)
	{
		Global.user_id = user_id;
	}

	public String getFbtoken()
	{
		return fbtoken;
	}

	public void setFbtoken(String fbtoken)
	{
		Global.fbtoken = fbtoken;
	}

	// --------------------------FriendInfoArrayList-----------------------------------
	public ArrayList<FriendInfo>	friend_info_list	= new ArrayList<FriendInfo>();

	public ArrayList<FriendInfo> getFriend_info_list()
	{
		return friend_info_list;
	}

	public void setFriend_info_list(ArrayList<FriendInfo> friend_info_list)
	{
		this.friend_info_list = friend_info_list;
	}

	// --------------------HashMap-----------------------------------
	public static HashMap<Integer , String>	hashMap_ids	= new HashMap<Integer , String>();

	public HashMap<Integer , String> getHashMap()
	{
		return hashMap_ids;
	}

	public void setHashMap(HashMap<Integer , String> hashMap)
	{
		this.hashMap_ids = hashMap;
	}

	public void clearhashMAp()
	{
		this.hashMap_ids.clear();

	}

	// group mates
	public static HashMap<Integer , String>	hashMap_idsGroupmates	= new HashMap<Integer , String>();

	public HashMap<Integer , String> getHashMapGroupmates()
	{
		return hashMap_idsGroupmates;
	}

	public void setHashMapGroupmates(HashMap<Integer , String> hashMap)
	{
		this.hashMap_idsGroupmates = hashMap;
	}

	public void clearhashMApGroupmates()
	{
		this.hashMap_idsGroupmates.clear();

	}

	// group mates end

	// around me
	public static HashMap<Integer , String>	hashMap_idsaroundme	= new HashMap<Integer , String>();

	public HashMap<Integer , String> getHashMaparoundme()
	{
		return hashMap_idsaroundme;
	}

	public void setHashMaparoundme(HashMap<Integer , String> hashMap)
	{
		this.hashMap_idsaroundme = hashMap;
	}

	public void clearhashMAparoundme()
	{
		this.hashMap_idsaroundme.clear();

	}

	// around me end

	// --------------------HashMap phone no.-----------------------------------

	public static HashMap<Integer , String>	hashMap_ids_ph	= new HashMap<Integer , String>();

	public HashMap<Integer , String> getHashMap_ph()
	{
		return hashMap_ids_ph;
	}

	public void setHashMap_ph(HashMap<Integer , String> hashMap)
	{
		this.hashMap_ids_ph = hashMap;
	}

	public void clearhashMAp_ph()
	{
		this.hashMap_ids_ph.clear();

	}

    //--------------------------------------------------------------------------------------------------------------------------
    private final static int MAXIMAL_INTERSTITIAL_AD_PROVIDER_SERIES_LENGTH = 2;

    //--------------------------------------------------------------------------------------------------------------------------
    public static String lati     = "0";
    public static String longi    = "0";
    public static String timeZone = "";

    //--------------------------------------------------------------------------------------------------------------------------
    private static InAppAds.Provider currentInterstitialAdProvider_      = InAppAds.Provider.UNDEFINED;
    private static InAppAds.Provider currentNativeAdProvider_            = InAppAds.Provider.UNDEFINED;
    private static int               interstitialAdProviderSeriesLength_ = 0;
    private static int               chatEntersCount_                    = 0;
}
