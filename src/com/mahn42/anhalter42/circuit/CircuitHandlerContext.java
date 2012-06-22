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

    public CircuitHandlerContext(CircuitBuilding aCircuit) {
        circuit = aCircuit;
    }
}
