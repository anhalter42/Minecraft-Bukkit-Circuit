/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author Nils
 */
public class CircuitHandler42M1000 extends CircuitHandler {

    public CircuitHandler42M1000() {
        super("DIP8", "42M1000");
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.NotConnected);
        pins.add(PinMode.NotConnected);
        pins.add(PinMode.NotConnected);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
    }

    @Override
    protected void tick() {
        CircuitPin lPin1 = fContext.pins.get("pin1");
    }
}
