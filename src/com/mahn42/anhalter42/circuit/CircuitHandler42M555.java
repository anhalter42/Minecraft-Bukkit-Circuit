/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author Nils
 */
public class CircuitHandler42M555 extends CircuitHandler{
    
    public CircuitHandler42M555() {
        super("DIP4", "42M555");
        description = "Clock generator";
        pins.add(PinMode.Input);
        pins.add(PinMode.NotConnected);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
    }
    
    @Override
    protected void tick() {
        CircuitPin lPin1 = fContext.pins.get("pin1");
        CircuitPin lPin3 = fContext.pins.get("pin3");
        int lWaitTicks = fContext.circuit.getNamedValueAsInt("waitticks");
        if (lPin1.newValue) {
            if (lWaitTicks == 0 ) {
                if (!fContext.circuit.signLine1.isEmpty()) {
                    try {
                        lWaitTicks = Integer.parseInt(fContext.circuit.signLine1);
                    } catch (Exception ex) {
                        lWaitTicks = 20;
                    }
                } else {
                    lWaitTicks = 20;
                }
                fContext.circuit.setNamedValueAsInt("waitticks", lWaitTicks);
                setPin("pin3", !lPin3.oldValue);
                setPin("pin4", lPin3.oldValue);
            }
            else {
                lWaitTicks --;
                fContext.circuit.setNamedValueAsInt("waitticks", lWaitTicks);
            }
        }
        else {
            fContext.circuit.setNamedValueAsInt("waitticks", 0);
            setPin("pin3", false);
            setPin("pin4", true);
        }
    }
}
