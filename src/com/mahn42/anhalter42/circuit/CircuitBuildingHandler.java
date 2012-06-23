/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.Building;
import com.mahn42.framework.BuildingDB;
import com.mahn42.framework.BuildingHandlerBase;
import org.bukkit.World;
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
            lDB.addRecord(lCircuit);
            lPlayer.sendMessage("Building " + lCircuit.getName() + " found.");
            lFound = true;
        }
        return lFound;
    }
    
    @Override
    public boolean signChanged(SignChangeEvent aEvent, Building aBuilding) {
        String[] lLines = aEvent.getLines();
        CircuitBuilding lCircuit = (CircuitBuilding)aBuilding;
        CircuitHandler lHandler = plugin.circuitHandlers.get(lLines[0]);
        if (lHandler.typeName.equals(((CircuitDescription)lCircuit.description).typeName)) {
            lCircuit.circuitType = lHandler.name;
        } else {
            Player lPlayer = aEvent.getPlayer();
            if (lPlayer != null) {
                lPlayer.sendMessage("Circuit " + lHandler.name + " needs to be type " + lHandler.typeName);
            }
        }
        return true;
    }


    @Override
    public BuildingDB getDB(World aWorld) {
        return plugin.DBs.getDB(aWorld);
    }
    
}
