/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.BlockPosition;
import java.util.ArrayList;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author andre
 */
public class CommandList implements CommandExecutor {
    
    public Circuit plugin;
    
    public CommandList(Circuit aPlugin) {
        plugin = aPlugin;
    }

    @Override
    public boolean onCommand(CommandSender aCommandSender, Command aCommand, String aString, String[] aStrings) {
        String lName = "";
        String lWorldName = "";
        Player lPlayer = null;
        if (aStrings.length > 0) {
            lName = aStrings[0];
        }
        if (aStrings.length > 1) {
            lWorldName = aStrings[1];
        }
        if (aCommandSender instanceof Player) {
            lPlayer = (Player)aCommandSender;
            if (lName.isEmpty()) lName = lPlayer.getName();
            if (lWorldName.isEmpty()) lWorldName = lPlayer.getWorld().getName();
        } else {
            if (lName.isEmpty()) lName = "all";
            if (lWorldName.isEmpty()) lWorldName = plugin.getServer().getWorlds().get(0).getName();
        }
        if (lName.equals("types")) {
            for(CircuitHandler lHandler : plugin.circuitHandlers.values()) {
                String lLine = lHandler.typeName + " " + lHandler.name + " " + lHandler.description;
                if (lPlayer != null) {
                    lPlayer.sendMessage(lLine);
                } else {
                    plugin.getLogger().info(lLine);
                }
            }
        } if (lName.equals("target")) {
            Block lBlock = lPlayer.getTargetBlock(null, 100);
            CircuitBuildingDB lDB = plugin.DBs.getDB(lBlock.getWorld());
            ArrayList<CircuitBuilding> lBuildings = lDB.getBuildings(new BlockPosition(lBlock.getLocation()));
            for(CircuitBuilding lCircuit : lBuildings) {
                lPlayer.sendMessage("type: " + lCircuit.circuitType);
                lPlayer.sendMessage(" player: " + lCircuit.playerName);
                if (lCircuit.signLine1 != null && !lCircuit.signLine1.isEmpty()) lPlayer.sendMessage(" sign1: " + lCircuit.signLine1);
                if (lCircuit.signLine2 != null && !lCircuit.signLine2.isEmpty()) lPlayer.sendMessage(" sign2: " + lCircuit.signLine2);
                if (lCircuit.signLine3 != null && !lCircuit.signLine3.isEmpty()) lPlayer.sendMessage(" sign3: " + lCircuit.signLine3);
                String[] lpinValues = lCircuit.pinValues.split("\\|");
                int lCount = lpinValues.length;
                if (lCount > 20) lCount = 20;
                for(int lPin = 0; lPin < lCount; lPin += 2) {
                    String lLine = " pin" + (lPin+1) + " " + (lpinValues[lPin] == "true" ? "1" : "0");
                    if ((lPin + 1) < lpinValues.length)
                        lLine += " pin" + (lPin+2) + " " + (lpinValues[lPin+1] == "true" ? "1" : "0");
                    lPlayer.sendMessage(lLine);
                }
                for(String lKeyName : lCircuit.namedValues.keySet()) {
                    lPlayer.sendMessage(" " + lKeyName + "=" + lCircuit.namedValues.get(lKeyName));
                }
            }
        } else {
            for(CircuitBuilding lCircuit : plugin.DBs.getDB(lWorldName)) {
                if (lName.equals("all") || lCircuit.playerName.equals(lName)) {
                    String lLine = lCircuit.circuitType + " " + lCircuit.getName() + " " + lCircuit.playerName + " @ " + lCircuit.edge1;
                    if (lPlayer != null) {
                        lPlayer.sendMessage(lLine);
                    } else {
                        plugin.getLogger().info(lLine);
                    }
                }
            }
        }
        return true;
    }
}
