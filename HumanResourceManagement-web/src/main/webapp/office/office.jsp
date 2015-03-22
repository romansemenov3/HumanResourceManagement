<%-- 
    Document   : country
    Created on : Feb 18, 2015, 4:01:14 PM
    Author     : Roman Semenov <romansemenov3@gmial.com>
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<a href="/HumanResourceManagement-web/office?region_id=${office.regionId.id}">Back to ${office.regionId.name} offices</a>
<table border="1" style="border: 1px solid black; border-collapse: collapse">
    <tr>
        <th>Column name</th>
        <th>Column value</th>
    </tr>    
    <form method="post" action="/HumanResourceManagement-web/edit_office?id=${office.id}">
    <tr>
        <td>Office ID</td>
        <td>${office.id}</td>
    </tr>
    <tr>
        <td>Office Name</td>
        <td><input type="text" value="${office.name}" name="name"/></td>
    </tr>
    <tr>
        <td>Region Name</td>
        <td>${office.regionId.name}</td>
    </tr>
    <tr>
        <td>Country Name</td>
        <td>${office.regionId.countryId.name}</td>
    </tr>
    <tr>
        <td style="text-align: right"><input type="submit" value="Save"></td>
        </form>
        <form method="post" action="/HumanResourceManagement-web/delete_office?id=${office.id}">
            <td style="text-align: left"><input type="submit" value="Delete"></td>
        </form>
    </tr>
</table>