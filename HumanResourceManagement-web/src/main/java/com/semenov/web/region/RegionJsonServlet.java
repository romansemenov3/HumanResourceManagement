/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.web.region;

import com.semenov.web.office.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.semenov.core.data.accessobjects.CountryFacade;
import com.semenov.core.data.accessobjects.OfficeFacade;
import com.semenov.core.data.accessobjects.RegionFacade;
import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Region;
import com.semenov.core.utils.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@WebServlet(name = "RegionJsonServlet", urlPatterns = {"/region/json"})
public class RegionJsonServlet extends HttpServlet {
    
    @EJB(beanName="countryOnline")
    CountryFacade countryFacade;
    
    @EJB(beanName="regionOnline")
    RegionFacade regionFacade;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    	//?country_id=i&length=x
    	//?country_id=i&length=x&page=y
    	//?id=x
        
    	Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
    	String regionsData = "";
    	
    	String countryId = request.getParameter("country_id");
    	String pageLength = request.getParameter("length");
    	
        if (StringUtils.isNotEmpty(countryId) && StringUtils.isNotEmpty(pageLength)) {

            Country country = countryFacade.find(new BigDecimal(countryId));
            if(country != null)
            {            
	        	int length = Integer.parseInt(pageLength);            	
	            	
	        	String pageNumber = request.getParameter("page");
	            if (StringUtils.isNotEmpty(pageNumber)) {
	            	regionsData = gson.toJson(regionFacade.page(country, length, Integer.parseInt(pageNumber)));
	            }
	            else
	            {
	            	regionsData = "{\"pages\":\""
	            					+ String.valueOf(regionFacade.getRowCount(country) / length + 
	            									((regionFacade.getRowCount(country) % length == 0) ? 0 : 1)) +
	            					"\"}";
	            }
            }
        }
        else
        {
        	String regionID = request.getParameter("id");
        	if (StringUtils.isNotEmpty(regionID)) { 
        		regionsData = gson.toJson(regionFacade.find(new BigDecimal(regionID)));
        	}
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println( regionsData );
        out.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
