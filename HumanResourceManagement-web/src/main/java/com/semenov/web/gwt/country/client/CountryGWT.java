package com.semenov.web.gwt.country.client;

import com.google.gwt.http.client.URL;

/**
 * <code>Country</code> GWT representation
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class CountryGWT {
	/**
	 * Country ID number
	 */
	private String id;
	/**
	 * Country name
	 */
	private String name;
	/**
	 * Country is checked
	 */
	private boolean isChecked;
	/**
	 * Country is editing	
	 */
	private boolean isEditing;
	
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
	
	public void setChecked(boolean isChecked)
	{
		this.isChecked = isChecked;
	}
	
	public boolean isChecked()
	{
		return this.isChecked;
	}
	
	public void setEditing(boolean isEditing)
	{
		this.isEditing = isEditing;
	}
	
	public boolean isEditing()
	{
		return this.isEditing;
	}
}
