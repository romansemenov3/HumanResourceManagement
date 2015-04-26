package com.semenov.web.gwt.country.client;

import com.google.gwt.http.client.URL;

public class CountryGWT {
	/**
	 * Country ID number
	 */
	private String id;
	/**
	 * Country name
	 */
	private String name;
	
	public CountryGWT(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public String getID()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getEditData()
	{
		return "id=" + URL.encode(this.getID()) + "&name=" + URL.encode(this.getName());
	}
	
	public String getDeleteData()
	{
		return "id=" + URL.encode(this.getID());
	}
}
