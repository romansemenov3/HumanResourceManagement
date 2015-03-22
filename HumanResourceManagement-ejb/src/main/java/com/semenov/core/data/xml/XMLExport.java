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
import com.semenov.core.utils.StringUtils;
import java.io.StringWriter;
import java.math.BigDecimal;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The class <code>XMLExport</code> represents XML export operations
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@Stateless (name="XMLExport")
@LocalBean
public class XMLExport extends XMLBean {

    /**
     * Builds <code>Element</code> representation of <code>Country</code>
     * @param country - country to convert
     * @param document - data reciever
     * @return <code>Element</code> representation of <code>Country</code>
     */
    private Element countryToElement(Country country, Document document)
    {
        Element result = document.createElement(COUNTRY_TAG);
        result.setAttribute("name", country.getName());
        result.setAttribute("id", country.getId().toString());
        return result;
    }
    
    /**
     * Builds <code>Element</code> representation of <code>Region</code>
     * @param region - region to convert
     * @param document - data reciever
     * @return <code>Element</code> representation of <code>REgion</code>
     */
    private Element regionToElement(Region region, Document document)
    {
        Element result = document.createElement(REGION_TAG);
        result.setAttribute("name", region.getName());
        result.setAttribute("id", region.getId().toString());
        return result;
    }
    
    /**
     * Builds <code>Element</code> representation of <code>Office</code>
     * @param office - office to convert
     * @param document - data reciever
     * @return <code>Element</code> representation of <code>Office</code>
     */
    private Element officeToElement(Office office, Document document)
    {
        Element result = document.createElement(OFFICE_TAG);
        result.setAttribute("name", office.getName());
        result.setAttribute("id", office.getId().toString());
        return result;
    }
    
    /**
     * Builds <code>Element</code> representation of <code>Staff</code>
     * @param staff - staff to convert
     * @param document - data reciever
     * @return <code>Element</code> representation of <code>Staff</code>
     */
    private Element staffToElement(Staff staff, Document document)
    {
        Element result = document.createElement(OFFICE_TAG);
        result.setAttribute("firstName", staff.getFirstName());
        result.setAttribute("secondName", staff.getSecondName());
        result.setAttribute("id", staff.getId().toString());
        return result;
    }
    
    /**
     * Builds XML content of <code>Country</code>
     * @param country - country to represent
     * @return XML content of <code>Country</code>
     */
    private Document exportCountry(Country country)
    {
        Document result = documentBuilder.newDocument();        
        Element root = result.createElement(ROOT_TAG);
        result.appendChild(root);
        
        Element countryElement = countryToElement(country, result);
        for(Region region : regionFacade.list(country))
        {
            Element regionElement = regionToElement(region, result);
            for(Office office : officeFacade.list(region))
            {
                Element officeElement = officeToElement(office, result);                            
                for(Staff staff : staffFacade.list(office))
                {
                    Element staffElement = staffToElement(staff, result);
                    officeElement.appendChild(staffElement);
                }
                regionElement.appendChild(officeElement);                        
            }
            countryElement.appendChild(regionElement);
        }
        root.appendChild(countryElement);
        
        return result;
    }
    
    /**
     * Builds XML content of <code>Region</code>
     * @param region - region to represent
     * @return XML content of <code>Region</code>
     */
    private Document exportRegion(Region region)
    {
        Document result = documentBuilder.newDocument();        
        Element root = result.createElement(ROOT_TAG);
        result.appendChild(root);
        
        Element countryElement = countryToElement(region.getCountryId(), result);
        Element regionElement = regionToElement(region, result);
        for(Office office : officeFacade.list(region))
        {
            Element officeElement = officeToElement(office, result);                            
            for(Staff staff : staffFacade.list(office))
            {
                Element staffElement = staffToElement(staff, result);
                officeElement.appendChild(staffElement);
            }
            regionElement.appendChild(officeElement);                        
        }
        countryElement.appendChild(regionElement);
        root.appendChild(countryElement);
        
        return result;
    }
    
    /**
     * Builds XML content of <code>Office</code>
     * @param office - office to represent
     * @return XML content of <code>Office</code>
     */
    private Document exportOffice(Office office)
    {
        Document result = documentBuilder.newDocument();
        Element root = result.createElement(ROOT_TAG);
        result.appendChild(root);
        
        Element countryElement = countryToElement(office.getRegionId().getCountryId(), result);
        Element regionElement = regionToElement(office.getRegionId(), result);
        Element officeElement = officeToElement(office, result);
        for(Staff staff : staffFacade.list(office))
        {
            Element staffElement = staffToElement(staff, result);
            officeElement.appendChild(staffElement);
        }
        regionElement.appendChild(officeElement);
        countryElement.appendChild(regionElement);
        root.appendChild(countryElement);
        
        return result;
    }
    
    /**
     * Builds XML content for export
     * @return XML content for export
     * @throws XMLException 
     */
    public String exportXML() throws XMLException
    {        
        Document exportData = null;
        
        String id = request.getParameter("country_id");
        if(StringUtils.isNotEmpty(id))
        {
            Country country = countryFacade.find(new BigDecimal(id));
            if(country != null)
                exportData = exportCountry(country);
            else
                throw new XMLException("Could not build xml: country not found");
        }
        else
        {
            id = request.getParameter("region_id");
            if(StringUtils.isNotEmpty(id))
            {
                Region region = regionFacade.find(new BigDecimal(id));
                if(region != null)
                    exportData = exportRegion(region);
                else
                    throw new XMLException("Could not build xml: region not found");
            }
            else
            {
                id = request.getParameter("office_id");
                if(StringUtils.isNotEmpty(id))
                {
                    Office office = officeFacade.find(new BigDecimal(id));
                    if(office != null)
                        exportData = exportOffice(office);
                    else
                        throw new XMLException("Could not build xml: office not found");
                }
                else
                {
                    throw new XMLException("Could not build xml: no input parameters");
                }
            }
        }
        
        StringWriter result = new StringWriter();
        try {
            transformer.transform(new DOMSource(exportData), new StreamResult(result));
        } catch (TransformerException e) {
            throw new XMLException("Could not build xml: " + e.getMessage());
        }
        
        return result.toString();
    }
}
