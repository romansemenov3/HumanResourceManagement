/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.web.country;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.semenov.core.data.accessobjects.CountryFacade;
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
@WebServlet(name = "CountryJsonServlet", urlPatterns = {"/country/json"})
public class CountryJsonServlet extends HttpServlet {

    @EJB(beanName="countryOnline")
    CountryFacade countryFacade;
    
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
    	
    	//?length=x
    	//?length=x&page=y
    	//?id=x
        
    	Gson gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
    	String countriesData = "";
    	
    	String pageLength = request.getParameter("length");    	
        if (StringUtils.isNotEmpty(pageLength)) { 
        	int length = Integer.parseInt(pageLength);
        	String pageNumber = request.getParameter("page");
            if (StringUtils.isNotEmpty(pageNumber)) {
            	countriesData = gson.toJson(countryFacade.page(length, Integer.parseInt(pageNumber)));
            }
            else
            {
            	countriesData = "{\"pages\":\""
            					+ String.valueOf(countryFacade.getRowCount() / length + 
            									((countryFacade.getRowCount() % length == 0) ? 0 : 1)) +
            					"\"}";
            }
        }
        else
        {
        	String countryID = request.getParameter("id");
        	if (StringUtils.isNotEmpty(countryID)) { 
        		countriesData = gson.toJson(countryFacade.find(new BigDecimal(countryID)));
        	}
        	else
        	{
        		countriesData = gson.toJson(countryFacade.list());
        	}
        }

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println( countriesData );
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
