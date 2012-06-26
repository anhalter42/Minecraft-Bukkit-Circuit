/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.SyncBlockList;
import java.util.HashMap;

/**
 *
 * @author andre
 */
public class CircuitHandlerContext {
    public CircuitBuilding circuit;
    public SyncBlockList syncBlockList;
    public HashMap<String, CircuitPin> pins = new HashMap<String, CircuitPin>();
    public CircuitHandler handler;

    public CircuitHandlerContext(CircuitBuilding aCircuit) {
        circuit = aCircuit;
    }
    
    @Override
    public String toString() {
        String lResult = circuit.getName() + " ";
        for(CircuitPin lPin : pins.values()) {
            lResult += lPin.name + " " + lPin.oldValue + " -> " + lPin.newValue + "|";
        }
        return lResult;
    }
}
