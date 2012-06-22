/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhater42.circuit;

import com.mahn42.framework.BuildingDetector;
import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldDBList;
import java.util.HashMap;
import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void onEnable() {
        framework = Framework.plugin;
        DBs = new WorldDBList<CircuitBuildingDB>(CircuitBuildingDB.class, this);
        
        framework.registerSaver(DBs);
        
        CircuitBuildingHandler lHandler = new CircuitBuildingHandler(this);
        
        CircuitDescription lDesc;
        //BuildingDescription.BlockDescription lBDesc;
        //BuildingDescription.RelatedTo lRel;
        
        BuildingDetector lDetector = framework.getBuildingDetector();
        lDesc = new CircuitDescription();
        lDesc.type = CircuitDescription.Type.DIP;
        lDesc.pins.add(CircuitDescription.PinMode.Output, "out_door1");
        lDesc.pins.add(CircuitDescription.PinMode.Output, "out_pump");
        lDesc.pins.add(CircuitDescription.PinMode.Output, "out_door2");
        lDesc.pins.add(CircuitDescription.PinMode.NotConnected);
        lDesc.pins.add(CircuitDescription.PinMode.Input, "in_door1");
        lDesc.pins.add(CircuitDescription.PinMode.NotConnected);
        lDesc.pins.add(CircuitDescription.PinMode.Input, "in_door2");
        lDesc.pins.add(CircuitDescription.PinMode.NotConnected);
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.activate();
    }
    
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }
}
