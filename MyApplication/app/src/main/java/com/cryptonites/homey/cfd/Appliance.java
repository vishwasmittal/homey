package com.cryptonites.homey.cfd;

import org.json.JSONObject;

/**
 * This class will contain the features of an appliance and will give it to
 */

public class Appliance {

    private final String name;
    private int powerLevel;
    private boolean state;

    public Appliance(String name){
        this.name = name;
        powerLevel = 0;
        state = false;
    }

    public Appliance(String name, boolean state) {
        this.name = name;
        this.state = state;
        powerLevel = 0;
    }

    public Appliance(Appliance apl) {
        this.name = apl.getName();
        this.state = apl.getState();
        this.powerLevel = this.getPowerLevel();
    }

    public Appliance(String name, int powerLevel){
        this.name = name;
        this.powerLevel = powerLevel;
        state = false;
    }

    public Appliance(String name, int powerLevel, boolean state) {
        this.name = name;
        this.powerLevel = powerLevel;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public JSONObject getJsonObject(){
        JSONObject applJSON = new JSONObject();
        try{
            applJSON.put("applName", this.name);
            applJSON.put("state", (this.state)?"true":"false");
            applJSON.put("powerLevel", this.powerLevel);
        }catch (Exception e) {e.printStackTrace();}
        return applJSON;
    }
}