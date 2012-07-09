/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author Nils
 */
public class CircuitHandler42M50 extends CircuitHandler {

    public CircuitHandler42M50() {
        super("DIP4", "42M50");
        description = "Pulsdetector";
        pins.add(PinMode.Input);
        pins.add(PinMode.NotConnected);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
    }
        
    @Override
    protected void tick() {
        CircuitPin lPin1 = getPin("pin1");
        int lHoldTicks = fContext.circuit.getNamedValueAsInt("holdticks");
        if (!lPin1.oldValue && lPin1.newValue) {
            setPin("pin4", true);
            setPin("pin3", false);
            if (!fContext.circuit.signLine1.isEmpty()) {
                try {
                    lHoldTicks = Integer.parseInt(fContext.circuit.signLine1);
                } catch (Exception ex) {
                    lHoldTicks = 20;
                }
            } else {
                lHoldTicks = 20;
            }
        }
        else if (lHoldTicks == 0) {
            setPin("pin4", false);
            setPin("pin3", true);
        }
        else {
            lHoldTicks--;
        }
        fContext.circuit.setNamedValueAsInt("holdticks", lHoldTicks);
    }
}
