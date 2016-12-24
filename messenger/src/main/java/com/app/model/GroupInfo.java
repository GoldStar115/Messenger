package com.app.model;

public class GroupInfo
{
	private String	groupId, groupName, groupImage, groupTotalMembers, groupType, groupPassword, groupTotalmsgs, groupTotalnew, userStatus;

	public String getUserStatus()
	{
		return userStatus;
	}

	public void setUserStatus(String userStatus)
	{
		this.userStatus = userStatus;
	}

	public String getGroupPassword()
	{
		return groupPassword;
	}

	public void setGroupPassword(String groupPassword)
	{
		this.groupPassword = groupPassword;
	}

	public String getGroupType()
	{
		return groupType;
	}

	public String getGroupTotalmsgs()
	{
		return groupTotalmsgs;
	}

	public void setGroupTotalmsgs(String groupTotalmsgs)
	{
		this.groupTotalmsgs = groupTotalmsgs;
	}

	public String getGroupTotalnew()
	{
		return groupTotalnew;
	}

	public void setGroupTotalnew(String groupTotalnew)
	{
		this.groupTotalnew = groupTotalnew;
	}

	public void setGroupType(String groupType)
	{
		this.groupType = groupType;
	}

	public String getgroupId()
	{
		return groupId;
	}

	public void setgroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public String getgroupName()
	{
		return groupName;
	}

	public void setgroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public String getgroupImage()
	{
		return groupImage;
	}

	public void setgroupImage(String groupImage)
	{
		this.groupImage = groupImage;
	}

	public String getgroupTotalMembers()
	{
		return groupTotalMembers;
	}

	public void setgroupTotalMembers(String groupTotalMembers)
	{
		this.groupTotalMembers = groupTotalMembers;
	}

}
