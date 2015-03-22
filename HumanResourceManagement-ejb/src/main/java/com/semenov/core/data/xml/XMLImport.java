/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.xml;

import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Office;
import com.semenov.core.data.entities.Region;
import com.semenov.core.data.entities.Staff;
import java.math.BigDecimal;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The class <code>XMLImport</code> represents XML import operations
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@Stateless (name="XMLImport")
@LocalBean
public class XMLImport extends XMLBean {

    /**
     * Gets uploaded data file
     * @return parsed object
     * @throws XMLException
     */
    private Document loadFile() throws XMLException
    {        
        try {
            return documentBuilder.parse(request.getPart(DATA_FILE).getInputStream());            
        } catch (Exception e) {
            throw new XMLException("Could not parse file: " + e);
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
     * @throws XMLException 
     */
    private ImportMode getImportMode() throws XMLException
    {
        try {
            switch(request.getParameter(DATA_IMPORT_MODE))
            {
                case "ADD_ONLY": return ImportMode.ADD_ONLY;
                case "REWRITE": return ImportMode.REWRITE;
                default: throw new XMLException("Unknown import mode");
            }
        } catch (Exception e) {
            throw new XMLException("Could not parse file: " + e);
        }
    }
    
    /**
     * Imports XML data
     * @throws XMLException 
     */
    public void importXML() throws XMLException
    {
        Document doc = loadFile();
        ImportMode importMode = getImportMode();
        
        NodeList importData = doc.getElementsByTagName(ROOT_TAG);
        if(importData != null && importData.getLength() > 0)
        {        
            importCountries(((Element)importData.item(0)).getElementsByTagName(COUNTRY_TAG), importMode);
        }
        else
        {
            throw new XMLException("Invalid or empty data file");
        }
    }
    
    /**
     * Imports countries
     * @param XMLcountries - countries elements
     * @param importMode - import mode
     * @throws XMLException 
     */
    private void importCountries(NodeList XMLcountries, ImportMode importMode) throws XMLException
    {
        if(XMLcountries != null)
            for(int i = 0; i < XMLcountries.getLength(); ++i)
            {
                Node XMLcountry = XMLcountries.item(i);
                if(XMLcountry instanceof Element)
                {
                    Country country = null;
                    
                    try
                    {
                        country = new Country(new BigDecimal(((Element) XMLcountry).getAttribute("id")),
                                              ((Element) XMLcountry).getAttribute("name"));
                    }
                    catch(Exception e)
                    {
                        throw new XMLException("Could not parse COUNTRY: " + e.getMessage()); 
                    }
                    
                    boolean exists = (countryFacade.find(country.getId()) != null);
                    
                    switch(importMode)
                    {
                        case REWRITE:
                            if(exists)
                                countryFacade.update(country); //no break here
                        case ADD_ONLY:
                            if(!exists)
                                countryFacade.add(country);
                    }
                    
                    NodeList XMLregions = ((Element) XMLcountry).getElementsByTagName(REGION_TAG);
                    if(XMLregions != null && XMLregions.getLength() > 0)      
                        importRegions(country, XMLregions, importMode);
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
    private void importRegions(Country countryId, NodeList XMLregions, ImportMode importMode) throws XMLException
    {
        if(XMLregions != null)
            for(int i = 0; i < XMLregions.getLength(); ++i)
            {
                Node XMLregion = XMLregions.item(i);
                if(XMLregion instanceof Element)
                {
                    Region region = null;
                    
                    try
                    {
                        region = new Region(new BigDecimal(((Element) XMLregion).getAttribute("id")),
                                            ((Element) XMLregion).getAttribute("name"), countryId);
                    }
                    catch(Exception e)
                    {
                        throw new XMLException("Could not parse REGION: " + e.getMessage()); 
                    }
                    
                    boolean exists = (regionFacade.find(region.getId()) != null);
                    
                    switch(importMode)
                    {
                        case REWRITE:
                            if(exists)
                                regionFacade.update(region); //no break here
                        case ADD_ONLY:
                            if(!exists)
                                regionFacade.add(region);
                    }
                    
                    NodeList XMLoffices = ((Element) XMLregion).getElementsByTagName(OFFICE_TAG);
                    if(XMLoffices != null && XMLoffices.getLength() > 0)      
                        importOffices(region, XMLoffices, importMode); 
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
    private void importOffices(Region regionId, NodeList XMLoffices, ImportMode importMode) throws XMLException
    {
        if(XMLoffices != null)
            for(int i = 0; i < XMLoffices.getLength(); ++i)
            {
                Node XMLoffice = XMLoffices.item(i);
                if(XMLoffice instanceof Element)
                {
                    Office office = null;
                    
                    try
                    {
                        office = new Office(new BigDecimal(((Element) XMLoffice).getAttribute("id")),
                                            ((Element) XMLoffice).getAttribute("name"), regionId);
                    }
                    catch(Exception e)
                    {
                        throw new XMLException("Could not parse OFFICE: " + e.getMessage()); 
                    }
                    
                    boolean exists = (officeFacade.find(office.getId()) != null);
                    
                    switch(importMode)
                    {
                        case REWRITE:
                            if(exists)
                                officeFacade.update(office); //no break here
                        case ADD_ONLY:
                            if(!exists)
                                officeFacade.add(office);
                    }
                    
                    NodeList XMLstaff = ((Element) XMLoffice).getElementsByTagName(STAFF_TAG);
                    if(XMLstaff != null && XMLstaff.getLength() > 0)      
                        importStaff(office, XMLstaff, importMode);
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
    private void importStaff(Office officeId, NodeList XMLstaff, ImportMode importMode) throws XMLException
    {
        if(XMLstaff != null)
            for(int i = 0; i < XMLstaff.getLength(); ++i)
            {
                Node XMLemployee = XMLstaff.item(i);
                if(XMLemployee instanceof Element)
                {
                    Staff employee = null;
                    
                    try
                    {
                        employee = new Staff(new BigDecimal(((Element) XMLemployee).getAttribute("id")),
                                             ((Element) XMLemployee).getAttribute("firstName"),
                                             ((Element) XMLemployee).getAttribute("secondName"), officeId);
                    }
                    catch(Exception e)
                    {
                        throw new XMLException("Could not parse STAFF: " + e.getMessage()); 
                    }
                    
                    boolean exists = (staffFacade.find(employee.getId()) != null);
                    
                    switch(importMode)
                    {
                        case REWRITE:
                            if(exists)
                                staffFacade.update(employee); //no break here
                        case ADD_ONLY:
                            if(!exists)
                                staffFacade.add(employee);
                    }
                }
            }
    }
}
