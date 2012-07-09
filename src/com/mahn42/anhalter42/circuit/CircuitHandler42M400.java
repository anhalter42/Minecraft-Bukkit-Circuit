/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author andre
 */
public class CircuitHandler42M400 extends CircuitHandler {
    
    public CircuitHandler42M400() {
        super("DIP8", "42M400");
        description = "4 Bit Counter 3 Input 5 Output";
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
        pins.add(PinMode.Output);
    }
    
    private int asInt(boolean aBool) {
        return aBool ? 1 : 0;
    }
    
    private int asInt(CircuitPin aPin) {
        return asInt(aPin.newValue);
    }
    
    @Override
    protected void tick() {
        CircuitPin lReset = getPin("pin2");
        if (lReset.newValue) {
            setPin("pin4", false);
            setPin("pin5", false);
            setPin("pin6", false);
            setPin("pin7", false);
            setPin("pin8", false);
        } else {
            CircuitPin lTick = getPin("pin1");
            if (lTick.newValue && !lTick.oldValue) {
                int lMax = 15;
                try { lMax = Integer.parseInt(fContext.circuit.signLine1); } catch (Exception ex) { }
                CircuitPin lBit1 = getPin("pin8");
                CircuitPin lBit2 = getPin("pin7");
                CircuitPin lBit3 = getPin("pin6");
                CircuitPin lBit4 = getPin("pin5");
                int lCount = asInt(lBit1) | (asInt(lBit2) << 1) | (asInt(lBit3) << 2) | (asInt(lBit4) << 3);
                lCount += getPinValue("pin3") ? -1 : 1;
                setPin("pin4", (lCount > lMax) || (lCount < 0));
                if (lCount > lMax) lCount = 0;
                lCount &= 0xF;
                setPin("pin8", (lCount & 1) > 0);
                setPin("pin7", (lCount & 2) > 0);
                setPin("pin6", (lCount & 4) > 0);
                setPin("pin5", (lCount & 8) > 0);
            }
            if (lReset.newValue && !lReset.oldValue) {
                setPin("pin4", false);
                setPin("pin5", false);
                setPin("pin6", false);
                setPin("pin7", false);
                setPin("pin8", false);
            }
        }
    }
}
