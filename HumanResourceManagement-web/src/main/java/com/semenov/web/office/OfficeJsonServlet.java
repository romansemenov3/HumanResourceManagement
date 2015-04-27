/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.web.office;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
@WebServlet(name = "OfficeJsonServlet", urlPatterns = {"/office/json"})
public class OfficeJsonServlet extends HttpServlet {

    
    @EJB(beanName="officeOnline")
    OfficeFacade officeFacade;
    
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
        
    	//?region_id=i
    	//?region_id=i&length=x
    	//?region_id=i&length=x&page=y
    	//?id=x
        
    	Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
    	String officesData = "";
    	
    	String regionId = request.getParameter("region_id");    	
    	
        if (StringUtils.isNotEmpty(regionId)) {
            Region region = regionFacade.find(new BigDecimal(regionId));
            if(region != null)
            {            
            	String pageLength = request.getParameter("length"); 
            	if(StringUtils.isNotEmpty(pageLength))
            	{            	
	            	int length = Integer.parseInt(pageLength);            	
		            	
		        	String pageNumber = request.getParameter("page");
		            if (StringUtils.isNotEmpty(pageNumber)) {
		            	officesData = gson.toJson(officeFacade.page(region, length, Integer.parseInt(pageNumber)));
		            }
		            else
		            {
		            	officesData = "{\"pages\":\""
		            					+ String.valueOf(officeFacade.getRowCount(region) / length + 
		            									((officeFacade.getRowCount(region) % length == 0) ? 0 : 1)) +
		            					"\"}";
		            }
            	}
            	else
            	{
            		officesData = gson.toJson(officeFacade.list(region));
            	}
            }
        }
        else
        {
        	String officeID = request.getParameter("id");
        	if (StringUtils.isNotEmpty(officeID)) { 
        		officesData = gson.toJson(officeFacade.find(new BigDecimal(officeID)));
        	}
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println( officesData );
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
