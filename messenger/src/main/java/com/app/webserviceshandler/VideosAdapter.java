package com.app.webserviceshandler;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.messenger.YoutubeVideoPlayer;
import com.app.messenger.R;
import com.app.util.GlobalUtills;
import com.app.util.RoundedCornersGaganImageView;
import com.squareup.picasso.Picasso;


public class VideosAdapter extends BaseAdapter {
    // The list of videos to display
    List<Video> videos;
    Video video;
    Context con;
   
    // An inflator to use when creating rows
    private LayoutInflater mInflater;
     
   
    public VideosAdapter(Context context, List<Video> videos) {
        this.videos = videos;
        this.mInflater = LayoutInflater.from(context);
        con=context;
    }
 
    

	@Override
    public int getCount() {
        return videos.size();
    }
 
    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // If convertView wasn't null it means we have already set it to our list_item_user_video so no need to do it again
        if(convertView == null){
            // This is the layout we are using for each row in our list
            // anything you declare in this layout can then be referenced below
            convertView = mInflater.inflate(R.layout.veido_adapter, null);
        }
      
        RoundedCornersGaganImageView thumb = (RoundedCornersGaganImageView) convertView.findViewById(R.id.userVideoThumbImageView);
        
     
        
         
        TextView title = (TextView) convertView.findViewById(R.id.userVideoTitleTextView); 
        // Get a single video from our list
         video = videos.get(position);
      
        // Set the image for the list item
        thumb.setImageUrl(con,video.getThumbUrl());

        // Set the title for the list item
        title.setText(video.getTitle());
         
        
        
        title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  video = videos.get(position);
			       				
//				GlobalUtills.showToast(""+video.getUrl(),con );
				  
				  
				  
				GlobalUtills.YouTube_URL=""+video.getThumbUrl();
				
//				GlobalUtills globalUtills=new GlobalUtills();
				
				GlobalUtills.YouTube_VIdeoID=video.getUrl();
                GlobalUtills.youtubeTitile=video.getTitle();
				
				Intent intent = new Intent(
						con,
						YoutubeVideoPlayer.class);
				intent.putExtra("video_id",
						GlobalUtills.YouTube_VIdeoID);
				intent.putExtra("preview",
						true);
				con.startActivity(intent);
				
				
				
				
//				((Activity) con).finish();
			}
		});
        
        
        return convertView;
    }
}