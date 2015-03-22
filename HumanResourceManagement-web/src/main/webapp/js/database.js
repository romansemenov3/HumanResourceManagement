/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var database = new Object();

database.countries = function(countriesList)
{
    $.ajax({
        type: 'GET',
        url: 'country_json',
        cache: false,
        response: 'text',
        success: function (data) {
            var countriesArray = JSON.parse(data);
            
            countriesList.options.length = 0;
            
            var option = document.createElement("option");
            option.value = -1;
            option.text = 'Select country';
            option.selected = true;
            option.disabled = true;
            countriesList.add(option);
            
            for(var key in countriesArray)
            {
                var option = document.createElement("option");
                option.value = countriesArray[key].id;
                option.text = countriesArray[key].name;
                countriesList.add(option);
            }
        }
    });
}

database.countriesSelected = function(countriesList, selected)
{
    $.ajax({
        type: 'GET',
        url: 'country_json',
        cache: false,
        response: 'text',
        success: function (data) {
            var countriesArray = JSON.parse(data);
            countriesList.options.length = 0;
            for(var key in countriesArray)
            {
                var option = document.createElement("option");
                option.value = countriesArray[key].id;
                option.text = countriesArray[key].name;
                if(countriesArray[key].id === selected)
                    option.selected = true;
                
                countriesList.add(option);
            }
        }
    });
}

database.regions = function(regionsList, countryId)
{
    $.ajax({
        type: 'GET',
        url: 'region_json',
        cache: false,
        data: {country_id: countryId},
        response: 'text',
        success: function (data) {
            var regionsArray = JSON.parse(data);
            
            regionsList.options.length = 0;
            
            var option = document.createElement("option");
            option.value = -1;
            option.text = 'Select region';
            option.selected = true;
            option.disabled = true;
            regionsList.add(option);
            
            for(var key in regionsArray)
            {
                var option = document.createElement("option");
                option.value = regionsArray[key].id;
                option.text = regionsArray[key].name;
                regionsList.add(option);
            }
        }
    });
}

database.regionsSelected = function(regionsList, countryId, selected)
{
    $.ajax({
        type: 'GET',
        url: 'region_json',
        cache: false,
        data: {country_id: countryId},
        response: 'text',
        success: function (data) {
            var regionsArray = JSON.parse(data);
            regionsList.options.length = 0;
            for(var key in regionsArray)
            {
                var option = document.createElement("option");
                option.value = regionsArray[key].id;
                option.text = regionsArray[key].name;
                if(regionsArray[key].id === selected)
                    option.selected = true;
                
                regionsList.add(option);
            }
        }
    });
}               

database.offices = function(officesList, regionId)
{
    $.ajax({
        type: 'GET',
        url: 'office_json',
        cache: false,
        data: {region_id: regionId},
        response: 'text',
        success: function (data) {
            var officesArray = JSON.parse(data);
            
            officesList.options.length = 0;
            
            var option = document.createElement("option");
            option.value = -1;
            option.text = 'Select office';
            option.selected = true;
            option.disabled = true;
            officesList.add(option);
            
            for(var key in officesArray)
            {
                var option = document.createElement("option");
                option.value = officesArray[key].id;
                option.text = officesArray[key].name;
                officesList.add(option);
            }
        }
    });
}

database.officesSelected = function(officesList, regionId, selected)
{
    $.ajax({
        type: 'GET',
        url: 'office_json',
        cache: false,
        data: {region_id: regionId},
        response: 'text',
        success: function (data) {
            var officesArray = JSON.parse(data);
            officesList.options.length = 0;
            for(var key in officesArray)
            {
                var option = document.createElement("option");
                option.value = officesArray[key].id;
                option.text = officesArray[key].name;
                if(officesArray[key].id === selected)
                    option.selected = true;
                
                officesList.add(option);
            }
        }
    });
}
