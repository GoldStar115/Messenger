//==============================================================================================================================
package com.app.adapter;


//==============================================================================================================================
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.messenger.GlobalConstant;
import com.app.messenger.R;
import com.app.messenger.RequestActivity;
import com.app.model.Request_groupmembers;
import com.app.util.GlobalUtills;
import com.app.webserviceshandler.WebServiceHandler;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;

//------------------------------------------------------------------------------------------------------------------------------
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


//==============================================================================================================================
public class RequestAdapter  extends BaseAdapter
{

    //--------------------------------------------------------------------------------------------------------------------------
    class AproveJoinRequest extends AsyncTask<String, String, String>
    {
        //----------------------------------------------------------------------------------------------------------------------
        public AproveJoinRequest(int index)
        {
            this.index_ =index;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected String doInBackground(String... params)
        {
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            param.add(new BasicNameValuePair(GlobalConstant.POST_TYPE,    "post"));
            param.add(new BasicNameValuePair(GlobalConstant.U_TYPE,       "aprove_join_request"));
            param.add(new BasicNameValuePair("member_id",                 userId_));
            param.add(new BasicNameValuePair(GlobalConstant.GROUP_ID,     groupId_));
            param.add(new BasicNameValuePair(GlobalConstant.JOIN_USER_ID, memberId_));
            param.add(new BasicNameValuePair("status",                 status_));

            try
            {
                responseAproveJoinRequest_ = (new WebServiceHandler()).makeServiceCall(GlobalConstant.URL,
                                                                                       WebServiceHandler.GET, param);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }

            return responseAproveJoinRequest_;
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            dialog_ =ProgressDialog.show(context_, "", "Loading..");
        }

        //----------------------------------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(String result)
        {
            if (dialog_.isShowing())
                dialog_.dismiss();

            try
            {
                JSONObject jsonObj = new JSONObject(result);
                String     res     = jsonObj.getString(GlobalConstant.MESSAGE);

                if (!res.equalsIgnoreCase(GlobalConstant.SUCCESS))
                    GlobalUtills.showToast("Error", context_);

                else
                {
                    if (status_.equals("N"))
                        GlobalUtills.showToast("Request  Declined..!", context_);

                    else
                        GlobalUtills.showToast("Request Successfully Accepted..!", context_);

                    RequestActivity.requestsList().remove(index_);
                    RequestActivity.requestAdapter().notifyDataSetChanged();
                }
            }
            catch (Exception e)
            {
                if (dialog_.isShowing())
                    dialog_.dismiss();

                e.printStackTrace();
            }

            super.onPostExecute(result);
        }

        //----------------------------------------------------------------------------------------------------------------------
        private ProgressDialog dialog_;
        private String         responseAproveJoinRequest_;
        private int            index_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public   RequestAdapter(Context context, ArrayList<Request_groupmembers> allGroupList, String usrID)
    {
        this.context_      = context;
        this.allGroupList_ = allGroupList;
        this.userId_       = usrID;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public int getCount()
    {
        return allGroupList_.size();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public Object getItem(int position)
    {
        return null;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public View getView(final int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view        = inflater.inflate(R.layout.request_inflator, null);
        image_      = (com.app.messenger.Facebook_ProfilePictureView_rounded)
                                 view.findViewById(R.id.request_image);
        textName_   = (TextView) view.findViewById(R.id.request_name);
        btnAccept_  = (TextView) view.findViewById(R.id.request_accept_btn);
        btnDecline_ = (TextView) view.findViewById(R.id.request_decline_btn);

        image_.setProfileId(allGroupList_.get(position).getUserFBid());

        String name      = allGroupList_.get(position).getUserName();
        String groupName = allGroupList_.get(position).getGroupName();

        textName_.setText(name + " wants to be member of -" + groupName);

        btnAccept_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                memberId_ = allGroupList_.get(position).getUserID();
                groupId_ = allGroupList_.get(position).getGroupID();
                status_ = "Y";

                new AproveJoinRequest(position).execute();
            }
        });

        btnDecline_.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                memberId_ = allGroupList_.get(position).getUserID();
                groupId_  = allGroupList_.get(position).getGroupID();
                status_   = "N";

                new AproveJoinRequest(position).execute();
            }
        });

        view.setBackgroundColor(position % 2 != 0 ? GlobalConstant.COLOR_GREY : GlobalConstant.COLOR_WHITE);

        return  view;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private Context                                               context_;
    private ArrayList <Request_groupmembers>                      allGroupList_;
    private String                                                memberId_;
    private String                                                userId_;
    private String                                                groupId_;
    private String                                                status_;
    private com.app.messenger.Facebook_ProfilePictureView_rounded image_;
    private TextView                                              textName_;
    private TextView                                              btnAccept_;
    private TextView                                              btnDecline_;
}