package com.semenov.core.data.xml;

import java.math.BigDecimal;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Office;
import com.semenov.core.data.entities.Region;
import com.semenov.core.data.entities.Staff;

public class XMLParser extends DefaultHandler {
	
    /* DATA SAMPLE
    <?xml version="1.0" encoding="utf-8"?>
    <human_resource_management_data>
        <country id="10" name="Country10">
            <region id="10" name="Region10">
                <office id="10" name="Office10">
                    <staff id="10" firstName="John" secondName="Brown"/>
                    <staff id="11" firstName="Stanley" secondName="Sandfield"/>
                </office>
            </region>
            <region id="11" name="Region11">
                <office id="11" name="Office11"/>
            </region>
        </country>
        <country id="100" name="Country100">
            <region id="100" name="Region100">
                <office id="100" name="Office100">
                    <staff id="100" firstName="Eric" secondName="Malldraw"/>
                </office>
            </region>
        </country>
    </human_resource_management_data>
    */
	
    private Country currentCountry;
    private Region currentRegion;
    private Office currentOffice;
    private Staff currentEmployee;
    private DataImporter importer;
	
    private static final String COUNTRY_TAG = "country";
    private static final String REGION_TAG = "region";
    private static final String OFFICE_TAG = "office";
    private static final String STAFF_TAG = "staff";

    public XMLParser(DataImporter importer)
    {
        this.importer = importer;
    }

    @Override 
    public void startElement(String namespaceURI, String localName, String qName, Attributes attr) throws SAXException { 
        switch(qName)
        {
        case COUNTRY_TAG: 		
            currentCountry = new Country(new BigDecimal(attr.getValue("id")), attr.getValue("name"));
            importer.importCountry(currentCountry);
            break;
        case REGION_TAG:
            currentRegion = new Region(new BigDecimal(attr.getValue("id")), attr.getValue("name"), currentCountry);
            importer.importRegion(currentRegion);
            break;
        case OFFICE_TAG:
            currentOffice = new Office(new BigDecimal(attr.getValue("id")), attr.getValue("name"), currentRegion); 
            importer.importOffice(currentOffice);
            break;
        case STAFF_TAG:
            currentEmployee = new Staff(new BigDecimal(attr.getValue("id")), attr.getValue("firstName"), attr.getValue("secondName"), currentOffice);
            importer.importStaff(currentEmployee);
            break;
        }
    }
}
