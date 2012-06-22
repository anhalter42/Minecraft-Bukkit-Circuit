/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhater42.circuit;

import com.mahn42.framework.BuildingDB;
import java.io.File;
import org.bukkit.World;

/**
 *
 * @author andre
 */
public class CircuitBuildingDB extends BuildingDB<CircuitBuilding> {

    public CircuitBuildingDB() {
        super(CircuitBuilding.class);
    }

    public CircuitBuildingDB(World aWorld, File aFile) {
        super(CircuitBuilding.class, aWorld, aFile);
    }
    
}
