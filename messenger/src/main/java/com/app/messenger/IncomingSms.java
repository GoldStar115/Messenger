// TODO: file usses \n line ends instead \r\n
//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.util.GlobalUtills;


//==============================================================================================================================
public class IncomingSms extends BroadcastReceiver
{

    //--------------------------------------------------------------------------------------------------------------------------
    public void onReceive(Context context, Intent intent)
    {
        final Bundle bundle = intent.getExtras();

        try
        {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int index = 0; index < pdusObj.length; ++index)
                {
                    SmsMessage currentMessage      = SmsMessage.createFromPdu((byte[]) pdusObj[index]);
                    String     phoneNumber         = currentMessage.getDisplayOriginatingAddress();
                    String     senderNumber        = phoneNumber;
                    String     message             = currentMessage.getDisplayMessageBody();
                    String     temporaryVerifyCode = message.substring(message.length() - 4, message.length());

                    GlobalUtills.verification_code = "";

                    try
                    {
                        if (message.contains("Your Get Groupy App"))
                        {
                            GlobalUtills.verification_code = temporaryVerifyCode;

                            GlobalUtills.txt_verify_code.setText(temporaryVerifyCode);


                        }
                    }
                    catch (Exception error)
                    {
                        error.printStackTrace();
                    }
                    catch (Error e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception error)
        {
            Log.e("SmsReceiver", "Exception smsReceiver" + error);
        }
    }
}
