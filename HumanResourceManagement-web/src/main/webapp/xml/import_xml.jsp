<%-- 
    Document   : load
    Created on : Mar 8, 2015, 10:56:02 AM
    Author     : Roman
--%>

<form method="POST" action="import_xml" enctype="multipart/form-data" >
    File:
    <input type="file" name="XMLDataFile" /> <br/><br/>
    Import mode:<br/>
    <input type="radio" name="XMLDataImportMode" value="ADD_ONLY" checked>Add only<br/>
    <input type="radio" name="XMLDataImportMode" value="REWRITE">Rewrite<br/>
    <br/>
    <input type="submit" value="Import" name="upload" />
</form>

${importResult}
