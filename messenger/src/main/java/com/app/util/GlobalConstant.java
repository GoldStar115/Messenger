package com.app.util;





public class GlobalConstant {
	public static final String SUCCESS = "Success";
	public static final String FAILURE = "Failure";
	public static final String USERID = "userId";
	public static final String NAME = "uname";
	public static final String UEMAIL = "uemail";
	public static final String IS_SOCIAL = "issocial";

	
//----------------RegistrationParameter---------------------------//
	
	public static final String POST_TYPE = "post_type";
	public static final String U_TYPE = "mtype";
	public static final String MESSAGE = "Message";
	public static final String EMAIL = "uemail";
	public static final String COUNTRY = "ucountry";
	public static final String Image_url = "img";
	public static final String FirstName = "uname";
	
	public static final String PHONE_NUMBER = "ph";


//location saved locally on world group fragment
	public static String lati = "0";
	public static  String longi = "0";
	
//	time zone
	public static  String time_zone = "";



	public static final String BROADCAST_UPDATELIST_MYGROUPS = "gagan.update.mygroups";





	public static String hideNumber(String PhoneNumber)
	{

		try
		{
			String firsthalf = PhoneNumber.substring(0, 4);
			String secndHalf = "*****";
			String endHalf   = PhoneNumber.substring(11, PhoneNumber.length());

			return firsthalf + secndHalf + endHalf;
		} catch (Exception e)
		{

			e.printStackTrace();
			return PhoneNumber;
		}
	}
	
	

}
