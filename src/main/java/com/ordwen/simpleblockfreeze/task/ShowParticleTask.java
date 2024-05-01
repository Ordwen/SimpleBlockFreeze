package com.ordwen.simpleblockfreeze.task;

import com.ordwen.simpleblockfreeze.LocationKey;
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

public class ShowParticleTask extends BukkitRunnable {

    private static Particle showParticle;
    private static Color showColor;

    private static final int INTERVAL = 10;

    private final SimpleBlockFreeze plugin;
    private final UUID uuid;
    private int numberOfLoop;

    public static void init(ConfigurationSection section) {
        showParticle = EnumUtils.verifyParticle(section.getString("show.type"));
        showColor = EnumUtils.verifyColor(section.getString("show.color"));
    }

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
            player.spawnParticle(
                    showParticle,
                    location,
                    4,
                    0.4,
                    0.4,
                    0.4,
                    new Particle.DustOptions(showColor, 1.2f)
            );
        }
    }
}
