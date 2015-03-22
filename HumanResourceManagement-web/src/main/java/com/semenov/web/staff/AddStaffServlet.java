/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.web.staff;

import com.semenov.core.data.accessobjects.CountryFacade;
import com.semenov.core.data.accessobjects.OfficeFacade;
import com.semenov.core.data.accessobjects.StaffFacade;
import com.semenov.core.data.entities.Office;
import com.semenov.core.data.entities.Staff;
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
@WebServlet(name = "AddStaffServlet", urlPatterns = {"/add_staff"})
public class AddStaffServlet extends HttpServlet {
    
    @EJB(beanName="countryOnline")
    CountryFacade countryFacade;
    
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
            
        String officeId = request.getParameter("office_id");
        if (StringUtils.isNotEmpty(officeId)) {
            Office office = officeFacade.find(new BigDecimal(officeId));
            
            Staff staffToAdd = new Staff(null, "New", "Employee", office);
            staffFacade.add(staffToAdd);
            
            request.setAttribute("office", office);
            request.setAttribute("employees", staffFacade.list(office));
            request.setAttribute("content", "staff/employees.jsp");
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
