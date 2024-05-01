package com.ordwen.simpleblockfreeze.flag.impl;

import com.ordwen.simpleblockfreeze.flag.FlagTester;
import com.ordwen.simpleblockfreeze.flag.FlagType;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.session.SessionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class WorldGuardFlagTester implements FlagTester {

    private final Map<FlagType, StateFlag> flags = new EnumMap<>(FlagType.class);

    @Override
    public void registerFlags() {

        final FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        for(FlagType type : FlagType.values()) {
            try {
                final StateFlag flag = new StateFlag(sanitize(type), true);
                registry.register(flag);
                flags.put(type, flag);
            } catch (FlagConflictException exception) {
                PluginLogger.error("An error occurred while registering flag " + type + " for WorldGuard.", exception);
            }
        }
    }

    @Override
    public boolean test(FlagType type, Player player, Location location) {

        final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        final SessionManager sessionManager = WorldGuard.getInstance().getPlatform().getSessionManager();

        if(sessionManager.hasBypass(localPlayer, BukkitAdapter.adapt(location.getWorld()))) {
            return true;
        }

        for(ProtectedRegion region : getRegions(location)) {
            final StateFlag.State state = region.getFlag(flags.get(type));
            if(state == StateFlag.State.DENY) {
                return false;
            }
        }

        return true;
    }

    private String sanitize(FlagType type) {
        return type.name()
                .toLowerCase(Locale.ROOT)
                .replace("_", "-");
    }

    private Set<ProtectedRegion> getRegions(Location location) {
        final RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        final ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));
        return set.getRegions();
    }
}
