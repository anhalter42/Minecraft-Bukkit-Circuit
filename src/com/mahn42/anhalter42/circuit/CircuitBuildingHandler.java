/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.Building;
import com.mahn42.framework.BuildingDB;
import com.mahn42.framework.BuildingHandlerBase;
import java.util.ArrayList;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 *
 * @author andre
 */
public class CircuitBuildingHandler extends BuildingHandlerBase {

    protected Circuit plugin;
    
    public CircuitBuildingHandler(Circuit aPlugin) {
        plugin = aPlugin;
    }

    @Override
    public boolean redstoneChanged(BlockRedstoneEvent aEvent, Building aBuilding) {
        CircuitBuilding lCircuit = (CircuitBuilding)aBuilding;
        plugin.circuitTask.addChange(lCircuit,aEvent.getBlock(), aEvent.getNewCurrent());
        return true;
    }

    @Override
    public Building insert(Building aBuilding) {
        CircuitBuildingDB lDB = (CircuitBuildingDB)getDB(aBuilding.world);
        CircuitBuilding lCircuit = new CircuitBuilding();
        lCircuit.cloneFrom(aBuilding);
        CircuitDescription lCDesc = (CircuitDescription)lCircuit.description;
        lCircuit.sendToPlayer("Circuit design " + lCDesc.circuitTypeName + " detected.");
        Sign lSign = (Sign)lCircuit.getBlock("sign").position.getBlock(lCircuit.world).getState();
        String[] lSignLines = lSign.getLines();
        if (lSignLines.length > 1) {
            lCircuit.signLine1 = lSignLines[1];
        }
        if (lSignLines.length > 2) {
            lCircuit.signLine2 = lSignLines[2];
        }
        if (lSignLines.length > 3) {
            lCircuit.signLine3 = lSignLines[3];
        }
        if (checkPins(lCircuit, lSignLines[0])) {
            lCircuit.sendToPlayer("Circuit type " + lCircuit.circuitType + " found.");
            lDB.addRecord(lCircuit);
            return lCircuit;
        } else {
            return null;
        }
    }
    
    @Override
    public BuildingDB getDB(World aWorld) {
        return plugin.DBs.getDB(aWorld);
    }
    
    protected boolean checkPins(CircuitBuilding aCircuit, String aHandlerName) {
        if (aHandlerName != null && !aHandlerName.isEmpty()) {
            CircuitHandler lHandler = plugin.circuitHandlers.get(aHandlerName);
            if (lHandler == null) {
                aCircuit.sendToPlayer("Circuit " + aHandlerName + " unknown!");
                return false;
            } else {
                if (lHandler.typeName.equals(((CircuitDescription)aCircuit.description).circuitTypeName)) {
                    ArrayList<String> lFails = new ArrayList<String>();
                    if (lHandler.acceptPins(aCircuit, lFails)) {
                        aCircuit.circuitType = lHandler.name;
                        return true;
                    } else {
                        aCircuit.sendToPlayer("Circuit " + lHandler.name + " has a different pin assignments (check levers)!");
                        for(String lFail : lFails) {
                            aCircuit.sendToPlayer(lFail);
                        }
                        return false;
                    }
                } else {
                    aCircuit.sendToPlayer("Circuit " + lHandler.name + " needs to be type " + lHandler.typeName);
                    return false;
                }
            }
        } else {
            aCircuit.sendToPlayer("Circuit must have a given type on first line on sign!");
            return false;
        }
    }
}
