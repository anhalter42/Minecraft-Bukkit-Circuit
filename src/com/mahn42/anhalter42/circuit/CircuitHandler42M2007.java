/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author andre
 */
public class CircuitHandler42M2007 extends CircuitHandler {
    
    public CircuitHandler42M2007() {
        super("DFP8", "42M2007");
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.addArea(PinMode.Output, "out", "lm", "rtmu", 7);
    }
    
    @Override
    protected void tick() {
        for(int lX = 0; lX < 7; lX++) {
            for(int lY = 0; lY < 7; lY++) {
                setPinInArea("out", lX, lY, fContext.pins.get("pin1").newValue);
            }
        }
    }
}
