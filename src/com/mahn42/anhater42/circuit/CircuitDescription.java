/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhater42.circuit;

import com.mahn42.framework.BuildingDescription;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class CircuitDescription extends BuildingDescription {

    public enum Type {
        DIP, // Dual in Line (Rechteck mit Pins aun 2 Seiten)
        QFP  // Quad mit Pins an allen Seiten (Quadrat)
    }
    
    public enum PinMode {
        Input,
        Output,
        NotConnected
    }
    
    public Type type = Type.DIP;
    public int inputCount = 0;
    public int outputCount = 0;
    public int ncCount = 0;
    
    public CircuitDescription(Type aType, PinMode[] aInPins) {
        switch (aType) {
            case DIP: makeDIP(aInPins); break;
            case QFP: makeQFP(aInPins); break;
        }
    }

    // In1, In2 ...
    // Out1, Out2 ...   OutLever1, OutLever2 ...
    // NC1, NC2 ...
    private void makeDIP(PinMode[] aInPins) {
        int lCount = aInPins.length;
        lCount = ((lCount + 1) >> 1) << 1; 
        int lWidth = lCount >> 1;
        BlockDescription lBlock;
        RelatedTo lRel;
        lBlock = newBlockDescription("lt");
        lBlock.materials.add(Material.WOOL);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, 0, 0), "rt");
        lRel.minDistance = lWidth - 1;
        lBlock.newRelatedTo(new Vector(0, 0, 1), "lb");
        lBlock = newBlockDescription("rt");
        lBlock.materials.add(Material.WOOL);
        lBlock.newRelatedTo(new Vector(0, 0, 1), "rb");
        lBlock = newBlockDescription("lb");
        lBlock.materials.add(Material.WOOL);
        lBlock.signSensible = true;
        lBlock = newBlockDescription("rb");
        lBlock.materials.add(Material.WOOL);
        String lLastPin = null;
        String lNewPin = null;
        int lDir = 1;
        int lPos = 0;
        for(PinMode lMode : aInPins) {
            if (lPos == (lCount << 1)) {
                lDir = -1;
                lLastPin = null;
            }
            switch (lMode) {
                case Input:
                    inputCount++;
                    lNewPin = "In" + inputCount;
                    lBlock = newBlockDescription(lNewPin);
                    lBlock.redstoneSensible = true;
                    break;
                case Output:
                    outputCount++;
                    lNewPin = "Out" + outputCount;
                    lBlock = newBlockDescription("OutLever" + outputCount);
                    lBlock.newRelatedTo(new Vector(0, -1, 0), lNewPin);
                    lBlock = newBlockDescription(lNewPin);
                    break;
                case NotConnected:
                    ncCount++;
                    lNewPin = "NC" + ncCount;
                    lBlock = newBlockDescription(lNewPin);
                    break;
            }
            if (lLastPin == null) {
                if (lDir > 0) {
                    lBlock.newRelatedTo(new Vector(0,0,1), "lb");
                } else {
                    lBlock.newRelatedTo(new Vector(0,0,-1), "rt");
                }
            } else {
                lRel = lBlock.newRelatedTo(new Vector(2 * lDir,0,0), lLastPin);
                lRel.minDistance = 1;
            }
            lLastPin = lNewPin;
        }
    }

    private void makeQFP(PinMode[] aInPins) {
    }

}
