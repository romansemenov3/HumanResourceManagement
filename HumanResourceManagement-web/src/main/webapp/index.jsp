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
    <style type="text/css">
        td {padding: 3px; padding-left: 5px; padding-right: 5px}
        th {padding: 3px}
    </style>
    <script src="js/jquery.js"></script>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Human Resource Management</title>
    </head>
    <body>
        <c:choose>
            <c:when test="${content == null}">
                <a href="country">Editor</a><br/>
                <a href="import/import.jsp">Import</a><br/>
            </c:when>
            <c:otherwise>                
                <c:import url="${content}" />
            </c:otherwise>
        </c:choose>
    </body>
</html>
