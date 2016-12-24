package com.app.webserviceshandler;

import android.content.Context;
import android.os.AsyncTask;

import com.app.messenger.GlobalConstant;
import com.app.messenger.R;
import com.app.util.GlobalUtills;
import com.app.util.TransparentProgressDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gagandeep on 25/9/15.
 */
public class ReportAbuseUser extends AsyncTask<String, Void, Void> {

    //----------------------------------------------------------------------------------------------------------------------
    private String jsonString_ = "", user_id = "", block_user_id;

    private TransparentProgressDialog pd;
    Context con;

    public ReportAbuseUser(Context con,String user_id, String block_user_id) {
        this.user_id = user_id;
        this.block_user_id = block_user_id;
        this.con=con;
    }

    //----------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onPreExecute() {

        pd=new TransparentProgressDialog(con, R.drawable.loading_spinner_icon);
        pd.show();

        super.onPreExecute();
    }

    //----------------------------------------------------------------------------------------------------------------------
    @Override
    protected Void doInBackground(String... params) {
        try {
            List<NameValuePair> param = new ArrayList<>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE, "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE, "report_abuse_user"));
            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, user_id));
            param.add(new BasicNameValuePair("block_user_id", block_user_id));

            jsonString_ = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL, WebServiceHandler.GET, param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //----------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);


        try {
            pd.dismiss();

            JSONObject jsonObject = new JSONObject(jsonString_);

            String message = jsonObject.getString(GlobalConstant.MESSAGE);

            if (message.equalsIgnoreCase(GlobalConstant.SUCCESS))
            {
                    new GlobalUtills().DialogOK(con,"","Report Abused successfully.");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
