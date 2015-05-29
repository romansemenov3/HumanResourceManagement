package com.semenov.web.gwt.index.client;

import com.google.gwt.core.client.EntryPoint;
import com.semenov.web.gwt.country.client.Countries;
import com.semenov.web.gwt.office.client.Offices;
import com.semenov.web.gwt.region.client.Regions;
import com.semenov.web.gwt.staff.client.Staff;

public class Index implements EntryPoint {	
	
	@Override
	public void onModuleLoad() {
		Staff staffModule = new Staff();
		Offices officesModule = new Offices(staffModule);
		Regions regionsModule = new Regions(officesModule);
		Countries countriesModule = new Countries(regionsModule);		
	}

}
