package com.example.uberapp_tim.model.vehicle;

public enum CarType {
    STANDARD("STANDARD"),
    LUXURY("LUXURY"),
    VAN("VAN");

    private String friendlyName;

    private CarType(String friendlyName){
        this.friendlyName = friendlyName;
    }

    @Override public String toString(){
        return friendlyName;
    }

}
