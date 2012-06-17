/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhater42.circuit;

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
}
