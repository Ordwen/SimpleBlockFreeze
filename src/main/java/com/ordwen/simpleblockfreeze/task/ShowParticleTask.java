package com.ordwen.simpleblockfreeze.task;

import com.ordwen.simpleblockfreeze.LocationKey;
import com.ordwen.simpleblockfreeze.Options;
import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.enums.ParticleType;
import com.ordwen.simpleblockfreeze.tools.EnumUtils;
import com.ordwen.simpleblockfreeze.tools.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class ShowParticleTask extends BukkitRunnable {

    private static final int INTERVAL = 10;

    private final SimpleBlockFreeze plugin;
    private final UUID uuid;
    private int numberOfLoop;

    public static BukkitTask runFor(Player player, int seconds) {
        return new ShowParticleTask(SimpleBlockFreeze.instance(), player.getUniqueId(), seconds).runTaskTimer(
                SimpleBlockFreeze.instance(),
                INTERVAL,
                INTERVAL
        );
    }

    public ShowParticleTask(SimpleBlockFreeze plugin, UUID uuid, int seconds) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.numberOfLoop = 20 / INTERVAL * seconds;
    }

    @Override
    public void run() {
        if(numberOfLoop <= 0) {
            cancel();
            return;
        }

        final Player player = Bukkit.getPlayer(uuid);

        if(player == null || !player.isOnline()) {
            cancel();
            return;
        }

        --numberOfLoop;

        for(LocationKey key : plugin.getBlockManager().getKeys()) {
            final Location location = key.toLocation();
            spawnParticle(player, location);
        }
    }

    private void spawnParticle(Player player, Location location) {

        final Pair<Particle, Color> particleOptions = Options.PARTICLES.get(ParticleType.SHOW);

        final Particle particle = particleOptions.getFst();
        final Color color = particleOptions.getSnd();

        if(color != null) {
            player.spawnParticle(
                    particle,
                    location,
                    4,
                    0.4,
                    0.4,
                    0.4,
                    new Particle.DustOptions(particleOptions.getSnd(), 1.2f)
            );
        } else {
            player.spawnParticle(
                    particle,
                    location,
                    4,
                    0.4,
                    0.4,
                    0.4
            );
        }
    }
}
