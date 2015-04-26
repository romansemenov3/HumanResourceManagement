/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.web.staff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.semenov.core.data.accessobjects.OfficeFacade;
import com.semenov.core.data.accessobjects.StaffFacade;
import com.semenov.core.data.entities.Office;
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
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
@WebServlet(name = "StaffJsonServlet", urlPatterns = {"/staff/json"})
public class StaffJsonServlet extends HttpServlet {

    
    @EJB(beanName="officeOnline")
    OfficeFacade officeFacade;
    
    @EJB(beanName="staffOnline")
    StaffFacade staffFacade;
    
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
        
    	//?office_id=i&length=x
    	//?office_id=i&length=x&page=y
    	//?id=i
    	//?id_extended=i
        
    	Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
    	String staffData = "";
    	
    	String officeId = request.getParameter("office_id");
    	String pageLength = request.getParameter("length");
    	
        if (StringUtils.isNotEmpty(officeId) && StringUtils.isNotEmpty(pageLength)) {

            Office office = officeFacade.find(new BigDecimal(officeId));
            if(office != null)
            {            
	        	int length = Integer.parseInt(pageLength);            	
	            	
	        	String pageNumber = request.getParameter("page");
	            if (StringUtils.isNotEmpty(pageNumber)) {
	            	staffData = gson.toJson(staffFacade.page(office, length, Integer.parseInt(pageNumber)));
	            }
	            else
	            {
	            	staffData = "{\"pages\":\""
	            					+ String.valueOf(staffFacade.getRowCount(office) / length + 
	            									((staffFacade.getRowCount(office) % length == 0) ? 0 : 1)) +
	            					"\"}";
	            }
            }
        }
        else
        {
        	String staffID = request.getParameter("id");
        	if (StringUtils.isNotEmpty(staffID)) {
        		staffData = gson.toJson(staffFacade.find(new BigDecimal(staffID)));
        	}
        	else
        	{
        		staffID = request.getParameter("id_extended");
        		if (StringUtils.isNotEmpty(staffID)) {
            		staffData = gson.toJson(new StaffExtendedJSON(staffFacade.find(new BigDecimal(staffID))));
            	}
        	}
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println( staffData );
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
