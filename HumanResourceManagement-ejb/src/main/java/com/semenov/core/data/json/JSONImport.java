/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.json;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Office;
import com.semenov.core.data.entities.Region;
import com.semenov.core.data.entities.Staff;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * The class <code>JSONImport</code> represents JSON import operations
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@Stateless (name="JSONImport")
@LocalBean
public class JSONImport extends JSONBean {
    private Object Charsets;
    
    /**
     * Gets uploaded data file
     * @return parsed object
     * @throws XMLException
     */
    private DataImportTemplate loadFile() throws JSONException
    {
        try {
            Gson g = new Gson();
            return g.fromJson(new JsonReader(new InputStreamReader(request.getPart(DATA_FILE).getInputStream())), DataImportTemplate.class);
            
        } catch (Exception e) {
            throw new JSONException("Could not parse file: " + e);
        }
    }
    
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
    
    /**
     * Gets data import mode
     * @return data import mode
     * @throws JSONException 
     */
    private ImportMode getImportMode() throws JSONException
    {
        try {
            switch(request.getParameter(DATA_IMPORT_MODE))
            {
                case "ADD_ONLY": return ImportMode.ADD_ONLY;
                case "REWRITE": return ImportMode.REWRITE;
                default: throw new JSONException("Unknown import mode");
            }
        } catch (Exception e) {
            throw new JSONException("Could not parse file: " + e);
        }
    }
    
    /**
     * Imports JSON data
     * @throws JSONException 
     */
    public void importJSON() throws JSONException
    {
        DataImportTemplate data = loadFile();
        ImportMode importMode = getImportMode();
        
        try
        {        
            for(DataImportTemplate.CountryTemplate country : data.human_resource_management_data)
            {
                importCountry(country, importMode);
            }
        }catch(Exception e)
        {
            throw new JSONException("Invalid or empty data file: " + e.getMessage());
        }
    }
    
    /**
     * Imports countries
     * @param XMLcountries - countries elements
     * @param importMode - import mode
     * @throws XMLException 
     */
    private void importCountry(DataImportTemplate.CountryTemplate country, ImportMode importMode) throws JSONException
    {        
        if(country != null)
        {
            Country c = null;
            try
            {
                c = new Country(new BigDecimal(country.id), country.name);
            }
            catch(Exception e)
            {
                throw new JSONException("Could not parse COUNTRY: " + e.getMessage()); 
            }

            boolean exists = countryFacade.find(c.getId()) != null;

            switch(importMode)
            {
                case REWRITE:
                    if(exists)
                        countryFacade.update(c); //no break here
                case ADD_ONLY:
                    if(!exists)
                        countryFacade.add(c);
            }

            for(DataImportTemplate.CountryTemplate.RegionTemplate region : country.regions)
            {
                importRegion(c, region, importMode);
            }
        }
    }
    
    /**
     * Imports regions
     * @param countryId - parent country
     * @param XMLregions - regions data
     * @param importMode - import mode
     * @throws XMLException 
     */
    private void importRegion(Country countryId, DataImportTemplate.CountryTemplate.RegionTemplate region, ImportMode importMode) throws JSONException
    {
        if(region != null)
        {
            Region r = null;
            try
            {
                r = new Region(new BigDecimal(region.id), region.name, countryId);
            }
            catch(Exception e)
            {
                throw new JSONException("Could not parse REGION: " + e.getMessage()); 
            }

            boolean exists = regionFacade.find(r.getId()) != null;

            switch(importMode)
            {
                case REWRITE:
                    if(exists)
                        regionFacade.update(r); //no break here
                case ADD_ONLY:
                    if(!exists)
                        regionFacade.add(r);
            }

            for(DataImportTemplate.CountryTemplate.RegionTemplate.OfficeTemplate office : region.offices)
            {
                importOffice(r, office, importMode);
            }
        }
    }
    
    /**
     * Imports offices
     * @param regionId - parent region
     * @param XMLoffices - offices data
     * @param importMode - import mode
     * @throws XMLException 
     */
    private void importOffice(Region regionId, DataImportTemplate.CountryTemplate.RegionTemplate.OfficeTemplate office, ImportMode importMode) throws JSONException
    {
        if(office != null)
        {
            Office o = null;
            try
            {
                o = new Office(new BigDecimal(office.id), office.name, regionId);
            }
            catch(Exception e)
            {
                throw new JSONException("Could not parse OFFICE: " + e.getMessage()); 
            }

            boolean exists = officeFacade.find(o.getId()) != null;

            switch(importMode)
            {
                case REWRITE:
                    if(exists)
                        officeFacade.update(o); //no break here
                case ADD_ONLY:
                    if(!exists)
                        officeFacade.add(o);
            }

            for(DataImportTemplate.CountryTemplate.RegionTemplate.OfficeTemplate.StaffTemplate staff : office.staff)
            {
                importStaff(o, staff, importMode);
            }
        }
    }
    
    /**
     * Imports staff
     * @param officeId - parent office
     * @param XMLstaff - staff data
     * @param importMode - import mode
     * @throws XMLException 
     */
    private void importStaff(Office officeId, DataImportTemplate.CountryTemplate.RegionTemplate.OfficeTemplate.StaffTemplate staff, ImportMode importMode) throws JSONException
    {
        if(staff != null)
        {
            Staff s = null;
            try
            {
                s = new Staff(new BigDecimal(staff.id), staff.firstName, staff.secondName, officeId);
            }
            catch(Exception e)
            {
                throw new JSONException("Could not parse STAFF: " + e.getMessage()); 
            }

            boolean exists = staffFacade.find(s.getId()) != null;

            switch(importMode)
            {
                case REWRITE:
                    if(exists)
                        staffFacade.update(s); //no break here
                case ADD_ONLY:
                    if(!exists)
                        staffFacade.add(s);
            }
        }
    }
}
