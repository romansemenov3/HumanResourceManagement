<%-- 
    Document   : employee
    Created on : Feb 25, 2015, 3:46:48 PM
    Author     : Roman
--%>

<script src="js/database.js"></script>
<a href="/HumanResourceManagement-web/staff?office_id=${employee.officeId.id}">Back to ${employee.officeId.name} staff</a>
<table border="1" style="border: 1px solid black; border-collapse: collapse">
    <tr>
        <th>Column name</th>
        <th>Column value</th>
    </tr>    
    <form method="post" action="/HumanResourceManagement-web/edit_staff?id=${employee.id}">
    <tr>
        <td>Employee ID</td>
        <td>${employee.id}</td>
    </tr>
    <tr>
        <td>Employee First Name</td>
        <td><input type="text" value="${employee.firstName}" name="firstName"/></td>
    </tr>
    <tr>
        <td>Employee Second Name</td>
        <td><input type="text" value="${employee.secondName}" name="secondName"/></td>
    </tr>
    <tr>
        <td>Country Name</td>
        <td><select id="countriesList" onchange="database.regions(regionsList, countriesList.value)"></select></td>
        <script type="text/JavaScript">
            database.countriesSelected(countriesList, ${employee.officeId.regionId.countryId.id});
        </script>    
    </tr>
    <tr>
        <td>Region Name</td>
        <td><select id="regionsList" onchange="database.offices(officesList, regionsList.value)"></select></td>
        <script type="text/JavaScript">
            database.regionsSelected(regionsList, ${employee.officeId.regionId.countryId.id}, ${employee.officeId.regionId.id});
        </script>    
    </tr>
    <tr>
        <td>Office Name</td>
        <td><select id="officesList" name="officeId"></select></td>
        <script type="text/JavaScript">
            database.officesSelected(officesList, ${employee.officeId.regionId.id}, ${employee.officeId.id});
        </script>        
    </tr>
    <tr>
        <td style="text-align: right"><input type="submit" value="Save"></td>
        </form>
        <form method="post" action="/HumanResourceManagement-web/delete_staff?id=${employee.id}">
            <td style="text-align: left"><input type="submit" value="Delete"></td>
        </form>
    </tr>
</table>