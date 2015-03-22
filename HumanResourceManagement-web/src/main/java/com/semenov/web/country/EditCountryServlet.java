/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.web.country;

import com.semenov.core.data.accessobjects.CountryFacade;
import com.semenov.core.data.entities.Country;
import com.semenov.core.utils.StringUtils;
import java.io.IOException;
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
@WebServlet(name = "EditCountryServlet", urlPatterns = {"/edit_country"})
public class EditCountryServlet extends HttpServlet {
    
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
        
        String countryId = request.getParameter("id");
        String countryName = request.getParameter("name");
        if (StringUtils.isNotEmpty(countryId)) {
            Country countryToEdit = countryFacade.find(new BigDecimal(countryId));
            
            if(countryToEdit != null)
            {
                countryToEdit.setName(countryName);
                countryFacade.update(countryToEdit);
            }
            
            request.setAttribute("country", countryToEdit);
            request.setAttribute("content", "country/country.jsp");
        }
        else {        
            
            request.setAttribute("countries", countryFacade.list());
            request.setAttribute("content", "country/countries.jsp");
        }
        
        request.getRequestDispatcher("index.jsp").forward(request, response);
        
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
