package com.semenov.web.gwt.region.client;

import com.google.gwt.http.client.URL;

/**
 * <code>Office</code> GWT representation
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class RegionGWT {
	/**
	 * Region ID number
	 */
	private String id;
	/**
	 * Region name
	 */
	private String name;
	
	public RegionGWT(String id, String name)
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
