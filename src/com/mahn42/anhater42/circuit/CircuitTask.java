/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhater42.circuit;

/**
 *
 * @author andre
 */
class CircuitTask implements Runnable {

    public Circuit plugin;
    public int taskId;
    
    public CircuitTask(Circuit aPlugin) {
        plugin = aPlugin;
    }
    
    @Override
    public void run() {
    }
    
}
