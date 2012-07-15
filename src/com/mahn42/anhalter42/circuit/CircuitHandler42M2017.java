/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.anhalter42.circuit.CircuitFont.CircuitFontChar;
import java.util.Random;

/**
 *
 * @author andre
 */
public class CircuitHandler42M2017 extends CircuitHandler {
    
    public CircuitHandler42M2017() {
        super("DFP8", "42M2017");
        description = "7x7 MiniGame";
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Output);
        pins.addArea(PinMode.Output, "out", 7);
    }

    public class Display {
        public boolean pixels[];
        public int width;
        public int height;
        
        public Display(int aWidth, int aHeight) {
            width = aWidth;
            height = aHeight;
            pixels = new boolean[height * width];
        }

        public void clear(boolean aValue) {
            for(int lX = 0; lX < width; lX++) {
                for(int lY = 0; lY < height; lY++) {
                    setPixel(lX, lY, aValue);
                }
            }
        }
        
        public void fromString(String aDisplay) {
            fromString(aDisplay,"\\n");
        }
        public void fromString(String aDisplay, String aSep) {
            clear(false);
            if (aDisplay != null && !aDisplay.isEmpty()) {
                String lLines[] = aDisplay.split(aSep);
                int lY = 0;
                for(String lLine : lLines) {
                    int lX = 0;
                    for(char lChar : lLine.toCharArray()) {
                        setPixel(lX, lY, lChar == 'X');
                        lX++;
                    }
                    lY++;
                }
            }
        }
        
        @Override
        public String toString() {
            return toString("\n");
        }
        public String toString(String aSep) {
            String lResult = "";
            for(int lY = 0; lY < height; lY++) {
                for(int lX = 0; lX < width; lX++) {
                    lResult += getPixel(lX, lY) ? "X" : "_";
                }
                lResult += aSep;
            }
            return lResult;
        }
        
        public void setPixel(int aX, int aY, boolean aValue) {
            pixels[aX+aY*width] = aValue;
        }
        
        public boolean getPixel(int aX, int aY) {
            return pixels[aX+aY*width];
        }
        
        public void output() {
            for(int lX = 0; lX < width; lX++) {
                for(int lY = 0; lY < height; lY++) {
                    setPinInArea("out", lX + 1, height - lY, getPixel(lX, lY));
                }
            }
        }
        
        public void setChar(String aName) {
            CircuitFontChar lChar = Circuit.plugin.configASCI_7.getChar(aName);
            for(int lY = 0; lY < height; lY++) {
                for(int lX = 0; lX < width; lX++) {
                    setPixel(lX, lY, lChar.getPixel(lX, lY));
                }
            }
        }
    }
    
    @Override
    protected void tick() {
        if (getPinValue("pin3")) {
            int lFlush = fContext.circuit.getNamedValueAsInt("Flush");
            if (lFlush <= 0) {
                int lDelay = fContext.circuit.getNamedValueAsInt("Delay");
                if (lDelay <= 0) {
                    int lStartCounter = fContext.circuit.getNamedValueAsInt("StartCounter");
                    if (lStartCounter >= 0) {
                        Display lDisplay = new Display(7,7);
                        lDisplay.setChar("" + lStartCounter);
                        lDisplay.output();
                        lStartCounter--;
                    } else {
                        int lPos = fContext.circuit.getNamedValueAsInt("PlayerPos");
                        CircuitPin lPin1 = getPin("pin1");
                        CircuitPin lPin2 = getPin("pin2");
                        if (lPos > 0 && lPin1.newValue) {
                            lPos--;
                        }
                        if (lPos < 6 && lPin2.newValue) {
                            lPos++;
                        }
                        Random lRnd = new Random();
                        Display lDisplay = new Display(7,7);
                        lDisplay.fromString(fContext.circuit.getNamedValue("Display"), "\\.");
                        boolean lHit = lDisplay.getPixel(lPos, 5);
                        for(int lY = 5; lY > 0; lY--) {
                            for(int lX = 0; lX < 7; lX++) {
                                boolean lStone = lDisplay.getPixel(lX, lY-1);
                                lDisplay.setPixel(lX, lY, lStone);
                            }
                        }
                        for(int lX = 0; lX < 7; lX++) {
                            boolean lStone = lRnd.nextInt(100) < 10;
                            lDisplay.setPixel(lX, 0, lStone);
                        }
                        for(int lX = 0; lX < 7; lX++) {
                            lDisplay.setPixel(lX, 6, lPos == lX);
                        }
                        lDisplay.output();
                        if (lHit) {
                            lDisplay.clear(false);
                            lFlush = 20;
                            lStartCounter = 5;
                        }
                        fContext.circuit.setNamedValue("Display", lDisplay.toString("."));
                        fContext.circuit.setNamedValueAsInt("PlayerPos", lPos);
                    }
                    fContext.circuit.setNamedValueAsInt("StartCounter", lStartCounter);
                    lDelay = 10;
                } else {
                    lDelay--;
                }
                fContext.circuit.setNamedValueAsInt("Delay", lDelay);
            } else {
                lFlush--;
                Display lDisplay = new Display(7,7);
                lDisplay.clear((lFlush & 2) > 0);
                lDisplay.output();
            }
            fContext.circuit.setNamedValueAsInt("Flush", lFlush);
        } else {
            int lScreenSaver = fContext.circuit.getNamedValueAsInt("ScreenSaver");
            lScreenSaver = (lScreenSaver + 1) & 15;
            Display lDisplay = new Display(7,7);
            lDisplay.setChar(fSSCharNames[lScreenSaver]);
            lDisplay.output();
            fContext.circuit.setNamedValueAsInt("ScreenSaver", lScreenSaver);
            fContext.circuit.setNamedValueAsInt("StartCounter", 8);
            fContext.circuit.setNamedValueAsInt("Flush", 20);
        }
    }

    static String[] fSSCharNames = new String[16];
    {
        fSSCharNames[0] = " ";
        fSSCharNames[1] = "Line 7";
        fSSCharNames[2] = "Fill 2";
        fSSCharNames[3] = "Fill 3";
        fSSCharNames[4] = "Fill 4";
        fSSCharNames[5] = "Fill 5";
        fSSCharNames[6] = "Fill 6";
        fSSCharNames[7] = "Block";
        fSSCharNames[8] = "Fill 6";
        fSSCharNames[9] = "Fill 5";
        fSSCharNames[10] = "Fill 4";
        fSSCharNames[11] = "Fill 3";
        fSSCharNames[12] = "Fill 2";
        fSSCharNames[13] = "Line 7";
        fSSCharNames[14] = "Smile";
        fSSCharNames[15] = "Smile";
    }
}
