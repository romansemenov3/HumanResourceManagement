/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.json;

/**
 * Template for imported data
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class DataImportTemplate {
    public class CountryTemplate {
        public class RegionTemplate{
            public class OfficeTemplate{
                public class StaffTemplate{
                    public String id;
                    public String firstName;
                    public String secondName;
                }
                
                public String id;
                public String name;
                public StaffTemplate[] staff;
            }
            
            public String id;
            public String name; 
            public OfficeTemplate[] offices;
        }
        
        public String id;
        public String name;
        public RegionTemplate[] regions;
    }
    
    public CountryTemplate[] human_resource_management_data;
}
