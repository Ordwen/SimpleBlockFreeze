package com.ordwen.simpleblockfreeze.enums;

public enum ParticleType {

    SHOW,
    FREEZE,
    UNFREEZE,
    ;

    public String lower() {
        return name().toLowerCase();
    }
}
