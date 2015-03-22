/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.xml;

import com.semenov.core.data.accessobjects.CountryFacade;
import com.semenov.core.data.accessobjects.OfficeFacade;
import com.semenov.core.data.accessobjects.RegionFacade;
import com.semenov.core.data.accessobjects.StaffFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

/**
 * The class <code>XMLBean</code> represents common XML operations
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public abstract class XMLBean{
    
    @Inject
    protected HttpServletRequest request;
    
    @EJB(beanName="countryOnline")
    protected CountryFacade countryFacade;
    
    @EJB(beanName="regionOnline")
    protected RegionFacade regionFacade;
    
    @EJB(beanName="officeOnline")
    protected OfficeFacade officeFacade;
    
    @EJB(beanName="staffOnline")
    protected StaffFacade staffFacade;
    
    protected DocumentBuilder documentBuilder;
    protected Transformer transformer;
    
    {
        try {
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (ParserConfigurationException | TransformerConfigurationException ex) {
            Logger.getLogger(XMLBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }            
    
    /**
     * HTML file uploader id
     */
    public final String DATA_FILE = "DataImportFile";
    
    /**
     * HTML data merge value
     */
    public final String DATA_IMPORT_MODE = "DataImportMode";
    
    protected  final String ROOT_TAG = "human_resource_management_data";
    protected  final String COUNTRY_TAG = "country";
    protected  final String REGION_TAG = "region";
    protected  final String OFFICE_TAG = "office";
    protected  final String STAFF_TAG = "staff";
   
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
}
