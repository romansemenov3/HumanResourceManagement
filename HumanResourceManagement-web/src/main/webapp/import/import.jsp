<%-- 
    Document   : load
    Created on : Mar 8, 2015, 10:56:02 AM
    Author     : Roman
--%>

<form method="POST" action="/HumanResourceManagement-web/import" enctype="multipart/form-data" >
    File:
    <input type="file" name="DataImportFile" /> <br/><br/>
    Import type:<br/>
    <input type="radio" name="DataImportType" value="XML" checked>XML<br/>
    <input type="radio" name="DataImportType" value="JSON">JSON<br/>
    Import mode:<br/>
    <input type="radio" name="DataImportMode" value="ADD_ONLY" checked>Add only<br/>
    <input type="radio" name="DataImportMode" value="REWRITE">Rewrite<br/>
    <br/>
    <input type="submit" value="Import" name="upload" />
</form>

${importResult}
