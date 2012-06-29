/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mahn42.anhalter42.circuit;

import com.mahn42.framework.Building;
import com.mahn42.framework.DBRecord;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author andre
 */
public class CircuitBuilding extends Building{

    public String circuitType = "";
    public String pinValues = ""; // '|' separated true|false|false|true..
    public HashMap<String, String> namedValues = new HashMap<String, String>();
    public String signLine1 = "";
    public String signLine2 = "";
    public String signLine3 = "";
    
    public void cloneFrom(DBRecord aRecord) {
       super.cloneFrom(aRecord);
       if (aRecord instanceof CircuitBuilding) {
           CircuitBuilding lCircuit = (CircuitBuilding)aRecord;
           circuitType = lCircuit.circuitType;
           pinValues = lCircuit.pinValues;
           namedValues = (HashMap<String, String>) lCircuit.namedValues.clone();
           signLine1 = lCircuit.signLine1;
           signLine2 = lCircuit.signLine2;
           signLine3 = lCircuit.signLine3;
       }
    }
    
    @Override
    protected void toCSVInternal(ArrayList aCols) {
        super.toCSVInternal(aCols);
        aCols.add(circuitType);
        aCols.add(pinValues);
        String lNamedValues = null;
        for(String lName : namedValues.keySet()) {
            if (lNamedValues == null) {
                lNamedValues = lName + "," + namedValues.get(lName);
            } else {
                lNamedValues += '|' + lName + "," + namedValues.get(lName);
            }
        }
        aCols.add(lNamedValues == null ? "" : lNamedValues);
    }

    @Override
    protected void fromCSVInternal(DBRecordCSVArray aCols) {
        super.fromCSVInternal(aCols);
        circuitType = aCols.pop();
        pinValues = aCols.pop();
        String lNamedValues = aCols.pop();
        namedValues.clear();
        if (!lNamedValues.isEmpty()) {
            String[] lNVs = lNamedValues.split("\\|");
            for(String lNV : lNVs) {
                String[] lNVLine = lNV.split("\\,");
                namedValues.put(lNVLine[0], lNVLine.length > 1 ? lNVLine[1] : "");
            }
        }
    }

    public String getNamedValue(String aName) {
        String lResult = namedValues.get(aName);
        if (lResult == null) {
            lResult = "";
            namedValues.put(aName, lResult);
        }
        return lResult;
    }

    public int getNamedValueAsInt(String aName) {
        int lResult;
        try {
            lResult = Integer.parseInt(getNamedValue(aName));
        } catch (Exception ex) {
            lResult = 0;
        }
        return lResult;
    }

    public void setNamedValue(String aName, String aValue) {
        namedValues.put(aName, aValue);
    }

    public void setNamedValueAsInt(String aName, int aValue) {
        setNamedValue(aName, new Integer(aValue).toString());
    }

}
