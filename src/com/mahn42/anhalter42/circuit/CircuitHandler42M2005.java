/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author andre
 */
public class CircuitHandler42M2005 extends CircuitHandler {
    
    public CircuitHandler42M2005() {
        super("DFP6", "42M2005");
        description = "5x5 Redstone Lamp Display 6 Input";
        pins.add(CircuitHandler.PinMode.Input);
        pins.add(CircuitHandler.PinMode.Input);
        pins.add(CircuitHandler.PinMode.Input);
        pins.add(CircuitHandler.PinMode.Input);
        pins.add(CircuitHandler.PinMode.Input);
        pins.add(CircuitHandler.PinMode.Input);
        pins.addArea(CircuitHandler.PinMode.Output, "out", 5);
    }
    
    @Override
    protected void tick() {
        int lCode = getPinValueInt("pin1")
                | (getPinValueInt("pin2") << 1)
                | (getPinValueInt("pin3") << 2)
                | (getPinValueInt("pin4") << 3)
                | (getPinValueInt("pin5") << 4)
                | (getPinValueInt("pin6") << 5);
        CircuitFont.CircuitFontChar lChar = Circuit.plugin.configASCI_5.getChar(lCode);
        for(int lX = 1; lX <= 5; lX++) {
            for(int lY = 1; lY <= 5; lY++) {
                setPinInArea("out", lX, lY, lChar.getPixel(lX - 1, 5 - lY));
            }
        }
    }
}
