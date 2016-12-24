package com.app.model;

public class AroundmeGroups
{
	// "Message": "Success",
	// "groupId": "98",
	// "groupAdminId": "49",
	// "groupName": "working properly",
	// "groupImage":
	// "http://messenger.amebasoftware.com/wp-content/uploads/group/2463107.png",
	// "groupType": "PV",
	// "grouplatitude": "0.000000",
	// "grouplongitude": "0.000000",

	private String	groupId, groupName, groupImage, groupType, grouplatitude, grouplongitude, GroupPassword;

	public String getGroupPassword()
	{
		return GroupPassword;
	}

	public void setGroupPassword(String groupPassword)
	{
		GroupPassword = groupPassword;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getGroupImage()
	{
		return groupImage;
	}

	public void setGroupImage(String groupImage)
	{
		this.groupImage = groupImage;
	}

	public String getGroupType()
	{
		return groupType;
	}

	public void setGroupType(String groupType)
	{
		this.groupType = groupType;
	}

	public String getGrouplatitude()
	{
		return grouplatitude;
	}

	public void setGrouplatitude(String grouplatitude)
	{
		this.grouplatitude = grouplatitude;
	}

	public String getGrouplongitude()
	{
		return grouplongitude;
	}

	public void setGrouplongitude(String grouplongitude)
	{
		this.grouplongitude = grouplongitude;
	}

}
