package com.app.webserviceshandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.app.util.GlobalUtills;

public class GetYouTubeUserVideosTask implements Runnable
{
	// A reference to retrieve the data when this task finishes
	public static final String	LIBRARY			= "Library";
	// A handler that will be notified when the task is finished
	private final Handler		replyTo;
	// The user we are querying on YouTube for videos
	private final String		username;

	GlobalUtills				globalUtills	= new GlobalUtills();

	boolean						mostPopular;

	/**
	 * Don't forget to call run(); to start this task
	 * 
	 * @param replyTo
	 *            - the handler you want to receive the response when this task
	 *            has finished
	 * @param username
	 *            - the username of who on YouTube you are browsing
	 */
	public GetYouTubeUserVideosTask(Handler replyTo , String username , boolean mostPopular)
	{
		this.replyTo = replyTo;
		this.username = username;
		this.mostPopular = mostPopular;
	}

	@Override
	public void run()
	{
		try
		{

			// Get a httpclient to talk to the internet
			HttpClient client = new DefaultHttpClient();
			// Perform a GET request to YouTube for a JSON list of all the
			// videos by a specific user
			String final_username = URLEncoder.encode(username, "UTF-8");
			// HttpUriRequest request = new
			// HttpGet("https://gdata.youtube.com/feeds/api/videos?q="+final_username+"&v=2&alt=jsonc");

			String URL_hit = "";

			if( mostPopular )
			{
				URL_hit = "https://www.googleapis.com/youtube/v3/search?part=snippet&chart=mostPopular&type=video&maxResults=7&key=AIzaSyAmA89rDIQhmfeaUYpzSj4nRatLz6pit7Q";

			}
			else
			{
				URL_hit = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + final_username + "&type=video&maxResults=10&key=AIzaSyAmA89rDIQhmfeaUYpzSj4nRatLz6pit7Q";
			}

			HttpUriRequest request = new HttpGet(URL_hit);

			// HttpUriRequest request2 = new
			// HttpGet("https://www.googleapis.com/youtube/v3/search?part=snippet&q="
			// + final_username +
			// "&chart=mostPopular&type=video&maxResults=15&key=AIzaSyAmA89rDIQhmfeaUYpzSj4nRatLz6pit7Q");

			// Get the response that YouTube sends back
			HttpResponse response = client.execute(request);
			// Convert this response into a readable string
			String jsonString = globalUtills.convertToString(response.getEntity().getContent());
			// Create a JSON object that we can use from the String
			JSONObject json = new JSONObject(jsonString);

			JSONArray jsonArray = json.getJSONArray("items");

			// Create a list to store are videos in
			List<Video> videos = new ArrayList<Video>();
			// Loop round our JSON list of videos creating Video objects to use
			// within our app
			for( int i = 0; i < jsonArray.length(); i++ )
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				// The title of the video

				String V_id = jsonObject.getJSONObject("id").getString("videoId");

				String title = jsonObject.getJSONObject("snippet").getString("title");

				JSONObject thumbUrlJ = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails");

				String uRL_ThumbString = thumbUrlJ.getJSONObject("default").getString("url");
				// Create the video object and add it to our list
				videos.add(new Video(title, V_id, uRL_ThumbString));
			}
			// Create a library to hold our videos
			Library lib = new Library(username, videos);
			// Pack the Library into the bundle to send back to the Activity
			Bundle data = new Bundle();
			data.putSerializable(LIBRARY, lib);

			// Send the Bundle of data (our Library) back to the handler (our
			// Activity)
			Message msg = Message.obtain();
			msg.setData(data);
			replyTo.sendMessage(msg);

			// We don't do any error catching, just nothing will happen if this
			// task falls over
			// an idea would be to reply to the handler with a different message
			// so your Activity can act accordingly
		}
		catch(ClientProtocolException e)
		{

		}
		catch(IOException e)
		{

		}
		catch(JSONException e)
		{

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
