package com.app.model;

public class ListALLGroups
{
	private String groupId;
	private String userId;
	private String name;
	private String image;
	private String type;
	private String status;
	private String addedDate;
	private String totalmembers;
	private String Totalmsgs;
	private String Totalnew;
	private String unread;
	private String groupIsSilent;

	public String getGroupIsVisible()
	{
		return groupIsVisible;
	}

	public void setGroupIsVisible(String groupIsVisible)
	{
		this.groupIsVisible = groupIsVisible;
	}

	public String getGroupIsSilent()
	{
		return groupIsSilent;
	}

	public void setGroupIsSilent(String groupIsSilent)
	{
		this.groupIsSilent = groupIsSilent;
	}

	private String groupIsVisible;


	public String getUnread()
	{
		return unread;
	}

	public void setUnread(String unread)
	{
		this.unread = unread;
	}

	public String getTotalmembers()
	{
		return totalmembers;
	}

	public String getTotalmsgs()
	{
		return Totalmsgs;
	}

	public void setTotalmsgs(String totalmsgs)
	{
		Totalmsgs = totalmsgs;
	}

	public String getTotalnew()
	{
		return Totalnew;
	}

	public void setTotalnew(String totalnew)
	{
		Totalnew = totalnew;
	}

	public void setTotalmembers(String totalmembers)
	{
		this.totalmembers = totalmembers;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getAddedDate()
	{
		return addedDate;
	}

	public void setAddedDate(String addedDate)
	{
		this.addedDate = addedDate;
	}
}
