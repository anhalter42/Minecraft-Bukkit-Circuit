/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhater42.circuit;

import com.mahn42.framework.BuildingDescription;
import com.mahn42.framework.BuildingDetector;
import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldDBList;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 *
 * @author andre
 */
public class Circuit extends JavaPlugin {

    public Framework framework;

    public WorldDBList<CircuitBuildingDB> DBs;
    
    protected HashMap<CircuitBuilding, CircuitTask> fCircuitTasks = new HashMap<CircuitBuilding, CircuitTask>();

    public static void main(String[] args) {
    }

    public void onEnable() {
        framework = Framework.plugin;
        DBs = new WorldDBList<CircuitBuildingDB>(CircuitBuildingDB.class, this);
        
        framework.registerSaver(DBs);
        
        CircuitHandler lHandler = new CircuitHandler(this);
        
        BuildingDescription lDesc;
        BuildingDescription.BlockDescription lBDesc;
        BuildingDescription.RelatedTo lRel;
        
        BuildingDetector lDetector = framework.getBuildingDetector();
        lDesc = new CircuitDescription(CircuitDescription.Type.DIP, new CircuitDescription.PinMode[] {
            CircuitDescription.PinMode.Output,              // Tor 1 hoch/runter
            CircuitDescription.PinMode.Output,              // Pumpe an/aus
            CircuitDescription.PinMode.Output,              // Tor 2 hoch/runter
            CircuitDescription.PinMode.NotConnected,
            CircuitDescription.PinMode.Input,               // Wechsel Signal an Tor 1
            CircuitDescription.PinMode.NotConnected,
            CircuitDescription.PinMode.Input,               // Wechsel Signal an Tor 2
            CircuitDescription.PinMode.NotConnected
        });
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.activate();
    }
    
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }
}
