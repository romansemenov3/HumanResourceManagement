<%-- 
    Document   : index
    Created on : Feb 12, 2015, 4:04:37 PM
    Author     : Roman Semenov
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <meta charset="utf-8">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resource Management</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${content == null}">
                <a href="country/countries.html">Editor</a><br/>
                <a href="import/import.jsp">Import</a><br/>
            </c:when>
            <c:otherwise>                
                <c:import url="${content}" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
