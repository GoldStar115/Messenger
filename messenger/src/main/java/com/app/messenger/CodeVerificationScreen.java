//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.util.GlobalUtills;
import com.app.webserviceshandler.WebServiceHandler;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;
import java.util.List;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;  // TODO:
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


//==============================================================================================================================
public class CodeVerificationScreen extends Activity
{

    //--------------------------------------------------------------------------------------------------------------------------
    public class VerifyUser extends AsyncTask<Void, Void, String>
    {

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressDialog_ = new ProgressDialog(CodeVerificationScreen.this);

            progressDialog_.setMessage   ("Verifying...");
            progressDialog_.setCancelable(false);
            progressDialog_.show         ();

            if (GlobalUtills.verification_code.equals(""))
                GlobalUtills.verification_code = GlobalUtills.txt_verify_code.getText().toString();
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(Void... params)
        {
            // TODO: post service which check verification code is correct or not
            List<NameValuePair> param = new ArrayList<>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE,    "verify_user"));
            param.add(new BasicNameValuePair("registration_id", registrationID_));
            param.add(new BasicNameValuePair("secure_code",     GlobalUtills.txt_verify_code.getText().toString()));

            return (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            try
            {

                progressDialog_.dismiss();

                JSONObject jsonObject     = new JSONObject(result);
                String     successMessage = jsonObject.getString(GlobalConstant.MESSAGE);

                if (successMessage.equalsIgnoreCase(GlobalConstant.FAILURE))
                    GlobalUtills.showToast("Verification code entered is wrong. \n Please check and try again.",  // TODO: show toast that code is wrong
                                           CodeVerificationScreen.this);

                else if (successMessage.equalsIgnoreCase(GlobalConstant.SUCCESS))
                {
                    GlobalUtills.verification_code = "";

                    if (jsonObject.has(GlobalConstant.USER_ID))
                    {
                        GlobalUtills.user_id = jsonObject.getString(GlobalConstant.USER_ID);

                        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                        Editor            editor            = sharedPreferences.edit();

                        editor.putString("UserID", GlobalUtills.user_id + "");
                        editor.commit   ();

                        intent_ = new Intent(CodeVerificationScreen.this, LoginActivity.class);

                        intent_.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent_);
                    }
                }
            }
            catch (JSONException error)
            {
                System.out.println("Exception in the main object");

                error.printStackTrace();
            }
        }
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_code_verification_screen);

        bundle_         = getIntent().getExtras();
        registrationID_ = bundle_.getString("register_id");

        GlobalUtills.txt_verify_code = (EditText) findViewById(R.id.txt_verification_code);
        buttonComplete_              = (Button)   findViewById(R.id.btn_code_verify_phone_register);

        globalUtills_ = new GlobalUtills();

        TextView textViewResend = (TextView) findViewById(R.id.txtSendSecure);

        textViewResend.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(CodeVerificationScreen.this, R.anim.zoom_out);

        animation.setFillAfter(true);

        buttonComplete_.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent event)
            {
                switch (event.getAction())
                {
                case MotionEvent.ACTION_DOWN:
                    buttonComplete_.setAnimation(animation);
                    buttonComplete_.startAnimation(animation);

                    break;

                case MotionEvent.ACTION_UP:

                    final Animation animZoom = AnimationUtils.loadAnimation(CodeVerificationScreen.this, R.anim.zoom_out_two);

                    animZoom.setFillAfter        (true);
                    animZoom.setAnimationListener(new AnimationListener()
                    {
                        @Override
                        public void onAnimationStart(Animation animation)
                        {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation)
                        {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation)
                        {
                            if (!globalUtills_.haveNetworkConnection(CodeVerificationScreen.this))
                                GlobalUtills.showToast(GlobalConstant.ERROR_NO_NETWORK_CONNECTION, CodeVerificationScreen.this);

                            else
                            {
                                if (GlobalUtills.txt_verify_code.getText().toString().equals(""))
                                    GlobalUtills.showToast("Cannot move forward without code", CodeVerificationScreen.this);

                                else
                                    new VerifyUser().execute();
                            }
                        }
                    });

                    buttonComplete_.setAnimation  (animZoom);
                    buttonComplete_.startAnimation(animZoom);

                    break;
                }

                return false;
            }
        });
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private Button         buttonComplete_;
    private Bundle         bundle_           = new Bundle();
    private GlobalUtills   globalUtills_;
    private Intent         intent_           = null;
    private ProgressDialog progressDialog_;
    private String         registrationID_   = "";
}
