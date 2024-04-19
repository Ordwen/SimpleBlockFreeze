package com.ordwen.simpleblockfreeze.flag;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.flag.impl.WorldGuardFlagTester;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class FlagManager {

    private final SimpleBlockFreeze plugin;
    private final List<FlagTester> testers = new ArrayList<>();

    public FlagManager(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
    }

    /*
    In general, if this method return TRUE = AUTHORIZED
    */
    public boolean test(FlagType type, Player player, Location location) {
        return testers.stream().allMatch(tester -> tester.test(type, player, location));
    }

    public void load() {

        final PluginManager pm = plugin.getServer().getPluginManager();

        if(pm.isPluginEnabled("WorldGuard") && pm.isPluginEnabled("WorldEdit")) {
            testers.add(new WorldGuardFlagTester());
        }

        testers.forEach(FlagTester::registerFlags);
    }
}
