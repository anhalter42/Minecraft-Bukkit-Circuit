/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.Framework;
import com.mahn42.framework.WoolColors;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class CircuitDescription extends BuildingDescription {

    public enum Type {
        DIP, // Dual in Line (Rechteck mit Pins aun 2 Seiten)
        QFP, // Quad mit Pins an allen Seiten (Quadrat)
        DFP, // Dual Footed Package
        SFP, // Single Footed Package 
        Custom // free style
    }

    public Type type = Type.DIP;
    public int pinCount = 0;
    public String circuitTypeName;
    
    public CircuitDescription() {
    }
    
    @Override
    public void activate() {
        if (blocks.size() == 0) {
            switch (type) {
                case DIP: makeDIP(); break;
                case QFP: makeQFP(); break;
                case DFP: makeDFP(); break;
                case SFP: makeSFP(); break;
            }
        }
        super.activate();
    }
    
    @Override
    protected BuildingDescription create(String aName) {
        CircuitDescription lDesc = new CircuitDescription();
        lDesc.name = aName;
        Framework.plugin.getBuildingDetector().addDescription(lDesc);
        return lDesc;
    }
    
    @Override
    public void cloneFrom(BuildingDescription aDesc) {
        super.cloneFrom(aDesc);
        if (aDesc instanceof CircuitDescription) {
            CircuitDescription aCDesc = (CircuitDescription)aDesc;
            type = aCDesc.type;
            pinCount = aCDesc.pinCount;
            circuitTypeName = aCDesc.circuitTypeName;
        }
    }

    private void makeDIP() {
        int lCount = pinCount;
        lCount = ((lCount + 1) >> 1) << 1; 
        int lWidth = lCount - 2;
        BlockDescription lBlock;
        BlockDescription lLB, lRT, lLast = null;
        RelatedTo lRel;
        lBlock = newBlockDescription("lt");
        lBlock.detectSensible = true;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, 0, 0), "rt");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;
        lBlock.newRelatedTo(new Vector(0, 0, 1), "lb");
        
        lBlock = newBlockDescription("rt"); lRT = lBlock;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lBlock.newRelatedTo(new Vector(0, 0, 1), "rb");
        
        lBlock = newBlockDescription("lb"); lLB = lBlock;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, 0, 0), "rb");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;
        lRel = lBlock.newRelatedTo("sign", RelatedPosition.Nearby, 1);
        
        lBlock = newBlockDescription("rb");
        lBlock.materials.add(Material.WOOL, WoolColors.black);

        lBlock = newBlockDescription("sign");
        lBlock.materials.add(Material.SIGN_POST);
        lBlock.materials.add(Material.WALL_SIGN);
        
        String lNewPin = null;
        int lDir = 1;
        int lPos = 0;
        for(lPos = 0; lPos < lCount; lPos++) {
            if (lPos == (lCount / 2)) {
                lDir = -1;
                lLast = null;
            }
            lNewPin = "pin" + (lPos + 1);
            lBlock = newBlockDescription(lNewPin);
            lBlock.materials.add(Material.WOOL, WoolColors.gray);
            lBlock.materials.add(Material.WOOL, WoolColors.light_gray);
            lBlock.redstoneSensible = true;
            if (lLast == null) {
                if (lDir > 0) {
                    lLB.newRelatedTo(new Vector(0, 0, 1), lNewPin);
                } else {
                    lRT.newRelatedTo(new Vector(0, 0, -1), lNewPin);
                }
            } else {
                lRel = lLast.newRelatedTo(new Vector(2 * lDir,0,0), lBlock.name);
                lRel.materials.add(Material.AIR);
                lRel.minDistance = 1;
            }
            lLast = lBlock;
        }
    }
    
    private void makeQFP() {
        int lCount = pinCount;
        lCount = ((lCount + 3) >> 2) << 2; 
        int lWidth = ((lCount >> 2) << 1);
        BlockDescription lBlock;
        BlockDescription lLB, lRT, lLT, lRB, lLast = null;
        RelatedTo lRel;
        lBlock = newBlockDescription("lt"); lLT = lBlock;
        lBlock.detectSensible = true;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, 0, 0), "rt");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;
        lRel = lBlock.newRelatedTo(new Vector(0, 0, lWidth), "lb");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;
        
        lBlock = newBlockDescription("rt"); lRT = lBlock;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector(0, 0, lWidth), "rb");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;
        
        lBlock = newBlockDescription("lb"); lLB = lBlock;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, 0, 0), "rb");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;
        lRel = lBlock.newRelatedTo("sign", RelatedPosition.Nearby, 1);
        
        lBlock = newBlockDescription("rb"); lRB = lBlock;
        lBlock.materials.add(Material.WOOL, WoolColors.black);

        lBlock = newBlockDescription("sign");
        lBlock.materials.add(Material.SIGN_POST);
        lBlock.materials.add(Material.WALL_SIGN);
        
        String lNewPin = null;
        int lSideCount = lCount / 4;
        lLast = lLB;
        for(int lPos = 0; lPos < lSideCount; lPos++) {
            lNewPin = "pin" + (lPos + 1);
            lBlock = newBlockDescription(lNewPin);
            lBlock.materials.add(Material.WOOL, WoolColors.gray);
            lBlock.materials.add(Material.WOOL, WoolColors.light_gray);
            lBlock.redstoneSensible = true;
            if (lPos == 0) {
                lRel = lLast.newRelatedTo(new Vector( 1, 0, 1), lBlock.name);
            } else {
                lRel = lLast.newRelatedTo(new Vector( 2, 0, 0), lBlock.name);
                lRel.materials.add(Material.AIR);
                lRel.minDistance = 1;
            }
            lLast = lBlock;
        }
        lLast = lRB;
        for(int lPos = 0; lPos < lSideCount; lPos++) {
            lNewPin = "pin" + (lPos + 1 + lSideCount);
            lBlock = newBlockDescription(lNewPin);
            lBlock.materials.add(Material.WOOL, WoolColors.gray);
            lBlock.materials.add(Material.WOOL, WoolColors.light_gray);
            lBlock.redstoneSensible = true;
            if (lPos == 0) {
                lRel = lLast.newRelatedTo(new Vector( 1, 0,-1), lBlock.name);
            } else {
                lRel = lLast.newRelatedTo(new Vector( 0, 0,-2), lBlock.name);
                lRel.materials.add(Material.AIR);
                lRel.minDistance = 1;
            }
            lLast = lBlock;
        }
        lLast = lRT;
        for(int lPos = 0; lPos < lSideCount; lPos++) {
            lNewPin = "pin" + (lPos + 1 + 2 * lSideCount);
            lBlock = newBlockDescription(lNewPin);
            lBlock.materials.add(Material.WOOL, WoolColors.gray);
            lBlock.materials.add(Material.WOOL, WoolColors.light_gray);
            lBlock.redstoneSensible = true;
            if (lPos == 0) {
                lRel = lLast.newRelatedTo(new Vector(-1, 0,-1), lBlock.name);
            } else {
                lRel = lLast.newRelatedTo(new Vector(-2, 0, 0), lBlock.name);
                lRel.materials.add(Material.AIR);
                lRel.minDistance = 1;
            }
            lLast = lBlock;
        }
        lLast = lLT;
        for(int lPos = 0; lPos < lSideCount; lPos++) {
            lNewPin = "pin" + (lPos + 1 + 3 * lSideCount);
            lBlock = newBlockDescription(lNewPin);
            lBlock.materials.add(Material.WOOL, WoolColors.gray);
            lBlock.materials.add(Material.WOOL, WoolColors.light_gray);
            lBlock.redstoneSensible = true;
            if (lPos == 0) {
                lRel = lLast.newRelatedTo(new Vector(-1, 0, 1), lBlock.name);
            } else {
                lRel = lLast.newRelatedTo(new Vector( 0, 0, 2), lBlock.name);
                lRel.materials.add(Material.AIR);
                lRel.minDistance = 1;
            }
            lLast = lBlock;
        }
    }

    private void makeDFP() {
        int lCount = pinCount;
        lCount = ((lCount + 1) >> 1) << 1; 
        int lWidth = lCount - 2;
        BlockDescription lBlock;
        BlockDescription lLB, lRT, lLast = null;
        RelatedTo lRel;
        lBlock = newBlockDescription("lt");
        lBlock.detectSensible = true;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, 0, 0), "rt");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;
        lRel = lBlock.newRelatedTo(new Vector( 0, 1, 1), "out_1_1");
        lRel = lBlock.newRelatedTo(new Vector( 0, 0, 2), "lb");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = 1;
        lRel = lBlock.newRelatedTo(new Vector( 0, 1, 0), "rty");
        
        lBlock = newBlockDescription("rt"); lRT = lBlock;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector( 0, 0, 2), "rb");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = 1;
        //lRel = lBlock.newRelatedTo(new Vector(lWidth, lWidth, 0), "rtu", RelatedPosition.AreaYX);
        //lRel.materials.add(Material.WOOL, WoolColors.black);
        //lRel.minDistance = lWidth - 1;
        
        lBlock = newBlockDescription("lb"); lLB = lBlock;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, 0, 0), "rb");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;
        lRel = lBlock.newRelatedTo("sign", RelatedPosition.Nearby, 1);
        lRel = lBlock.newRelatedTo(new Vector( 0, 1, 0), "rby");
        
        lBlock = newBlockDescription("rb");
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector( 0, lWidth, 0), "rbu");
        lRel.materials.add(Material.REDSTONE_LAMP_OFF);
        lRel.materials.add(Material.REDSTONE_LAMP_ON);
        lRel.minDistance = lWidth - 1;

        lBlock = newBlockDescription("sign");
        lBlock.materials.add(Material.SIGN_POST);
        lBlock.materials.add(Material.WALL_SIGN);
        
        lBlock = newBlockDescription("rty");
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, lWidth, 0), "rtu", RelatedPosition.AreaXZ);
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lRel.minDistance = lWidth - 1;

        lBlock = newBlockDescription("rby");
        lBlock.materials.add(Material.REDSTONE_LAMP_OFF);
        lBlock.materials.add(Material.REDSTONE_LAMP_ON);
        lRel = lBlock.newRelatedTo(new Vector(lWidth, lWidth, 0), "rbu", RelatedPosition.AreaXZ);
        lRel.materials.add(Material.REDSTONE_LAMP_OFF);
        lRel.materials.add(Material.REDSTONE_LAMP_ON);
        lRel.minDistance = lWidth - 1;

        lBlock = newBlockDescription("rbu");
        lBlock.materials.add(Material.REDSTONE_LAMP_OFF);
        lBlock.materials.add(Material.REDSTONE_LAMP_ON);

        lBlock = newBlockDescription("rtu");
        lBlock.materials.add(Material.WOOL, WoolColors.black);

        for(int lX = 1; lX <= (lWidth+1) ; lX++) {
            for(int lY = 1; lY <= (lWidth+1); lY++) {
                lBlock = newBlockDescription(CircuitHandler.getPinAreaName("out", lX, lY));
                lBlock.materials.add(Material.LEVER);
                if (lY <= lWidth) {
                    lRel = lBlock.newRelatedTo(new Vector( 0, 1, 0), CircuitHandler.getPinAreaName("out", lX , lY+1));
                }
                if (lY == 1 && lX <= lWidth) {
                    lRel = lBlock.newRelatedTo(new Vector( 1, 0, 0), CircuitHandler.getPinAreaName("out", lX+1, lY));
                }
            }
        }

        String lNewPin = null;
        int lDir = 1;
        int lPos = 0;
        for(lPos = 0; lPos < lCount; lPos++) {
            if (lPos == (lCount / 2)) {
                lDir = -1;
                lLast = null;
            }
            lNewPin = "pin" + (lPos + 1);
            lBlock = newBlockDescription(lNewPin);
            lBlock.materials.add(Material.WOOL, WoolColors.gray);
            lBlock.materials.add(Material.WOOL, WoolColors.light_gray);
            lBlock.redstoneSensible = true;
            if (lLast == null) {
                if (lDir > 0) {
                    lLB.newRelatedTo(new Vector(0, 0, 1), lNewPin);
                } else {
                    lRT.newRelatedTo(new Vector(0, 0, -1), lNewPin);
                }
            } else {
                lRel = lLast.newRelatedTo(new Vector(2 * lDir,0,0), lBlock.name);
                lRel.materials.add(Material.AIR);
                lRel.minDistance = 1;
            }
            lLast = lBlock;
        }
    }

    private void makeSFP() {
        int lCount = pinCount; // 2->3 3->5 4->7
        int lWidth = (pinCount*2)-2;
        BlockDescription lBlock;
        BlockDescription lLast = null;
        BlockDescription lFirst = null;
        RelatedTo lRel;
        /*
         * W    WWWWW 
         * W    WWWWW
         * W    WWWWW
         * W    WWWWW
         * WBG  GWGWG
         * 
         */
        lBlock = newBlockDescription("lb");
        lBlock.materials.add(Material.WOOL);
        lBlock.detectSensible = true;
        lRel = lBlock.newRelatedTo(new Vector(lWidth, lWidth, 0), "rt", RelatedPosition.AreaXZ);
        lRel.materials.add(Material.WOOL);
        lRel = lBlock.newRelatedTo(new Vector( 0, 0, 1), "lbb");
        lBlock = newBlockDescription("rt");
        lBlock.materials.add(Material.WOOL);
        lBlock = newBlockDescription("lbb"); lFirst = lBlock;
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lRel = lBlock.newRelatedTo("sign", RelatedPosition.Nearby, 1);
        lRel = lBlock.newRelatedTo(new Vector( lWidth, 0, 0), "rbb");
        lRel.materials.add(Material.WOOL, WoolColors.black);
        lBlock = newBlockDescription("rbb");
        lBlock.materials.add(Material.WOOL, WoolColors.black);
        lBlock = newBlockDescription("sign");
        lBlock.materials.add(Material.SIGN_POST);
        lBlock.materials.add(Material.WALL_SIGN);
        for(int lPos = 0; lPos < lCount; lPos++) {
            String lNewPin = "pin" + (lPos + 1);
            if (lLast != null) {
                lLast.newRelatedTo(new Vector(2, 0, 0), lNewPin);
            } else {
                lFirst.newRelatedTo(new Vector(0, 0, 1), lNewPin);
            }
            lBlock = newBlockDescription(lNewPin);
            lBlock.materials.add(Material.WOOL, WoolColors.gray);
            lBlock.materials.add(Material.WOOL, WoolColors.light_gray);
            lBlock.redstoneSensible = true;
            lLast = lBlock;
        }
    }
}
