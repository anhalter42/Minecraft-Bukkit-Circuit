/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.*;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author andre
 */
public class CircuitTask implements Runnable {

    public Circuit plugin;
    public int taskId;
    
    public CircuitTask(Circuit aPlugin) {
        plugin = aPlugin;
    }
    
    protected static final Object fSync = new Object();

    public class Change {
        CircuitBuilding circuit;
        Block block;
        int newCurrent;
    }
    
    protected ArrayList<Change> fChanges = new ArrayList<Change>();
    
    public void addChange(CircuitBuilding aCircuit, Block aBlock, int aNewCurrent) {
        Change lChange = new Change();
        lChange.circuit = aCircuit;
        lChange.block = aBlock;
        lChange.newCurrent = aNewCurrent;
        synchronized(fSync) {
            fChanges.add(lChange);
        }
    }

    protected boolean fInRun = false;
    
    @Override
    public void run() {
        if (!fInRun) {
            fInRun = true;
            try {
                execute();
            } finally {
                fInRun = false;
            }
        }
    }

    private void execute() {
        ArrayList<Change> lChanges;
        synchronized(fSync) {
            lChanges = fChanges;
            fChanges = new ArrayList<Change>();
        }
        //calculate distinct list for circuit with pin changes
        HashMap<CircuitBuilding, CircuitHandlerContext> lCircuits = new HashMap<CircuitBuilding, CircuitHandlerContext>();
        for(Change lChange : lChanges) {
            CircuitHandlerContext lContext = lCircuits.get(lChange.circuit);
            if (lContext == null) {
                lContext = new CircuitHandlerContext(lChange.circuit);
                lCircuits.put(lChange.circuit, lContext);
            }
            lContext.handler = plugin.circuitHandlers.get(lChange.circuit.circuitType);
            BuildingBlock lRSBlock = lContext.circuit.getRedStoneSensibles(new BlockPosition(lChange.block.getLocation()));
            CircuitHandler.Pin lHPin = lContext.handler.pins.getWithPinName(lRSBlock.description.name);
            if (lHPin.mode == CircuitHandler.PinMode.Input) {
                CircuitPin lCPin = lContext.pins.get(lHPin.name);
                if (lCPin == null) {
                    lCPin = new CircuitPin();
                    lCPin.name = lHPin.name;
                    lContext.pins.put(lCPin.name, lCPin);
                }
                lCPin.newValue = lChange.newCurrent > 0;
                plugin.getLogger().info("red stone input change on pin " + lCPin.name + " to " + lCPin.newValue);
            }
        }
        //loop over all circuits
        for (World lWorld : plugin.DBs.getWorlds()) {
            SyncBlockList lSyncList = new SyncBlockList(lWorld);
            CircuitBuildingDB lDB = plugin.DBs.getDB(lWorld);
            for(CircuitBuilding lCircuit : lDB) {
                if (lCircuit.circuitType != null && !lCircuit.circuitType.isEmpty()) {
                    CircuitHandlerContext lContext;
                    lContext = lCircuits.get(lCircuit);
                    if (lContext == null) {
                        lContext = new CircuitHandlerContext(lCircuit);
                    }
                    lContext.syncBlockList = lSyncList;
                    CircuitHandler lHandler = plugin.circuitHandlers.get(lCircuit.circuitType);
                    if (lHandler != null) {
                        String[] lpinValues = lCircuit.pinValues.split("\\|");
                        int lIndex = 0;
                        for(CircuitHandler.Pin lHPin : lHandler.pins) {
                            boolean lValue;
                            if (lIndex < lpinValues.length) {
                                lValue = Boolean.parseBoolean(lpinValues[lIndex]);
                            } else {
                                lValue = false;
                            }
                            lIndex++;
                            CircuitPin lCPin = lContext.pins.get(lHPin.name);
                            if (lCPin == null) {
                                lCPin = new CircuitPin();
                                lCPin.name = lHPin.name;
                                lCPin.newValue = lValue;
                                lContext.pins.put(lCPin.name, lCPin);
                            }
                            lCPin.oldValue = lValue;
                        }
                        
                        lHandler.execute(lContext);
                        
                        for(CircuitPin lCPin : lContext.pins.values()) {
                            if (lCPin.newValue != lCPin.oldValue) {
                                setPin(lSyncList, lCircuit.getBlock(lHandler.pins.get(lCPin.name).pinName).position, lCPin.newValue);
                            }
                        }
                        String lnewPinValues = null;
                        for(CircuitHandler.Pin lHPin : lHandler.pins) {
                            CircuitPin lCPin = lContext.pins.get(lHPin.name);
                            if (lnewPinValues == null) {
                                lnewPinValues = Boolean.toString(lCPin.newValue);
                            } else {
                                lnewPinValues += "|" + Boolean.toString(lCPin.newValue);
                            }
                        }
                        lCircuit.pinValues = lnewPinValues;
                        //plugin.getLogger().info(lCircuit.getName() + " " + lCircuit.pinValues);
                    } else {
                        plugin.getLogger().info("no handler for '" + lCircuit.circuitType + "'");
                    }
                }
            }
            lSyncList.execute();
        }
    }

    private void setPin(SyncBlockList aSyncList, BlockPosition aPosition, boolean aNewValue) {
        BlockPosition lPos = aPosition.clone();
        lPos.add(0, 1, 0);
        Block lBlock = lPos.getBlock(aSyncList.world);
        if (lBlock.getType().equals(Material.LEVER)) {
            if (aNewValue) {
                aSyncList.add(lPos, Material.LEVER, (byte)(lBlock.getData() | (byte)0x8), true);
                /*
                for(BlockPosition lUPos : new BlockPositionWalkAround(lPos, BlockPositionDelta.HorizontalAndVertical)) {
                    Block lBlockPin = lUPos.getBlock(aSyncList.world);
                    if (lBlockPin.getType().equals(Material.REDSTONE_WIRE)) {
                        aSyncList.add(lUPos, lBlockPin.getType(), (byte)0xF, true);
                    }
                }
                */
                plugin.getLogger().info("set lever at " + lPos + " true");
            } else {
                aSyncList.add(lPos, Material.LEVER, (byte)(lBlock.getData() & (byte)0xF7), true);
                /*
                for(BlockPosition lUPos : new BlockPositionWalkAround(lPos, BlockPositionDelta.HorizontalAndVertical)) {
                    Block lBlockPin = lUPos.getBlock(aSyncList.world);
                    if (lBlockPin.getType().equals(Material.REDSTONE_WIRE)) {
                        aSyncList.add(lUPos, lBlockPin.getType(), (byte)0x0, true);
                    }
                }
                */
                plugin.getLogger().info("set lever at " + lPos + " false");
            }
        } else {
            plugin.getLogger().info("no lever at " + lPos);
        }
    }
    
}
