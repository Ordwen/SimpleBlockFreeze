package com.ordwen.simpleblockfreeze.configuration;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.configuration.item.ItemGetter;
import com.ordwen.simpleblockfreeze.storage.sql.SQLManager;
import com.ordwen.simpleblockfreeze.storage.sql.h2.H2Manager;
import com.ordwen.simpleblockfreeze.storage.sql.mysql.MySQLManager;
import com.ordwen.simpleblockfreeze.tools.ColorConvert;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Configuration {

    private static ItemStack item;
    private static SQLManager sqlManager;

    private static final Set<Material> verticalBlocks = new HashSet<>();
    private static final Set<String> disabledWorlds = new HashSet<>();

    private static boolean isWorldGuardEnabled;
    private static WorldGuardPlatform wgPlatform = null;

    private final SimpleBlockFreeze plugin;
    private final FileConfiguration config;

    public Configuration(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void load() {
        plugin.saveDefaultConfig();
        loadItem();
        loadSQLManager();
        loadWorldGuard();
        loadVerticalBlocks();
        loadDisabledWorlds();
    }

    /**
     * Get the list of vertical blocks.
     */
    private void loadVerticalBlocks() {
        verticalBlocks.clear();

        final List<String> verticalBlocksList = config.getStringList("vertical_blocks");
        if (verticalBlocksList.isEmpty()) {
            PluginLogger.warn("vertical_blocks list is empty in config.yml.");
            return;
        }

        for (String block : verticalBlocksList) {
            final Material material = Material.getMaterial(block);
            if (material == null) {
                PluginLogger.configurationError(block, "Invalid material");
                continue;
            }

            verticalBlocks.add(material);
        }
    }

    /**
     * Load the list of disabled worlds from the configuration file.
     */
    private void loadDisabledWorlds() {
        disabledWorlds.clear();

        final List<String> disabledWorldsList = config.getStringList("disabled_worlds");
        if (disabledWorldsList.isEmpty()) {
            PluginLogger.warn("List of disabled worlds is empty in config.yml.");
            return;
        }

        disabledWorlds.addAll(disabledWorldsList);
    }

    /**
     * Load the item used to freeze blocks from the configuration file.
     */
    private void loadItem() {
        final ConfigurationSection itemSection = config.getConfigurationSection("item");
        if (itemSection == null) {
            PluginLogger.error("Item section not found in config.yml.");
            throw new IllegalArgumentException("Item section not found in config.yml.");
        }

        final String material = itemSection.getString("material");
        if (material == null) {
            PluginLogger.error("Material not found in item section in config.yml.");
            throw new IllegalArgumentException("Material not found in item section in config.yml.");
        }

        setItem(material, itemSection);
    }

    /**
     * Set the item used to freeze blocks.
     *
     * @param material    material of the item
     * @param itemSection item section of the configuration file
     */
    private static void setItem(String material, ConfigurationSection itemSection) {
        item = new ItemGetter().getItemStackFromMaterial(material, "material");

        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        final String name = itemSection.getString("name");
        if (name != null) itemMeta.setDisplayName(ColorConvert.convertColorCode(name));

        final List<String> lore = itemSection.getStringList("lore");
        if (!lore.isEmpty()) lore.replaceAll(ColorConvert::convertColorCode);

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
    }

    /**
     * Initialize the storage manager depending on the storage mode.
     */
    private void loadSQLManager() {
        final String storageMode = config.getString("storage_mode");
        if (storageMode == null) {
            PluginLogger.error("Storage mode not found in config.yml.");
            throw new IllegalArgumentException("Storage mode not found in config.yml.");
        }

        if (storageMode.equalsIgnoreCase("mysql")) {
            PluginLogger.info("MySQL storage mode enabled.");
            setSQLManager(new MySQLManager(plugin));
        } else if (storageMode.equalsIgnoreCase("h2")) {
            PluginLogger.info("H2 storage mode enabled.");
            setSQLManager(new H2Manager());
        } else {
            PluginLogger.error("Invalid storage mode in config.yml.");
            throw new IllegalArgumentException("Invalid storage mode in config.yml.");
        }
    }

    /**
     * Set the SQL manager.
     *
     * @param manager SQLManager instance
     */
    private static void setSQLManager(SQLManager manager) {
        sqlManager = manager;
    }

    /**
     * Load WorldGuard if enabled in the configuration file.
     */
    private void loadWorldGuard() {
        final boolean enabled = config.getBoolean("use_worldguard");
        if (enabled) {
            boolean isWorldGuardLoaded = plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null;
            if (!isWorldGuardLoaded) {
                PluginLogger.warn("WorldGuard is not loaded but use_worldguard is enabled in config.yml.");
                PluginLogger.warn("WorldGuard features will be disabled.");
                return;
            }
            setWorldGuardPlatform(WorldGuard.getInstance().getPlatform());
            setWorldGuardEnabled();
        }
    }

    /**
     * Set the WorldGuard platform.
     *
     * @param platform WorldGuardPlatform instance
     */
    private static void setWorldGuardPlatform(WorldGuardPlatform platform) {
        wgPlatform = platform;
    }

    /**
     * Set WorldGuard enabled.
     */
    private static void setWorldGuardEnabled() {
        isWorldGuardEnabled = true;
    }

    /**
     * Check if the player can build at the location.
     *
     * @param player   the player
     * @param world    the world
     * @param location the location
     * @return true if the player can build, false otherwise
     */
    public static boolean canBuild(Player player, World world, Location location) {
        if (disabledWorlds.contains(world.getName())) return false;
        if (!isWorldGuardEnabled) return true;

        final com.sk89q.worldedit.util.Location adaptedLocation = BukkitAdapter.adapt(location);
        final com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(world);
        final LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        if (wgPlatform.getSessionManager().hasBypass(localPlayer, adaptedWorld)) return true;

        final RegionQuery query = wgPlatform.getRegionContainer().createQuery();
        return query.testBuild(adaptedLocation, localPlayer, Flags.BUILD);
    }

    /**
     * Get the item used to freeze blocks.
     *
     * @return an ItemStack
     */
    public static ItemStack getItem() {
        return item;
    }

    /**
     * Get the SQL manager.
     *
     * @return the SQL manager
     */
    public static SQLManager getSQLManager() {
        return sqlManager;
    }

    /**
     * Check if the vertical blocks list contains the material.
     *
     * @param material material to check
     * @return true if the material is in the list, false otherwise
     */
    public static boolean containsVerticalBlock(Material material) {
        System.out.println("contains vertical block: " + verticalBlocks.contains(material));
        return verticalBlocks.contains(material);
    }
}
