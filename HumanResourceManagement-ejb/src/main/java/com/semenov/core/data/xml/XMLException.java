/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.xml;

/**
 * The class <code>XMLException</code> represents common XML error
 * @author Roman
 */
public class XMLException extends Exception{
    /**
     * Creates XMLException
     * @param message - error message
     */
    public XMLException(String message)
    {
        super(message);
    }    
    
    /**
     * Creates XMLException
     * @param e - exception
     */
    public XMLException(Exception e)
    {
        super(e);
    }
}
