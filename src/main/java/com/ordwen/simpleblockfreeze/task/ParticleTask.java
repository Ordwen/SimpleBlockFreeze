package com.ordwen.simpleblockfreeze.task;

import com.ordwen.simpleblockfreeze.LocationKey;
import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class ParticleTask extends BukkitRunnable {

    private final static int INTERVAL = 10;

    private final SimpleBlockFreeze plugin;
    private final UUID uuid;
    private int numberOfLoop;

    public static BukkitTask runFor(Player player, int seconds) {
        return new ParticleTask(SimpleBlockFreeze.instance(), player.getUniqueId(), seconds).runTaskTimer(
                SimpleBlockFreeze.instance(),
                INTERVAL,
                INTERVAL
        );
    }

    public ParticleTask(SimpleBlockFreeze plugin, UUID uuid, int seconds) {
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
            player.spawnParticle(
                    Particle.REDSTONE,
                    location,
                    4,
                    0.4,
                    0.4,
                    0.4,
                    new Particle.DustOptions(Color.YELLOW, 1.2f)
            );
        }
    }
}
