package com.semenov.web.staff;

import com.google.gson.annotations.Expose;
import com.semenov.core.data.entities.Staff;

/**
 * Extended version of Staff JSON record
 */
public class StaffExtendedJSON {
	/**
	 * Staff ID
	 */
	@Expose
	private String id;
	/**
	 * Staff first name
	 */
	@Expose
	private String firstName;
	/**
	 * Staff second name
	 */
	@Expose
	private String secondName;
	
	/**
	 * Staff office ID
	 */
	@Expose
	private String officeId;
	/**
	 * Staff regions ID
	 */
	@Expose
	private String regionId;
	/**
	 * Staff country ID
	 */
	@Expose
	private String countryId;
	
	public StaffExtendedJSON(Staff staff)
	{
		this.id = staff.getId().toString();
		this.firstName = staff.getFirstName();
		this.secondName = staff.getSecondName();
		
		this.officeId = staff.getOfficeId().getId().toString();
		this.regionId = staff.getOfficeId().getRegionId().getId().toString();
		this.countryId = staff.getOfficeId().getRegionId().getCountryId().getId().toString();
	}
}
