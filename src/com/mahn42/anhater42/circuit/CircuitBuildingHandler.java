/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhater42.circuit;

import com.mahn42.framework.Building;
import com.mahn42.framework.BuildingDB;
import com.mahn42.framework.BuildingHandlerBase;
import org.bukkit.World;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author andre
 */
class CircuitBuildingHandler extends BuildingHandlerBase {

    protected Circuit plugin;
    
    public CircuitBuildingHandler(Circuit aPlugin) {
        plugin = aPlugin;
    }

    @Override
    public boolean breakBlock(BlockBreakEvent aEvent, Building aBuilding) {
        World lWorld = aEvent.getBlock().getWorld();
        CircuitBuildingDB lDB = plugin.DBs.getDB(lWorld);
        lDB.remove(aBuilding);
        return true;
    }

    @Override
    public boolean redstoneChanged(BlockRedstoneEvent aEvent, Building aBuilding) {
        boolean lOpen = aEvent.getNewCurrent() > 0;
        CircuitBuilding lGate = (CircuitBuilding)aBuilding;
        return true;
    }

    @Override
    public boolean playerInteract(PlayerInteractEvent aEvent, Building aBuilding) {
        return true;
    }

    @Override
    public BuildingDB getDB(World aWorld) {
        return plugin.DBs.getDB(aWorld);
    }
    
}
