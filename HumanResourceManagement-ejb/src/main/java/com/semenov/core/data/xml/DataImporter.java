/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.xml;

import com.semenov.core.data.accessobjects.CRUD;
import com.semenov.core.data.accessobjects.CountryFacade;
import com.semenov.core.data.accessobjects.OfficeFacade;
import com.semenov.core.data.accessobjects.RegionFacade;
import com.semenov.core.data.accessobjects.StaffFacade;
import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Entity;
import com.semenov.core.data.entities.Office;
import com.semenov.core.data.entities.Region;
import com.semenov.core.data.entities.Staff;
import java.io.InputStream;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author Roman
 */
@Stateless (name="DataImporter")
@LocalBean
public class DataImporter {

    @EJB(beanName="countryOnline")
    protected CountryFacade countryFacade;
    
    @EJB(beanName="regionOnline")
    protected RegionFacade regionFacade;
    
    @EJB(beanName="officeOnline")
    protected OfficeFacade officeFacade;
    
    @EJB(beanName="staffOnline")
    protected StaffFacade staffFacade;
	
	/**
     * Data import mode
     */
    public enum ImportMode {
        /**
         * Adds new data without changing existing data
         */
        ADD_ONLY,
        /**
         * Adds new data and changes existing data 
         */
        REWRITE
    };
    
    private ImportMode importMode = ImportMode.ADD_ONLY;
    
    /**
     * Data import type
     */
    public enum ImportType {
        XML,
        JSON
    };
    
    public void importData(ImportType type, ImportMode mode, InputStream data) throws Exception
    {
    	this.importMode = mode;
    	switch(type)
    	{
            case XML:
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLParser xmlParser = new XMLParser(this);
                parser.parse(data, xmlParser);
                break;
            case JSON:
                break;
    	}
    }
    
    public void importCountry(Country country)
    {
    	importRecord(countryFacade, country);
    }
    
    public void importRegion(Region region)
    {
    	importRecord(regionFacade, region);
    }
    
    public void importOffice(Office office)
    {
    	importRecord(officeFacade, office);
    }
    
    public void importStaff(Staff staff)
    {
    	importRecord(staffFacade, staff);
    }
	
    private void importRecord(CRUD facade, Entity entity)
    {		
        boolean exists = (facade.find((entity).getId()) != null);
        switch(importMode)
        {
            case REWRITE:
                if(exists)
                    facade.update(entity); //no break here
            case ADD_ONLY:
                if(!exists)
                    facade.add(entity);
        }
    }
}
