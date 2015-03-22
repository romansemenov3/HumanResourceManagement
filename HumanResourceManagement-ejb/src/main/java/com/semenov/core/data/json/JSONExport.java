/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Office;
import com.semenov.core.data.entities.Region;
import com.semenov.core.data.entities.Staff;
import com.semenov.core.utils.StringUtils;
import java.math.BigDecimal;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * The class <code>XMLExport</code> represents XML export operations
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@Stateless (name="JSONExport")
@LocalBean
public class JSONExport extends JSONBean {

    /**
     * Builds <code>JsonObject</code> representation of <code>Country</code>
     * @param country - country to convert
     * @return <code>JsonObject</code> representation of <code>Country</code>
     */
    private JsonObject countryToJsonObject(Country country)
    {
        JsonObject result = new JsonObject();
        result.addProperty("name", country.getName());
        result.addProperty("id", country.getId().toString());
        return result;
    }
    
    /**
     * Builds <code>JsonObject</code> representation of <code>Region</code>
     * @param region - region to convert
     * @return <code>JsonObject</code> representation of <code>REgion</code>
     */
    private JsonObject regionToJsonObject(Region region)
    {
        JsonObject result = new JsonObject();
        result.addProperty("name", region.getName());
        result.addProperty("id", region.getId().toString());
        return result;
    }
    
    /**
     * Builds <code>JsonObject</code> representation of <code>Office</code>
     * @param office - office to convert
     * @return <code>JsonObject</code> representation of <code>Office</code>
     */
    private JsonObject officeToJsonObject(Office office)
    {
        JsonObject result = new JsonObject();
        result.addProperty("name", office.getName());
        result.addProperty("id", office.getId().toString());
        return result;
    }
    
    /**
     * Builds <code>JsonObject</code> representation of <code>Staff</code>
     * @param staff - staff to convert
     * @return <code>JsonObject</code> representation of <code>Staff</code>
     */
    private JsonObject staffToJsonObject(Staff staff)
    {
        JsonObject result = new JsonObject();
        result.addProperty("firstName", staff.getFirstName());
        result.addProperty("secondName", staff.getSecondName());
        result.addProperty("id", staff.getId().toString());
        return result;
    }
    
    /**
     * Builds JSON content of <code>Country</code>
     * @param country - country to represent
     * @return JSON content of <code>Country</code>
     */
    private JsonObject exportCountry(Country country)
    {
        JsonObject result = new JsonObject();
        JsonArray countries = new JsonArray();
        
        JsonObject countryJson = countryToJsonObject(country);
        JsonArray regionsArray = new JsonArray();
        for(Region region : regionFacade.list(country))
        {
            JsonObject regionJson = regionToJsonObject(region);
            JsonArray officesArray = new JsonArray();
            for(Office office : officeFacade.list(region))
            {
                JsonObject officeJson = officeToJsonObject(office);
                JsonArray staffArray = new JsonArray();
                for(Staff staff : staffFacade.list(office))
                {
                    JsonObject staffJson = staffToJsonObject(staff);
                    staffArray.add(staffJson);
                }
                officeJson.add(STAFF_TAG, staffArray);
                officesArray.add(officeJson);
            }
            regionJson.add(OFFICE_TAG, officesArray);
            regionsArray.add(regionJson);            
        }
        countryJson.add(REGION_TAG, regionsArray);
        countries.add(countryJson);
        
        result.add(COUNTRY_TAG, countries);
        
        return result;
    }
    
    /**
     * Builds JSON content of <code>Region</code>
     * @param region - region to represent
     * @return JSON content of <code>Region</code>
     */
    private JsonObject exportRegion(Region region)
    {
        JsonObject result = new JsonObject();
        JsonArray countries = new JsonArray();
        
        JsonObject countryJson = countryToJsonObject(region.getCountryId());
        JsonArray regionsArray = new JsonArray();
        
        JsonObject regionJson = regionToJsonObject(region);
        JsonArray officesArray = new JsonArray();
        for(Office office : officeFacade.list(region))
        {
            JsonObject officeJson = officeToJsonObject(office);
            JsonArray staffArray = new JsonArray();
            for(Staff staff : staffFacade.list(office))
            {
                JsonObject staffJson = staffToJsonObject(staff);
                staffArray.add(staffJson);
            }
            officeJson.add(STAFF_TAG, staffArray);
            officesArray.add(officeJson);
        }
        regionJson.add(OFFICE_TAG, officesArray);
        regionsArray.add(regionJson);            

        countryJson.add(REGION_TAG, regionsArray);
        countries.add(countryJson);
        
        result.add(COUNTRY_TAG, countries);
        
        return result;
    }
    
    /**
     * Builds JSON content of <code>Office</code>
     * @param office - office to represent
     * @return JSON content of <code>Office</code>
     */
    private JsonObject exportOffice(Office office)
    {
        JsonObject result = new JsonObject();
        JsonArray countries = new JsonArray();
        
        JsonObject countryJson = countryToJsonObject(office.getRegionId().getCountryId());
        JsonArray regionsArray = new JsonArray();
        
        JsonObject regionJson = regionToJsonObject(office.getRegionId());
        JsonArray officesArray = new JsonArray();
        
        JsonObject officeJson = officeToJsonObject(office);
        JsonArray staffArray = new JsonArray();
        for(Staff staff : staffFacade.list(office))
        {
            JsonObject staffJson = staffToJsonObject(staff);
            staffArray.add(staffJson);
        }
        officeJson.add(STAFF_TAG, staffArray);
        officesArray.add(officeJson);
            
        regionJson.add(OFFICE_TAG, officesArray);
        regionsArray.add(regionJson);            

        countryJson.add(REGION_TAG, regionsArray);
        countries.add(countryJson);
        
        result.add(COUNTRY_TAG, countries);
        
        return result;
    }
    
    /**
     * Builds JSON content for export
     * @return JSON content for export
     * @throws JSONException 
     */
    public String exportJSON() throws JSONException
    {
        String result = "";
        
        String id = request.getParameter("country_id");
        if(StringUtils.isNotEmpty(id))
        {
            Country country = countryFacade.find(new BigDecimal(id));
            if(country != null)
                result = exportCountry(country).toString();
            else
                throw new JSONException("Could not build json: country not found");
        }
        else
        {
            id = request.getParameter("region_id");
            if(StringUtils.isNotEmpty(id))
            {
                Region region = regionFacade.find(new BigDecimal(id));
                if(region != null)
                    result = exportRegion(region).toString();
                else
                    throw new JSONException("Could not build json: region not found");
            }
            else
            {
                id = request.getParameter("office_id");
                if(StringUtils.isNotEmpty(id))
                {
                    Office office = officeFacade.find(new BigDecimal(id));
                    if(office != null)
                        result = exportOffice(office).toString();
                    else
                        throw new JSONException("Could not build json: office not found");
                }
                else
                {
                    throw new JSONException("Could not build json: no input parameters");
                }
            }
        }
        
        return result;
    }
}
