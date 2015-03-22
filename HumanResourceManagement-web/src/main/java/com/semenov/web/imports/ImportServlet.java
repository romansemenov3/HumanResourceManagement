/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.web.imports;

import com.semenov.core.data.json.JSONException;
import com.semenov.core.data.json.JSONImport;
import com.semenov.core.data.xml.XMLException;
import com.semenov.core.data.xml.XMLImport;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Roman
 */
@WebServlet(name = "ImportServlet", urlPatterns = {"/import"})
@MultipartConfig(location="/tmp", fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class ImportServlet extends HttpServlet {

    @EJB (beanName="XMLImport")
    XMLImport xmlImport;
    
    @EJB (beanName="JSONImport")
    JSONImport jsonImport;
    
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
        
        switch(request.getParameter("DataImportType"))
        {
            case "XML": 
                try
                {
                    xmlImport.importXML();
                }catch(XMLException e)
                {
                    request.setAttribute("importResult", "Import error: " + e.getMessage());
                }
            break;
            case "JSON":
                try
                {
                    jsonImport.importJSON();
                }catch(JSONException e)
                {
                    request.setAttribute("importResult", "Import error: " + e.getMessage());
                }
            break;
            default:
                request.setAttribute("importResult", "Import error: unknown data type.");
        }
        
        request.setAttribute("content", "import/import.jsp");
        
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
