package com.ordwen.simpleblockfreeze.tools;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Particle;

public class EnumUtils {

    public static Particle verifyParticle(String given) {
        try {
            return Particle.valueOf(given);
        } catch (IllegalArgumentException e) {
            PluginLogger.warn("Invalid particle type: " + given + ". Using REDSTONE instead.");
            return Particle.REDSTONE;
        }
    }

    public static Color verifyColor(String given) {
        try {
            return DyeColor.valueOf(given).getColor();
        } catch (NumberFormatException e) {
            PluginLogger.warn("Invalid color: " + given + ". Using WHITE instead.");
            return Color.WHITE;
        }
    }
}
