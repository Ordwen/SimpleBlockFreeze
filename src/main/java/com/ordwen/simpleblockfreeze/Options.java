package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.enums.ParticleType;
import com.ordwen.simpleblockfreeze.tools.EnumUtils;
import com.ordwen.simpleblockfreeze.tools.Pair;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Options {

    public static List<String> DISABLED_WORLDS;
    public static Set<Material> VERTICAL_BLOCKS = new HashSet<>();
    public static EnumMap<ParticleType, Pair<Particle, Color>> PARTICLES = new EnumMap<>(ParticleType.class);

    public static void load(FileConfiguration configuration) {
        DISABLED_WORLDS = configuration.getStringList("disabled_worlds");
        loadVerticalBlocks(configuration);
        loadParticleEffects(configuration);
    }

    private static void loadVerticalBlocks(FileConfiguration configuration) {

        VERTICAL_BLOCKS.clear();

        for(String matLiteral : configuration.getStringList("vertical_blocks")) {

            final Material material = Material.matchMaterial(matLiteral);

            if(material == null) {
                PluginLogger.warn(matLiteral + "is not a valid material.");
                continue;
            }

            VERTICAL_BLOCKS.add(material);
        }
    }

    private static void loadParticleEffects(FileConfiguration configuration) {

        PARTICLES.clear();

        for(ParticleType particleType : ParticleType.values()) {
            final Particle particle = EnumUtils.verifyParticle(configuration.getString("particles." + particleType.lower() + ".type"));
            final Color color = particle == Particle.REDSTONE ? EnumUtils.verifyColor(configuration.getString("particles." + particleType.lower() + ".color")) : null;
            PARTICLES.put(particleType, new Pair<>(particle, color));
        }
    }
}
