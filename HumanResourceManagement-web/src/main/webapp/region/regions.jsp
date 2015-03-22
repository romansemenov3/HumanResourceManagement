<%-- 
    Document   : countries
    Created on : Feb 18, 2015, 10:58:04 AM
    Author     : Roman Semenov <romansemenov3@gmial.com>
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<a href="/HumanResourceManagement-web/country">Back to countries</a>
<table border="1" style="border: 1px solid black; border-collapse: collapse">
    <tr>
        <th>Region ID</th>
        <th>Region Name</th>
        <th>Country Name</th>
        <th></th>
        <th></th>
        <th></th>
        <th>XML</th>
        <th>JSON</th>
    </tr>
    <c:forEach items="${regions}" var="region">
        <tr>
            <td>${region.id}</td>
            <td>${region.name}</td>
            <td>${region.countryId.name}</td>
            <td><a href="/HumanResourceManagement-web/region?id=${region.id}">Edit</a></td>
            <td><a href="/HumanResourceManagement-web/delete_region?id=${region.id}">Delete</a></td>
            <td><a href="/HumanResourceManagement-web/office?region_id=${region.id}">Offices</a></td>
            <td><a href="/HumanResourceManagement-web/export_xml?region_id=${region.id}">Export with content</a></td>
            <td><a href="/HumanResourceManagement-web/export_json?region_id=${region.id}">Export with content</a></td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="/HumanResourceManagement-web/add_region?country_id=${country.id}">Add region</a>

