package com.semenov.web.gwt.staff.client;

import com.google.gwt.http.client.URL;

/**
 * <code>Staff</code> GWT representation
 * 
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class StaffGWT {
	/**
	 * Staff ID number
	 */
	private String id;
	/**
	 * Staff firstName
	 */
	private String firstName;	
	/**
	 * Staff secondName
	 */
	private String secondName;
	/**
	 * Staff office ID
	 */
	private String officeId;
	
	public StaffGWT(String id, String firstName, String secondName, String officeId)
	{
		this.id = id;
		this.firstName = firstName;
		this.secondName = secondName;
		this.officeId = officeId;
	}
	
	public String getID()
	{
		return this.id;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getSecondName()
	{
		return this.secondName;
	}
	
	public void setSecondName(String secondName)
	{
		this.secondName = secondName;
	}
	
	public String getOfficeId()
	{
		return this.officeId;
	}
	
	public void setOfficeId(String officeId)
	{
		this.officeId = officeId;
	}
	
	public String getEditData()
	{
		return "id=" + URL.encode(this.getID()) + 
			   "&firstName=" + URL.encode(this.getFirstName()) +
			   "&secondName=" + URL.encode(this.getSecondName()) +
			   "&officeId=" + URL.encode(this.getOfficeId());
	}
	
	public String getDeleteData()
	{
		return "id=" + URL.encode(this.getID());
	}
}
