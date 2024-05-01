package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Options {

    public static List<String> DISABLED_WORLDS;
    public static Set<Material> VERTICAL_BLOCKS = new HashSet<>();

    public static void load(FileConfiguration configuration) {
        DISABLED_WORLDS = configuration.getStringList("disabled_worlds");
        loadVerticalBlocks(configuration);
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
}
