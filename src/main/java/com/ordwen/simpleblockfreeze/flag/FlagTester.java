package com.ordwen.simpleblockfreeze.flag;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface FlagTester {

    void registerFlags();

    /**
     * return whether the state of the flag in this location (true usually means AUTHORIZED)
     * @param type
     * @param location
     * @param player
     * @return
     */
    boolean test(FlagType type, Player player, Location location);

}
