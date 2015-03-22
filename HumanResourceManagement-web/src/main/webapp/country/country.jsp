<%-- 
    Document   : country
    Created on : Feb 18, 2015, 4:01:14 PM
    Author     : Roman Semenov <romansemenov3@gmial.com>
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<a href="/HumanResourceManagement-web/country">Back to countries</a>
<table border="1" style="border: 1px solid black; border-collapse: collapse">
    <tr>
        <th>Column name</th>
        <th>Column value</th>
    </tr>    
    <form method="post" action="/HumanResourceManagement-web/edit_country?id=${country.id}">
    <tr>
        <td>Country ID</td>
        <td>${country.id}</td>
    </tr>
    <tr>
        <td>Country Name</td>
        <td><input type="text" value="${country.name}" name="name"/></td>
    </tr>
    <tr>
        <td style="text-align: right"><input type="submit" value="Save"></td>
        </form>
        <form method="post" action="/HumanResourceManagement-web/delete_country?id=${country.id}">
            <td style="text-align: left"><input type="submit" value="Delete"></td>
        </form>
    </tr>
</table>