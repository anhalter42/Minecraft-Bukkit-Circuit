/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author andre
 */
public class CircuitHandler42M00 extends CircuitHandler {
    
    public CircuitHandler42M00() {
        super("DIP4", "42M00");
        description = "AND + NAND Gatter 2 Input";
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
    }
    
    @Override
    protected void tick() {
        CircuitPin lPin1 = getPin("pin1");
        CircuitPin lPin2 = getPin("pin2");
        int lWaitTicks = fContext.circuit.getNamedValueAsInt("waitticks");
        // hat sich ein eingang geändert?
        if (hasInputPinsChanged()) {
            if (!fContext.circuit.signLine1.isEmpty()) {
                try {
                    lWaitTicks = Integer.parseInt(fContext.circuit.signLine1);
                } catch (Exception ex) {
                    lWaitTicks = 0;
                }
            } else {
                lWaitTicks = 0;
            }
        }
        if (lWaitTicks > 0) {
            lWaitTicks--;
        }
        if (lWaitTicks == 0) {
            setPin("pin3", lPin1.newValue && lPin2.newValue);
            setPin("pin4", !(lPin1.newValue && lPin2.newValue));
            lWaitTicks = -1; // finish
        }
        fContext.circuit.setNamedValueAsInt("waitticks", lWaitTicks);
    }
}
