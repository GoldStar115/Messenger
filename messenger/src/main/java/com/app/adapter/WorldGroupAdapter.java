//==============================================================================================================================
package com.app.adapter;


//==============================================================================================================================
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.messenger.GlobalConstant;
import com.app.messenger.R;
import com.app.model.GroupInfo;
import com.squareup.picasso.Picasso;

//------------------------------------------------------------------------------------------------------------------------------
import java.util.ArrayList;

//==============================================================================================================================
public class WorldGroupAdapter extends BaseAdapter
{

    //--------------------------------------------------------------------------------------------------------------------------
    public WorldGroupAdapter(Context context , ArrayList<GroupInfo> allGroupList)
    {
        this.context_      = context;
        this.allGroupList_ = allGroupList;
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
        try
        {
            LayoutInflater inflater = (LayoutInflater) context_.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.worldgroup_infaltor, null);

            groupImage_     = (com.app.util.RoundedCornersGaganImageView)
                                         view.findViewById(R.id.world_group_image);
            groupName_      = (TextView) view.findViewById(R.id.group_name);
            txtGroupType_   = (TextView) view.findViewById(R.id.txt_group_type);
            tvTotalMembers_ = (TextView) view.findViewById(R.id.tvMembersMygroup);
            tvTotalMsgs_    = (TextView) view.findViewById(R.id.tvtotalmsgsMygroup);
            tvTotalNew_     = (TextView) view.findViewById(R.id.tvTotalnewMygroup);

            groupName_.setText(allGroupList_.get(position).getgroupName());
            tvTotalMembers_.setText("Members(" + allGroupList_.get(position).getgroupTotalMembers() + ")");
            tvTotalMsgs_.setText("Total Msgs:" + allGroupList_.get(position).getGroupTotalmsgs());
            tvTotalNew_.setText("Total New:" + allGroupList_.get(position).getGroupTotalnew());

            String tempGroupType = "";

            ImageView imgPrivatePublic = (ImageView)view.findViewById(R.id.img_private_public);

            if (allGroupList_.get(position).getGroupType().equalsIgnoreCase("P"))
            {
                tempGroupType = "Public";

                imgPrivatePublic.setBackgroundResource(R.drawable.vault);
            }
            else if (allGroupList_.get(position).getGroupType().equalsIgnoreCase("PV"))
            {
                tempGroupType = "Private";

                imgPrivatePublic.setBackgroundResource(R.drawable.lock);
            }

            txtGroupType_.setText(tempGroupType);
            groupImage_.setRadius  (GlobalConstant.IMAGE_CONNERS_RADIUS);
            Picasso.with(context_)
                    .load(allGroupList_.get(position).getgroupImage())
                    .placeholder(R.drawable.group_image_public)
                    .error(R.drawable.group_image_public)
                    .into(groupImage_);
//            if (allGroupList_.get(position).getgroupImage().equalsIgnoreCase(""))
//                groupImage_.setImageResource(R.drawable.group_image_public);
//
//            else
//            {
//                try
//                {
//                    groupImage_.setRadius  (GlobalConstant.IMAGE_CONNERS_RADIUS);
//                    groupImage_.setImageUrl(context_, allGroupList_.get(position).getgroupImage());
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                catch (OutOfMemoryError e)
//                {
//                    e.printStackTrace();
//                }
//            }

            view.setBackgroundColor(position % 2 != 0 ? GlobalConstant.COLOR_GREY : GlobalConstant.COLOR_WHITE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        catch (Error e)
        {
        }

        return view;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private Context                                   context_;
    private ArrayList<GroupInfo>                      allGroupList_;
    private com.app.util.RoundedCornersGaganImageView groupImage_;
    private TextView                                  groupName_;
    private TextView                                  tvTotalMembers_;
    private TextView                                  tvTotalMsgs_;
    private TextView                                  tvTotalNew_;
    private TextView                                  txtGroupType_;
}
