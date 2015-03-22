<%-- 
    Document   : countries
    Created on : Feb 18, 2015, 10:58:04 AM
    Author     : Roman Semenov <romansemenov3@gmial.com>
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<table border="1" style="border: 1px solid black; border-collapse: collapse">
    <tr>
        <th>Country ID</th>
        <th>Country Name</th>
        <th></th>
        <th></th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${countries}" var="country">
        <tr>
            <td>${country.id}</td>
            <td>${country.name}</td>
            <td><a href="/HumanResourceManagement-web/country?id=${country.id}">Edit</a></td>
            <td><a href="/HumanResourceManagement-web/delete_country?id=${country.id}">Delete</a></td>
            <td><a href="/HumanResourceManagement-web/region?country_id=${country.id}">Regions</a></td>
            <td><a href="/HumanResourceManagement-web/export_xml?country_id=${country.id}">Export with content</a></td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="/HumanResourceManagement-web/add_country">Add country</a>

