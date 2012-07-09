/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author andre
 */
public class CircuitHandler42M10 extends CircuitHandler {
    
    public CircuitHandler42M10() {
        super("DIP6", "42M10");
        description = "AND + NAND Gatter 4 Input";
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
    }

    @Override
    protected void tick() {
        CircuitPin lPin1 = getPin("pin1");
        CircuitPin lPin2 = getPin("pin2");
        CircuitPin lPin3 = getPin("pin3");
        CircuitPin lPin4 = getPin("pin4");
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
            setPin("pin5", lPin1.newValue && lPin2.newValue && lPin3.newValue && lPin4.newValue);
            setPin("pin6", !(lPin1.newValue && lPin2.newValue && lPin3.newValue && lPin4.newValue));
            lWaitTicks = -1; // finish
        }
        fContext.circuit.setNamedValueAsInt("waitticks", lWaitTicks);
    }
}
