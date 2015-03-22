/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.semenov.core.data.json;

/**
 * The class <code>JSONException</code> represents common JSON error
 * @author Roman Semenov <romansemenov3@gmail.com>
 */
public class JSONException extends Exception{
    /**
     * Creates JSONException
     * @param message - error message
     */
    public JSONException(String message)
    {
        super(message);
    }    
    
    /**
     * Creates JSONException
     * @param e - exception
     */
    public JSONException(Exception e)
    {
        super(e);
    }
}
