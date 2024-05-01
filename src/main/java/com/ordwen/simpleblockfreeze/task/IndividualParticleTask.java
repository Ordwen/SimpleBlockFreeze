package com.ordwen.simpleblockfreeze.task;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.enums.ParticleType;
import com.ordwen.simpleblockfreeze.tools.EnumUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class IndividualParticleTask extends BukkitRunnable {

    private static final int INTERVAL = 10;

    private static Particle freezeParticle;
    private static Particle unfreezeParticle;

    private static Color freezeColor;
    private static Color unfreezeColor;

    private final ParticleType type;

    private final SimpleBlockFreeze plugin;
    private final UUID uuid;
    private final Location location;
    private int numberOfLoop;

    public static void init(ConfigurationSection section) {
        freezeParticle = EnumUtils.verifyParticle(section.getString("freeze.type"));
        freezeColor = EnumUtils.verifyColor(section.getString("freeze.color"));
        unfreezeParticle = EnumUtils.verifyParticle(section.getString("unfreeze.type"));
        unfreezeColor = EnumUtils.verifyColor(section.getString("unfreeze.color"));
    }

    public static BukkitTask runFor(Player player, Location location, int seconds, ParticleType type) {
        return new IndividualParticleTask(SimpleBlockFreeze.instance(), player.getUniqueId(), location, seconds, type).runTaskTimer(
                SimpleBlockFreeze.instance(),
                INTERVAL,
                INTERVAL
        );
    }

    public IndividualParticleTask(SimpleBlockFreeze plugin, UUID uuid, Location location, int seconds, ParticleType type) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.location = location;
        this.numberOfLoop = 20 / INTERVAL * seconds;
        this.type = type;
    }

    @Override
    public void run() {
        if (numberOfLoop <= 0) {
            cancel();
            return;
        }

        final Player player = Bukkit.getPlayer(uuid);

        if (player == null || !player.isOnline()) {
            cancel();
            return;
        }

        --numberOfLoop;

        switch (type) {
            case FREEZE -> spawnParticle(player, location, freezeParticle, freezeColor);
            case UNFREEZE -> spawnParticle(player, location, unfreezeParticle, unfreezeColor);
        }
    }

    private void spawnParticle(Player player, Location location, Particle particle, Color color) {
        player.spawnParticle(
                particle,
                location,
                4,
                0.4,
                0.4,
                0.4,
                new Particle.DustOptions(color, 1.2f)
        );
    }
}
