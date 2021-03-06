/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.BuildingDetector;
import com.mahn42.framework.Framework;
import com.mahn42.framework.WorldDBList;
import java.util.HashMap;
import java.util.List;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author andre
 */
public class Circuit extends JavaPlugin {

    public Framework framework;
    public static Circuit plugin;

    public WorldDBList<CircuitBuildingDB> DBs;

    public int configCircuitTicks = 1;
    public CircuitFont configASCI_7 = new CircuitFont();
    public CircuitFont configASCI_5 = new CircuitFont();
    public CircuitFont configASCI_3 = new CircuitFont();
    
    public CircuitTask circuitTask;
    public HashMap<String, CircuitHandler> circuitHandlers = new HashMap<String, CircuitHandler>();

    public static void main(String[] args) {
    }

    @Override
    public void onEnable() {
        plugin = this;
        readCircuitConfig();
        framework = Framework.plugin;
        DBs = new WorldDBList<CircuitBuildingDB>(CircuitBuildingDB.class, this);
        
        framework.registerSaver(DBs);
        
        circuitTask = new CircuitTask(this);
        getServer().getScheduler().scheduleAsyncRepeatingTask(this, circuitTask, 20, configCircuitTicks);
        getCommand("circuit_list").setExecutor(new CommandList(this));
        
        CircuitBuildingHandler lHandler = new CircuitBuildingHandler(this);

        CircuitDescription lDesc;
        
        BuildingDetector lDetector = framework.getBuildingDetector();
        
        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DIP4"; // IC with 4 Pins
        lDesc.typeName = "DIP IC 4 Pins";
        lDesc.circuitTypeName = "DIP4";
        lDesc.type = CircuitDescription.Type.DIP;
        lDesc.pinCount = 4;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DIP6"; // IC with 6 Pins
        lDesc.typeName = "DIP IC 6 Pins";
        lDesc.circuitTypeName = "DIP6";
        lDesc.type = CircuitDescription.Type.DIP;
        lDesc.pinCount = 6;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();
        
        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DIP8"; // IC with 8 Pins
        lDesc.typeName = "DIP IC 8 Pins";
        lDesc.circuitTypeName = "DIP8";
        lDesc.type = CircuitDescription.Type.DIP;
        lDesc.pinCount = 8;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DIP10"; // IC with 10 Pins
        lDesc.typeName = "DIP IC 10 Pins";
        lDesc.circuitTypeName = "DIP10";
        lDesc.type = CircuitDescription.Type.DIP;
        lDesc.pinCount = 10;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DIP12"; // IC with 12 Pins
        lDesc.typeName = "DIP IC 12 Pins";
        lDesc.circuitTypeName = "DIP12";
        lDesc.type = CircuitDescription.Type.DIP;
        lDesc.pinCount = 12;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.QFP4"; // IC with 4 Pins
        lDesc.typeName = "QFP IC 4 Pins";
        lDesc.circuitTypeName = "QFP4";
        lDesc.type = CircuitDescription.Type.QFP;
        lDesc.pinCount = 4;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();
        
        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.QFP8"; // IC with 8 Pins
        lDesc.typeName = "QFP IC 8 Pins";
        lDesc.circuitTypeName = "QFP8";
        lDesc.type = CircuitDescription.Type.QFP;
        lDesc.pinCount = 8;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();
        
        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.QFP12"; // IC with 12 Pins
        lDesc.typeName = "QFP IC 12 Pins";
        lDesc.circuitTypeName = "QFP12";
        lDesc.type = CircuitDescription.Type.QFP;
        lDesc.pinCount = 12;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();
        
        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.QFP16"; // IC with 16 Pins
        lDesc.typeName = "QFP IC 16 Pins";
        lDesc.circuitTypeName = "QFP16";
        lDesc.type = CircuitDescription.Type.QFP;
        lDesc.pinCount = 16;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();
        
        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DFP8"; // IC with 8 Pins
        lDesc.typeName = "DFP IC 8 Pins";
        lDesc.circuitTypeName = "DFP8";
        lDesc.type = CircuitDescription.Type.DFP;
        lDesc.pinCount = 8;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DFP6"; // IC with 6 Pins
        lDesc.typeName = "DFP IC 6 Pins";
        lDesc.circuitTypeName = "DFP6";
        lDesc.type = CircuitDescription.Type.DFP;
        lDesc.pinCount = 6;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DFP4"; // IC with 4 Pins
        lDesc.typeName = "DFP IC 4 Pins";
        lDesc.circuitTypeName = "DFP4";
        lDesc.type = CircuitDescription.Type.DFP;
        lDesc.pinCount = 4;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.DFP64"; // IC with 64 Pins
        lDesc.typeName = "DFP IC 64 Pins";
        lDesc.circuitTypeName = "DFP64";
        lDesc.type = CircuitDescription.Type.DFP;
        lDesc.pinCount = 64;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.SFP2"; // IC with 2 Pins
        lDesc.typeName = "SFP IC 2 Pins";
        lDesc.circuitTypeName = "SFP2";
        lDesc.type = CircuitDescription.Type.SFP;
        lDesc.pinCount = 2;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.SFP3"; // IC with 3 Pins
        lDesc.typeName = "SFP IC 3 Pins";
        lDesc.circuitTypeName = "SFP3";
        lDesc.type = CircuitDescription.Type.SFP;
        lDesc.pinCount = 3;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        lDesc = new CircuitDescription();
        lDesc.name = "Circuit.SFP4"; // IC with 4 Pins
        lDesc.typeName = "SFP IC 4 Pins";
        lDesc.circuitTypeName = "SFP4";
        lDesc.type = CircuitDescription.Type.SFP;
        lDesc.pinCount = 4;
        lDesc.handler = lHandler;
        lDetector.addDescription(lDesc);
        lDesc.createAndActivateXZ();

        registerCircuitHandler(new CircuitHandler42M00());
        registerCircuitHandler(new CircuitHandler42M01());
        registerCircuitHandler(new CircuitHandler42M02());
        registerCircuitHandler(new CircuitHandler42M05());
        registerCircuitHandler(new CircuitHandler42M10());
        registerCircuitHandler(new CircuitHandler42M11());
        registerCircuitHandler(new CircuitHandler42M12());
        registerCircuitHandler(new CircuitHandler42M50());
        registerCircuitHandler(new CircuitHandler42M400());
        registerCircuitHandler(new CircuitHandler42M555());
        registerCircuitHandler(new CircuitHandler42M1000());
        registerCircuitHandler(new CircuitHandler42M2003());
        registerCircuitHandler(new CircuitHandler42M2005());
        registerCircuitHandler(new CircuitHandler42M2007());
        registerCircuitHandler(new CircuitHandler42M2017());
        registerCircuitHandler(new CircuitHandler42M2103());
        registerCircuitHandler(new CircuitHandler42M2105());
        registerCircuitHandler(new CircuitHandler42M2107());

        // access all world, so they are loaded and can work in CircuitTask
        List<World> lWorlds = getServer().getWorlds();
        for(World lWorld : lWorlds) {
            DBs.getDB(lWorld);
        }
    }
    
    public void registerCircuitHandler(CircuitHandler aHandler) {
        circuitHandlers.put(aHandler.name, aHandler);
    }
    
    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        plugin = null;
    }

    private void readCircuitConfig() {
        FileConfiguration lConfig = getConfig();
        configCircuitTicks = lConfig.getInt("CircuitTicks", 1);
        configASCI_7.load(lConfig.getList("ASCII_7"));
        configASCI_5.load(lConfig.getList("ASCII_5"));
        configASCI_3.load(lConfig.getList("ASCII_3"));
    }

    public String getText(String aText, Object... aObjects) {
        return getText((String)null, aText, aObjects);
    }
    
    public String getText(CommandSender aPlayer, String aText, Object... aObjects) {
        return getText(Framework.plugin.getPlayerLanguage(aPlayer.getName()), aText, aObjects);
    }
    
    public String getText(String aLanguage, String aText, Object... aObjects) {
        return Framework.plugin.getText(this, aLanguage, aText, aObjects);
    }
}
