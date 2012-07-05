/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author andre
 */
public class CircuitFont {

    public class CircuitFontChar {
        public String name;
        public int width = 0;
        public int height = 0;
        public ArrayList<String> lines = new ArrayList<String>();
        
        public boolean getPixel(int aX, int aY) {
            char lChar = lines.get(aY).charAt(aX);
            return lChar == 'X' || lChar == '1';
        }
    }
    
    public int width = 0;
    public int height = 0;
    public ArrayList<CircuitFontChar> chars = new ArrayList<CircuitFont.CircuitFontChar>();
    
    public void load(List<?> lList) {
        chars.clear();
        for(Object lObject: lList) {
            LinkedHashMap lItem = (LinkedHashMap)lObject;
            CircuitFontChar lChar = new CircuitFontChar();
            Object lO = lItem.get("name");
            lChar.name = lO.toString();
            ArrayList lLines = (ArrayList) lItem.get("lines");
            int lMaxW = 0;
            for(Object lLineO : lLines) {
                String lLine = lLineO.toString();
                if (width < lLine.length()) {
                    width = lLine.length();
                }
                if (lMaxW < lLine.length()) {
                    lMaxW = lLine.length();
                }
                lChar.lines.add(lLine);
            }
            lChar.width = lMaxW;
            lChar.height = lLines.size();
            if (height < lChar.height) {
                height = lChar.height;
            }
            chars.add(lChar);
        }
    }

    public CircuitFontChar getChar(int aCode) {
        return chars.get(aCode);
    }

}
