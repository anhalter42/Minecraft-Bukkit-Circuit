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
public class CircuitHandler42M2103  extends CircuitHandler {
    
    public CircuitHandler42M2103() {
        super("SFP2", "42M2103");
        description = "3x3 Color Display 2 Input";
        pins.add(CircuitHandler.PinMode.Input);
        pins.add(CircuitHandler.PinMode.Input);
    }
    
    @Override
    protected void tick() {
        int lCode = getPinValueInt("pin1")
                | (getPinValueInt("pin2") << 1);
        CircuitFont.CircuitFontChar lChar;
        if (fContext.circuit.signLine2 != null && !fContext.circuit.signLine2.isEmpty()) {
            if (fContext.circuit.signLine2.length() > lCode) {
                char lC = fContext.circuit.signLine2.charAt(lCode);
                lChar = Circuit.plugin.configASCI_3.getChar("" + lC);
            } else {
                lChar = Circuit.plugin.configASCI_3.getChar(" ");
            }
        } else {
            lChar = Circuit.plugin.configASCI_3.getChar(lCode);
        }
        if (lChar == null) {
            lChar = Circuit.plugin.configASCI_3.getChar(" ");
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
            for(int lX = 0; lX < 3; lX++) {
                for(int lY = 0; lY < 3; lY++) {
                    BlockPosition lPos = lEdge.clone();
                    lPos.add(lX, lY, 0);
                    byte lColor;
                    if (lChar.getPixel(2 - lX, 2 - lY)) {
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
