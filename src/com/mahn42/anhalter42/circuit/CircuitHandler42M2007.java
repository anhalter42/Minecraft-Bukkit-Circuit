/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.anhalter42.circuit.CircuitFont.CircuitFontChar;

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
        pins.addArea(PinMode.Output, "out", 7);
    }
    
    @Override
    protected void tick() {
        int lCode = getPinValueInt("pin1")
                | (getPinValueInt("pin2") << 1)
                | (getPinValueInt("pin3") << 2)
                | (getPinValueInt("pin4") << 3)
                | (getPinValueInt("pin5") << 4)
                | (getPinValueInt("pin6") << 5)
                | (getPinValueInt("pin7") << 6)
                | (getPinValueInt("pin8") << 7);
        CircuitFontChar lChar = Circuit.plugin.configASCI_7.getChar(lCode);
        for(int lX = 1; lX <= 7; lX++) {
            for(int lY = 1; lY <= 7; lY++) {
                setPinInArea("out", lX, lY, lChar.getPixel(lX - 1, 7 - lY));
            }
        }
    }
}
