/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author andre
 */
public class CircuitHandler {

    public String typeName;
    public String name;
    
    protected CircuitHandlerContext fContext;
    
    public CircuitHandler(String aTypeName, String aName) {
        typeName = aTypeName;
        name = aName;
    }
    
    public void execute(CircuitHandlerContext aContext) {
        fContext = aContext;
    }
}
