package com.semenov.core.data.xml;

public class ImportException extends Exception {

    public ImportException(String message)
    {
        super(message);
    }    
    
    public ImportException(Exception e)
    {
        super(e);
    }
}
