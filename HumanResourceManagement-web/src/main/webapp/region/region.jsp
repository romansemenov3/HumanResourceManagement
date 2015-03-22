<%-- 
    Document   : country
    Created on : Feb 18, 2015, 4:01:14 PM
    Author     : Roman Semenov <romansemenov3@gmial.com>
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<a href="/HumanResourceManagement-web/region?country_id=${region.countryId.id}">Back to ${region.countryId.name} regions</a>
<table border="1" style="border: 1px solid black; border-collapse: collapse">
    <tr>
        <th>Column name</th>
        <th>Column value</th>
    </tr>    
    <form method="post" action="/HumanResourceManagement-web/edit_region?id=${region.id}">
    <tr>
        <td>Region ID</td>
        <td>${region.id}</td>
    </tr>
    <tr>
        <td>Region Name</td>
        <td><input type="text" value="${region.name}" name="name"/></td>
    </tr>
    <tr>
        <td>Country Name</td>
        <td>${region.countryId.name}</td>
    </tr>
    <tr>
        <td style="text-align: right"><input type="submit" value="Save"></td>
        </form>
        <form method="post" action="/HumanResourceManagement-web/delete_region?id=${region.id}">
            <td style="text-align: left"><input type="submit" value="Delete"></td>
        </form>
    </tr>
</table>