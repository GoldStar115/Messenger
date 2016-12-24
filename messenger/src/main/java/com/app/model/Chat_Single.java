package com.app.model;

import java.io.Serializable;

public class Chat_Single implements Serializable
{

	public String getPhNO_()
	{
		return phNO_;
	}

	private String	userId;
	private String	message;
	private String	date;
	private String	username;
	private String	imgURL;
	private String	youtube;

	private String	fbID;
	
	private String	phNO_;


	public Chat_Single(String userId , String message , String date , String username , String imgURL , String youtube , String fbID,String	phNO_)
	{

		super();

		this.userId = userId;
		this.message = message;
		this.date = date;
		this.username = username;
		this.imgURL = imgURL;
		this.youtube = youtube;
		this.fbID = fbID;
		this.phNO_=phNO_;
	}

	public String getFbID()
	{
		return fbID;
	}

	public String getUserId()
	{
		return userId;
	}

	public String getMessage()
	{
		return message;
	}

	public String getDate()
	{
		return date;
	}

	public String getUsername()
	{
		return username;
	}

	public String getImgURL()
	{
		return imgURL;
	}

	public String getYoutube()
	{
		return youtube;
	}

}
