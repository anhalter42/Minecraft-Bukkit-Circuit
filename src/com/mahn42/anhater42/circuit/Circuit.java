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
    
    public CircuitTask circuitTask;
    public HashMap<String, CircuitHandler> circuitHandlers = new HashMap<String, CircuitHandler>();

    public static void main(String[] args) {
    }

    @Override
    public void onEnable() {
        framework = Framework.plugin;
        DBs = new WorldDBList<CircuitBuildingDB>(CircuitBuildingDB.class, this);
        
        framework.registerSaver(DBs);
        
        circuitTask = new CircuitTask(this);
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, circuitTask, 10, 2);
        
        CircuitBuildingHandler lHandler = new CircuitBuildingHandler(this);

        registerCircuitHandler(new CircuitHandler42M00());
        CircuitDescription lDesc;
        
        BuildingDetector lDetector = framework.getBuildingDetector();
        lDesc = new CircuitDescription();
        lDesc.name = "42M00"; //AND
        lDesc.type = CircuitDescription.Type.DIP;
        lDesc.pins.add(CircuitDescription.PinMode.Input);
        lDesc.pins.add(CircuitDescription.PinMode.Input);
        lDesc.pins.add(CircuitDescription.PinMode.Output);
        lDesc.pins.add(CircuitDescription.PinMode.Output);
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.activate();
        lDesc.createAndActivateXZ();
        /*
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
        */
    }
    
    public void registerCircuitHandler(CircuitHandler aHandler) {
        circuitHandlers.put(aHandler.name, aHandler);
    }
    
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }
}
