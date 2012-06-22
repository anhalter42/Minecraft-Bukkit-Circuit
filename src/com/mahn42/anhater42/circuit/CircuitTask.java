/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhater42.circuit;

import com.mahn42.framework.SyncBlockList;
import java.util.ArrayList;
import java.util.HashMap;
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
        //TODO calculate distinct list for circuit with pin changes
        HashMap<CircuitBuilding, CircuitHandlerContext> lCircuits = new HashMap<CircuitBuilding, CircuitHandlerContext>();
        //TODO loop over circuits
        for (World lWorld : plugin.DBs.getWorlds()) {
            SyncBlockList lSyncList = new SyncBlockList(lWorld);
            CircuitBuildingDB lDB = plugin.DBs.getDB(lWorld);
            for(CircuitBuilding lCircuit : lDB) {
                CircuitHandlerContext lContext;
                lContext = lCircuits.get(lCircuit);
                if (lContext == null) {
                    lContext = new CircuitHandlerContext(lCircuit);
                }
                lContext.syncBlockList = lSyncList;
                CircuitHandler lHandler = plugin.circuitHandlers.get(lCircuit.circuitType);
                if (lHandler != null) {
                    lHandler.execute(lContext);
                } else {
                    plugin.getLogger().info("no handler for '" + lCircuit.circuitType + "'");
                }
            }
            lSyncList.execute();
        }
    }
    
}
