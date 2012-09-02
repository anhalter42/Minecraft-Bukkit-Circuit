/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.BlockPosition;
import com.mahn42.framework.WoolColors;
import java.util.logging.Level;
import org.bukkit.Material;

/**
 *
 * @author andre
 */
public class CircuitHandler42M2107 extends CircuitHandler {

    public CircuitHandler42M2107() {
        super("SFP4", "42M2107");
        description = "7x7 Color Display 4 Input";
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
        pins.add(PinMode.Input);
    }
    
    @Override
    protected void tick() {
        if (hasInputPinsChanged()) {
            int lCode = getPinValueInt("pin1")
                    | (getPinValueInt("pin2") << 1)
                    | (getPinValueInt("pin3") << 2)
                    | (getPinValueInt("pin4") << 3);
            CircuitFont.CircuitFontChar lChar;
            if (fContext.circuit.signLine2 != null && !fContext.circuit.signLine2.isEmpty()) {
                if (fContext.circuit.signLine2.length() > lCode) {
                    char lC = fContext.circuit.signLine2.charAt(lCode);
                    lChar = Circuit.plugin.configASCI_7.getChar("" + lC);
                } else {
                    lChar = Circuit.plugin.configASCI_7.getChar(" ");
                }
            } else {
                lChar = Circuit.plugin.configASCI_7.getChar(lCode);
            }
            if (lChar == null) {
                lChar = Circuit.plugin.configASCI_7.getChar(" ");
            }
            if (lChar != null) {
                byte lColorFg = WoolColors.white;
                byte lColorBg = WoolColors.black;
                if (fContext.circuit.signLine3 != null && !fContext.circuit.signLine3.isEmpty()) {
                    try {
                        String lCols[] = fContext.circuit.signLine3.split("\\,");
                        Byte lCol = WoolColors.strings.get(lCols[0]);
                        if (lCol != null) {
                            lColorBg = lCol;
                        } else {
                            lColorBg = Byte.parseByte(lCols[0]);
                        }
                        if (lCols.length > 1) {
                            lCol = WoolColors.strings.get(lCols[1]);
                            if (lCol != null) {
                                lColorFg = lCol;
                            } else {
                                lColorFg = Byte.parseByte(lCols[1]);
                            }
                        }
                    } catch (Exception ex) {
                        Circuit.plugin.getLogger().log(Level.SEVERE, null, ex);
                    }
                }
                BlockPosition lEdge = fContext.circuit.getBlock("lb").position.clone();
                for(int lX = 0; lX < 7; lX++) {
                    for(int lY = 0; lY < 7; lY++) {
                        BlockPosition lPos = lEdge.clone();
                        lPos.add(lX, lY, 0);
                        byte lColor;
                        if (lChar.getPixel(6 - lX, 6 - lY)) {
                            lColor = lColorFg;
                        } else {
                            lColor = lColorBg;
                        }
                        fContext.syncBlockList.add(lPos, Material.WOOL, lColor);
                    }
                }
            }
        }
    }
    
}
