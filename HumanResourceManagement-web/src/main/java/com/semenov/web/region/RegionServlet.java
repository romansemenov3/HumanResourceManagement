/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.web.region;

import com.semenov.core.data.accessobjects.CountryFacade;
import com.semenov.core.data.accessobjects.RegionFacade;
import com.semenov.core.data.entities.Country;
import com.semenov.core.data.entities.Region;
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
 * @author Roman  Semenov <romansemenov3@gmail.com>
 */
@WebServlet(name = "RegionServlet", urlPatterns = {"/region"})
public class RegionServlet extends HttpServlet {

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
        
        String regionId = request.getParameter("id");
        if(StringUtils.isNotEmpty(regionId)) {
            Region region = regionFacade.find(new BigDecimal(regionId));
            request.setAttribute("region", region);
            request.setAttribute("country", region.getCountryId());
            
            request.setAttribute("content", "region/region.jsp");
        }
        else 
        {
            String countryId = request.getParameter("country_id");
            if (StringUtils.isNotEmpty(countryId)) {            
                Country country = countryFacade.find(new BigDecimal(countryId));
                request.setAttribute("country", country);
                request.setAttribute("regions", regionFacade.list(country));
                
                request.setAttribute("content", "region/regions.jsp");
            }
            else
            {            
                request.setAttribute("countries", countryFacade.list());
                request.setAttribute("content", "country/countries.jsp");
            }
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
