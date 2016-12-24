//==============================================================================================================================
package com.app.messenger;


//==============================================================================================================================
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//------------------------------------------------------------------------------------------------------------------------------
import com.app.util.GlobalUtills;
import com.app.webserviceshandler.GetYouTubeUserVideosTask;
import com.app.webserviceshandler.Library;
import com.app.webserviceshandler.VideosListView;


//==============================================================================================================================
public class YouTubeVideosList extends Activity
{

    //--------------------------------------------------------------------------------------------------------------------------
    public static Context youtubeContext()
    {
        return youtubeContext_;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed()
    {
        GlobalUtills.YouTube_URL = "";

        GlobalUtills.YouTube_VIdeoID = "";

        super.onBackPressed();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_you_tube_videos_list);

        editSerachKey_ =  (EditText)       findViewById(R.id.edSearch_youtube);
        videosListView_ = (VideosListView) findViewById(R.id.videosListView);
        youtubeContext_ = this;

        new Thread(new GetYouTubeUserVideosTask(responseHandler, "",true)).start();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    public void getUserYouTubeFeed(View view)
    {
        String SerachKey = editSerachKey_.getText().toString().trim() + "";

        if (SerachKey.trim().equals(""))
            GlobalUtills.showToast("Please enter some search keyword..!", YouTubeVideosList.this);

        else
            new Thread(new GetYouTubeUserVideosTask(responseHandler, SerachKey,false)).start();
    }

  //--------------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onStop()
    {
        responseHandler = null;

        super.onStop();
    }

    //--------------------------------------------------------------------------------------------------------------------------
    private void populateListWithVideos(Message message)
    {
        Library library = (Library) message.getData().get(GetYouTubeUserVideosTask.LIBRARY);

        videosListView_.setVideos(library.getVideos());

        ((Button) findViewById(R.id.btn_youtube)).setEnabled(true);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    Handler responseHandler = new Handler()  // TODO: move to the constructor
    {
        public void handleMessage(Message message)
        {
            populateListWithVideos(message);
        };
    };

    //--------------------------------------------------------------------------------------------------------------------------
    private static Context youtubeContext_;

    //--------------------------------------------------------------------------------------------------------------------------
    private VideosListView videosListView_;
    private EditText       editSerachKey_;
}
