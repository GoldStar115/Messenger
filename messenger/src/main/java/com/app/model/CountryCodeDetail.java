package com.app.model;

public class CountryCodeDetail {

	int country_id = 0;
	String name = "", country_code = "",short_name="";

	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public CountryCodeDetail(int country_id, String name, String country_code,String shortName) {
		// TODO Auto-generated constructor stub
		this.country_id = country_id;
		this.name = name;
		this.country_code = country_code;
		this.short_name=shortName;

	}

	public int getCountry_id() {
		return country_id;
	}

	public void setCountry_id(int country_id) {
		this.country_id = country_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry_code() {
		return country_code;
	}

	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}

}
