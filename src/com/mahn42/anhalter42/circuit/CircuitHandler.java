/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.BuildingBlock;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author andre
 */
public class CircuitHandler {

    public String typeName;
    public String name;
    public Pins pins = new Pins();
    
    public enum PinMode {
        Input,
        Output,
        NotConnected
    }
    
    public class Pin {
        public PinMode mode;
        public String name;
        public String pinName;
        public boolean isArea = false;
        public int areaX;
        public int areaY;
        
        public Pin(PinMode aMode, String aName, String aPinName) {
            mode = aMode;
            name = aName;
            pinName = aPinName;
        }

        public Pin(PinMode aMode, String aPinName) {
            mode = aMode;
            name = aPinName;
            pinName = aPinName;
        }
    }
    
    public class Pins extends ArrayList<Pin> {
        public boolean add(PinMode aMode) {
            return add(new Pin(aMode, "pin" + new Integer(size() + 1)));
        }

        public boolean add(PinMode aMode, String aName) {
            return add(new Pin(aMode, aName, "pin" + new Integer(size() + 1)));
        }
        
        public boolean add(PinMode aMode, String aName, String aPinName) {
            return add(new Pin(aMode, aName, aPinName));
        }
        
        public void addArea(PinMode aMode, String aName, int aLength) {
            for(int x = 1; x <= aLength; x++) {
                for(int y = 1; y <= aLength; y++) {
                    Pin lPin = new Pin(aMode, getPinAreaName(aName, x, y));
                    lPin.isArea = true;
                    lPin.areaX = x;
                    lPin.areaY = y;
                    add(lPin);
                }
            }
        }
        
        public Pin get(String aName) {
            for(Pin lPin : this) {
                if (lPin.name.equals(aName)) {
                    return lPin;
                }
            }
            return null;
        }

        public Pin get(String aName, int aX, int aY) {
            String lName = getPinAreaName(aName, aX, aY);
            for(Pin lPin : this) {
                if (lPin.name.equals(lName)) {
                    return lPin;
                }
            }
            return null;
        }

        public Pin getWithPinName(String aPinName) {
            for(Pin lPin : this) {
                if (lPin.pinName.equals(aPinName)) {
                    return lPin;
                }
            }
            return null;
        }
    }
    
    protected CircuitHandlerContext fContext;
    
    public CircuitHandler(String aTypeName, String aName) {
        typeName = aTypeName;
        name = aName;
    }
    
    public static String getPinAreaName(String aName, int aX, int aY) {
        return aName + "_" + aX + "_" + aY;
    }


    public void execute(CircuitHandlerContext aContext) {
        fContext = aContext;
        tick();
    }
    
    protected void tick() {
        // should be override
        //Logger.getLogger("CircuitHandler").info(this.getClass().getName() + ".tick()");
    }
    
    public boolean acceptPins(CircuitBuilding aCircuit, ArrayList<String> aFails) {
        boolean lResult = true;
        for(Pin lPin : pins) {
            BuildingBlock lBBlock = aCircuit.getBlock(lPin.pinName);
            if (lBBlock != null) {
                Block lBlock;
                if (lPin.isArea) {
                    lBlock = lBBlock.position.getBlock(aCircuit.world);
                } else {
                    lBlock = lBBlock.position.getBlockAt(aCircuit.world, 0, 1, 0);
                }
                switch (lPin.mode) {
                    case Output: // a Lever must there
                        if (!lBlock.getType().equals(Material.LEVER)) {
                            aFails.add(lBBlock.description.name + " must have a lever on top!");
                            lResult = false;
                        }
                        break;
                    case Input: // no Lever should there
                        if (lBlock.getType().equals(Material.LEVER)) {
                            aFails.add(lBBlock.description.name + " does not have a lever on top!");
                            lResult = false;
                        }
                        break;
                    case NotConnected: // can be any
                        break;
                }
            } else {
                Logger.getLogger("CircuitHandler").info("block '" + lPin.pinName + "' not found for pin '" + lPin.name + "'!");
            }
        }
        return lResult;
    }
    
    protected CircuitPin getPin(String aName) {
        return fContext.pins.get(aName);
    }
    
    protected boolean getPinValue(String aName) {
        return fContext.pins.get(aName).oldValue;
    }
    
    protected int getPinValueInt(String aName) {
        return fContext.pins.get(aName).oldValue ? 1 : 0;
    }
    
    protected void setPin(String aName, boolean aValue) {
        CircuitPin lPin = fContext.pins.get(aName);
        if (lPin == null) {
            Pin lLPin = pins.get(aName);
            if (lLPin != null && lLPin.mode == PinMode.Output) {
                lPin = new CircuitPin();
                lPin.name = aName;
                lPin.oldValue = !aValue; // be sure we set it
                fContext.pins.put(lPin.name, lPin);
            } else {
                Logger.getLogger("CircuitHandler").info(this.getClass().getSimpleName() + ": Unkown or no output pin '" + aName + "'");
            }
        }
        lPin.newValue = aValue;
        /*
        if (lPin.newValue != lPin.oldValue) {
            Logger.getLogger("CircuitHandler").info(this.getClass().getSimpleName() + ": set pin '" + aName + "' to " + aValue);
        }
        */
    }

    protected void setPinInArea(String aName, int aX, int aY, boolean aValue) {
        String lName = getPinAreaName(aName, aX, aY);
        CircuitPin lPin = fContext.pins.get(lName);
        if (lPin == null) {
            Pin lLPin = pins.get(lName);
            if (lLPin != null && lLPin.mode == PinMode.Output) {
                lPin = new CircuitPin();
                lPin.name = lName;
                lPin.oldValue = !aValue; // be sure we set it
                fContext.pins.put(lPin.name, lPin);
            } else {
                Logger.getLogger("CircuitHandler").info(this.getClass().getSimpleName() + ": Unkown or no output pin '" + lName + "'");
            }
        }
        lPin.newValue = aValue;
    }
}
