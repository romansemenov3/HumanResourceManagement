<%-- 
    Document   : countries
    Created on : Feb 18, 2015, 10:58:04 AM
    Author     : Roman Semenov <romansemenov3@gmial.com>
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<a href="/HumanResourceManagement-web/region?country_id=${region.countryId.id}">Back to ${region.countryId.name} regions</a>
<table border="1" style="border: 1px solid black; border-collapse: collapse">
    <tr>
        <th>Office ID</th>
        <th>Office Name</th>
        <th>Region Name</th>
        <th>Country Name</th>
        <th></th>
        <th></th>
        <th></th>
        <th>XML</th>
        <th>JSON</th>
    </tr>
    <c:forEach items="${offices}" var="office">
        <tr>
            <td>${office.id}</td>
            <td>${office.name}</td>
            <td>${office.regionId.name}</td>
            <td>${office.regionId.countryId.name}</td>
            <td><a href="/HumanResourceManagement-web/office?id=${office.id}">Edit</a></td>
            <td><a href="/HumanResourceManagement-web/delete_office?id=${office.id}">Delete</a></td>
            <td><a href="/HumanResourceManagement-web/staff?office_id=${office.id}">Staff</a></td>
            <td><a href="/HumanResourceManagement-web/export_xml?office_id=${office.id}">Export with content</a></td>
            <td><a href="/HumanResourceManagement-web/export_json?office_id=${office.id}">Export with content</a></td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="/HumanResourceManagement-web/add_office?region_id=${region.id}">Add office</a>

