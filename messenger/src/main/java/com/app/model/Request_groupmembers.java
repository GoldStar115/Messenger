package com.app.model;

public class Request_groupmembers
{

	private String	UserID, groupID, groupName, userName, userFBid;

	public String getUserID()
	{
		return UserID;
	}

	public void setUserID(String userID)
	{
		UserID = userID;
	}

	public String getGroupID()
	{
		return groupID;
	}

	public void setGroupID(String groupID)
	{
		this.groupID = groupID;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getUserFBid()
	{
		return userFBid;
	}

	public void setUserFBid(String userFBid)
	{
		this.userFBid = userFBid;
	}

}
