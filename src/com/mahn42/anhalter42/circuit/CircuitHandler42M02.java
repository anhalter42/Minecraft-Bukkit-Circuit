/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author andre
 */
public class CircuitHandler42M02 extends CircuitHandler {
    public CircuitHandler42M02() {
        super("DIP4", "42M02");
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
    }
    
    @Override
    protected void tick() {
        setPin("pin3", fContext.pins.get("pin1").newValue ^ fContext.pins.get("pin2").newValue);
        setPin("pin4", !(fContext.pins.get("pin1").newValue ^ fContext.pins.get("pin2").newValue));
    }
    
}
