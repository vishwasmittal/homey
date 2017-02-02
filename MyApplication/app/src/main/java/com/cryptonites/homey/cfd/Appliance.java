package com.cryptonites.homey.cfd;

/**
 * This class will contain the features of an appliance and will give it to
 */

public class Appliance {

    private final String name;
    private int powerLevel;
    private boolean state;

    public Appliance(String name){
        this.name = name;
        powerLevel = 5;
        state = false;
    }

    public Appliance(String name, int powerLevel){
        this.name = name;
        this.powerLevel = powerLevel;
        state = false;
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
}
