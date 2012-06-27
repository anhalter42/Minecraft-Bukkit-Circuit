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
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
    public boolean breakBlock(BlockBreakEvent aEvent, Building aBuilding) {
        World lWorld = aEvent.getBlock().getWorld();
        CircuitBuilding lCircuit = (CircuitBuilding)aBuilding;
        CircuitBuildingDB lDB = plugin.DBs.getDB(lWorld);
        lDB.remove(lCircuit);
        return true;
    }

    @Override
    public boolean redstoneChanged(BlockRedstoneEvent aEvent, Building aBuilding) {
        CircuitBuilding lCircuit = (CircuitBuilding)aBuilding;
        plugin.circuitTask.addChange(lCircuit,aEvent.getBlock(), aEvent.getNewCurrent());
        return true;
    }

    @Override
    public boolean playerInteract(PlayerInteractEvent aEvent, Building aBuilding) {
        Player lPlayer = aEvent.getPlayer();
        World lWorld = lPlayer.getWorld();
        boolean lFound = false;
        CircuitBuildingDB lDB = plugin.DBs.getDB(lWorld);
        if (lDB.getBuildings(aBuilding.edge1).isEmpty()
                && lDB.getBuildings(aBuilding.edge2).isEmpty()) {
            CircuitBuilding lCircuit = new CircuitBuilding();
            lCircuit.cloneFrom(aBuilding);
            CircuitDescription lCDesc = (CircuitDescription)lCircuit.description;
            lPlayer.sendMessage("Circuit type " + lCDesc.circuitTypeName + " found.");
            Sign lSign = (Sign)lCircuit.getBlock("sign").position.getBlock(lWorld).getState();
            String[] lSignLines = lSign.getLines();
            if (lSignLines.length > 1) {
                lCircuit.signLine1 = lSign.getLine(1);
            } else if (lSignLines.length > 2) {
                lCircuit.signLine2 = lSign.getLine(2);
            } else if (lSignLines.length > 3) {
                lCircuit.signLine3 = lSign.getLine(3);
            }
            if (checkPins(lCircuit, lSignLines[0], aEvent.getPlayer())) {
                lDB.addRecord(lCircuit);
                lFound = true;
            }
        }
        return lFound;
    }
    
    @Override
    public boolean signChanged(SignChangeEvent aEvent, Building aBuilding) {
        String[] lLines = aEvent.getLines();
        CircuitBuilding lCircuit = (CircuitBuilding)aBuilding;
        checkPins(lCircuit, lLines[0], aEvent.getPlayer());
        return true;
    }


    @Override
    public BuildingDB getDB(World aWorld) {
        return plugin.DBs.getDB(aWorld);
    }
    
    protected boolean checkPins(CircuitBuilding aCircuit, String aHandlerName, Player aPlayer) {
        if (aHandlerName != null && !aHandlerName.isEmpty()) {
            CircuitHandler lHandler = plugin.circuitHandlers.get(aHandlerName);
            if (lHandler.typeName.equals(((CircuitDescription)aCircuit.description).circuitTypeName)) {
                ArrayList<String> lFails = new ArrayList<String>();
                if (lHandler.acceptPins(aCircuit, lFails)) {
                    aCircuit.circuitType = lHandler.name;
                    return true;
                } else {
                    if (aPlayer != null) {
                        aPlayer.sendMessage("Circuit " + lHandler.name + " has a different pin assignments (check levers)!");
                        for(String lFail : lFails) {
                            aPlayer.sendMessage(lFail);
                        }
                    }
                    return false;
                }
            } else {
                if (aPlayer != null) {
                    aPlayer.sendMessage("Circuit " + lHandler.name + " needs to be type " + lHandler.typeName);
                }
                return false;
            }
        } else {
            if (aPlayer != null) {
                aPlayer.sendMessage("Circuit must have a given type on first line on sign!");
            }
            return false;
        }
    }
    
}
