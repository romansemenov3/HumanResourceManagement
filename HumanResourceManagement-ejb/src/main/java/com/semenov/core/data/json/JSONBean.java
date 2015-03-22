/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.json;

import com.semenov.core.data.accessobjects.CountryFacade;
import com.semenov.core.data.accessobjects.OfficeFacade;
import com.semenov.core.data.accessobjects.RegionFacade;
import com.semenov.core.data.accessobjects.StaffFacade;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * The class <code>JSONBean</code> represents common JSON operations
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public abstract class JSONBean{
    
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
    
    /**
     * HTML file uploader id
     */
    public final String DATA_FILE = "DataImportFile";
    
    /**
     * HTML data merge value
     */
    public final String DATA_IMPORT_MODE = "DataImportMode";
    
    protected  final String COUNTRY_TAG = "human_resource_management_data";
    protected  final String REGION_TAG = "regions";
    protected  final String OFFICE_TAG = "offices";
    protected  final String STAFF_TAG = "staff";
   
    /* DATA SAMPLE
    {"human_resource_management_data": [
        {"id":"10", "name":"Country10", "regions": [
            {"id":"10", "name":"Region10", "offices": [
                {"id":"10", "name":"Office10", "staff": [
                    {"id":"10", "firstName":"John", "secondName":"Brown"},
                    {"id":"11", "firstName":"Stanley", "secondName":"Sandfield"}
                    ]
                }
                ]
            },
            {"id":"11", "name":"Region11", "offices": [
                {"id":"11", "name":"Office11", "staff": []}
                ]
            }
            ]
        },
        {"id":"100", "name": "Country100", "regions": [
            {"id":"100", "name":"Region100", "offices": [
                {"id":"100", "name":"Office100", "staff": [
                    {"id":"100", "firstName":"Eric", "secondName":"Malldraw"}
                    ]
                }
                ]
            }
            ]
        }
        ]
    }
    */
}
