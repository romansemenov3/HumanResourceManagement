<%-- 
    Document   : countries
    Created on : Feb 18, 2015, 10:58:04 AM
    Author     : Roman Semenov <romansemenov3@gmial.com>
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<a href="/HumanResourceManagement-web/office?region_id=${office.regionId.id}">Back to ${office.regionId.name} offices</a>
<table border="1" style="border: 1px solid black; border-collapse: collapse">
    <tr>
        <th>Employee ID</th>
        <th>Employee First Name</th>
        <th>Employee Second Name</th>
        <th>Office Name</th>
        <th>Region Name</th>
        <th>Country Name</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach items="${employees}" var="employee">
        <tr>
            <td>${employee.id}</td>
            <td>${employee.firstName}</td>
            <td>${employee.secondName}</td>
            <td>${employee.officeId.name}</td>
            <td>${employee.officeId.regionId.name}</td>
            <td>${employee.officeId.regionId.countryId.name}</td>
            <td><a href="/HumanResourceManagement-web/staff?id=${employee.id}">Edit</a></td>
            <td><a href="/HumanResourceManagement-web/delete_staff?id=${employee.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<br>
<a href="/HumanResourceManagement-web/add_staff?office_id=${office.id}">Add staff</a>

