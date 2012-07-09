/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

/**
 *
 * @author Nils
 */
public class CircuitHandler42M1000 extends CircuitHandler {

    public CircuitHandler42M1000() {
        super("DIP8", "42M1000");
        description = "Sluice Circuit";
        pins.add(PinMode.Input);  // Gate 1 In
        pins.add(PinMode.Input);  // Gate 2 In
        pins.add(PinMode.Input);  // Reset
        pins.add(PinMode.Output); // Pump Out
        pins.add(PinMode.Output); // Pump In
        pins.add(PinMode.Output); // InProcess
        pins.add(PinMode.Output); // Gate 2 Out
        pins.add(PinMode.Output); // Gate 1 Out
    }

    protected int FState;
    protected int FProcess;
    protected int FSubProcess;
    protected int FWaitTicks;
    protected boolean FDebug = false;
    
    @Override
    protected void tick() {
        boolean lProcessChanged = false;
        
        CircuitPin lPin1 = fContext.pins.get("pin1"); // Gate 1 In (Highlevel-Gate)
        CircuitPin lPin2 = fContext.pins.get("pin2"); // Gate 2 In (Lowlevel-Gate)
        CircuitPin lPin3 = fContext.pins.get("pin3"); // Reset
        // States
        // 0=Unkown
        // 1=State 1 (HighLevel)
        // 2=State 2 (LowLevel)
        FState = fContext.circuit.getNamedValueAsInt("state");
        // Processes.Subprocess
        // 0=NOP
        // 1.x=Statechange 1 --> 2 and 0 --> 2
        // 2.x=Statechange 2 --> 1
        FProcess = fContext.circuit.getNamedValueAsInt("process");
        FSubProcess = fContext.circuit.getNamedValueAsInt("subprocess");
        FWaitTicks = fContext.circuit.getNamedValueAsInt("waitticks");
        // Get Action
        switch (FProcess) {
            case 0:
                // Gate 1: Highlevel --> LowLevel
                if (lPin1.oldValue != lPin1.newValue && FState == 1) {
                    FProcess = 1;
                    FSubProcess = 0;
                    lProcessChanged = true;
                }
                // Gate 1: Lowlevel --> HighLevel
                else if (lPin1.oldValue != lPin1.newValue && FState == 2) {
                    FProcess = 2;
                    FSubProcess = 0;
                    lProcessChanged = true;
                }
                // Gate 2: Lowlevel --> HighLevel
                else if (lPin2.oldValue != lPin2.newValue && FState == 2) {
                    FProcess = 2;
                    FSubProcess = 0;
                    lProcessChanged = true;
                }
                // Gate 2: Highlevel --> LowLevel
                else if (lPin2.oldValue != lPin2.newValue && FState == 1) {
                    FProcess = 1;
                    FSubProcess = 0;
                    lProcessChanged = true;
                }
                // Reset ??? --> LowLevel
                else if (!lPin3.oldValue  && lPin3.newValue) {
                    FState = 0;
                    FProcess = 1;
                    FSubProcess = 0;
                    lProcessChanged = true;
                }
                if (FDebug && lProcessChanged) {
                    Circuit.plugin.getLogger().info("Current state: " + new Integer(FState).toString());
                    Circuit.plugin.getLogger().info("Do new Process: " + new Integer(FProcess).toString());
                }
                break;
            default:
                break;
        };
        // Evaluate Process
        switch (FProcess) {
            case 1:
                if (FState == 0 || FState == 1) {
                    DoProcess1( );
                }
                break;
            case 2:
                if (FState == 2) {
                    DoProcess2( );
                }
                break;
            default:
                break;
        };
        fContext.circuit.setNamedValueAsInt("state", FState);
        fContext.circuit.setNamedValueAsInt("process", FProcess);
        fContext.circuit.setNamedValueAsInt("subprocess", FSubProcess);
        fContext.circuit.setNamedValueAsInt("waitticks", FWaitTicks);
    }
    
    protected void DoProcess1( ) {
        if (FWaitTicks == 0) {
            if (FDebug) Circuit.plugin.getLogger().info("Current SubProcess: 1." + new Integer(FSubProcess).toString());
            switch (FSubProcess) {
                case 0:
                    setPin("pin6", true); //InProcess
                    setPin("pin8", false); //Gate 1 close
                    FSubProcess = 1;
                    if (FDebug) Circuit.plugin.getLogger().info("New SubProcess: 1." + new Integer(FSubProcess).toString());
                    if (!fContext.circuit.signLine1.isEmpty()) {
                        try {
                            FWaitTicks = Integer.parseInt(fContext.circuit.signLine1);
                        } catch (Exception ex) {
                            FWaitTicks = 100;
                        }
                    } else {
                        FWaitTicks = 100;
                    }
                    break;
                case 1:
                    setPin("pin4", false); //Pump empty
                    FSubProcess = 2;
                    if (FDebug) Circuit.plugin.getLogger().info("New SubProcess: 1." + new Integer(FSubProcess).toString());
                    if (!fContext.circuit.signLine2.isEmpty()) {
                        try {
                            if (FDebug) Circuit.plugin.getLogger().info("Pump WaitTicks1: " + fContext.circuit.signLine2);
                            FWaitTicks = Integer.parseInt(fContext.circuit.signLine2);
                            if (FDebug) Circuit.plugin.getLogger().info("Pump WaitTicks2: " + new Integer(FWaitTicks).toString());
                        } catch (Exception ex) {
                            FWaitTicks = 600;
                        }
                    } else {
                        FWaitTicks = 600;
                    }
                    break;
                case 2:
                    setPin("pin7", true); //Gate 2 open
                    setPin("pin6", false); //InProcess
                    FState = 2;
                    FProcess = 0;
                    FSubProcess = 0;
                    if (FDebug) Circuit.plugin.getLogger().info("New SubProcess: 0." + new Integer(FSubProcess).toString());
                    break;
                default:
                    break;
            }
        }
        else {
            FWaitTicks--;
        }
    }

    protected void DoProcess2( ) {
        if (FWaitTicks == 0) {
            if (FDebug) Circuit.plugin.getLogger().info("Current SubProcess: 2." + new Integer(FSubProcess).toString());
            switch (FSubProcess) {
                case 0:
                    setPin("pin6", true); //InProcess
                    setPin("pin7", false); //Gate 2 close
                    FSubProcess = 1;
                    if (FDebug) Circuit.plugin.getLogger().info("New SubProcess: 2." + new Integer(FSubProcess).toString());
                    if (!fContext.circuit.signLine3.isEmpty()) {
                        try {
                            FWaitTicks = Integer.parseInt(fContext.circuit.signLine3);
                        } catch (Exception ex) {
                            FWaitTicks = 100;
                        }
                    } else {
                        FWaitTicks = 100;
                    }
                    break;
                case 1:
                    setPin("pin4", true); //Pump full
                    FSubProcess = 2;
                    if (FDebug) Circuit.plugin.getLogger().info("New SubProcess: 2." + new Integer(FSubProcess).toString());
                    if (!fContext.circuit.signLine2.isEmpty()) {
                        try {
                            FWaitTicks = Integer.parseInt(fContext.circuit.signLine2);
                            if (FDebug) Circuit.plugin.getLogger().info("Pump WaitTicks: " + new Integer(FWaitTicks).toString());
                        } catch (Exception ex) {
                            FWaitTicks = 600;
                        }
                    } else {
                        FWaitTicks = 600;
                    }
                    break;
                case 2:
                    setPin("pin8", true); //Gate 1 open
                    setPin("pin6", false); //InProcess
                    FState = 1;
                    FProcess = 0;
                    FSubProcess = 0;
                    if (FDebug) Circuit.plugin.getLogger().info("New SubProcess: 0." + new Integer(FSubProcess).toString());
                    break;
                default:
                    break;
            }
        }
        else {
            FWaitTicks--;
        }
    }   
}
