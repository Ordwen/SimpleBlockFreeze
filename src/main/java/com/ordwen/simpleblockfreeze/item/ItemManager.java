package com.ordwen.simpleblockfreeze.item;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.tools.ColorConvert;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class ItemManager {

    private ItemStack stack;

    private final Map<String, Integer> durabilityMap = new LinkedHashMap<>();

    /**
     * Load configuration related to the item.
     *
     * @param plugin        the plugin instance
     * @param configuration configuration to load from.
     */
    public void load(SimpleBlockFreeze plugin, FileConfiguration configuration) {
        loadItem(plugin, configuration);
        loadDurabilityMap(configuration);
    }

    /**
     * Load the item from the configuration.
     *
     * @param plugin        the plugin instance
     * @param configuration configuration to load from.
     */
    private void loadItem(SimpleBlockFreeze plugin, FileConfiguration configuration) {
        final ConfigurationSection section = configuration.getConfigurationSection("item");

        if (section == null) {
            PluginLogger.warn("No item section could be found, is the plugin setup correctly ?");
            return;
        }

        final String model = section.getString("material");

        if (model == null) {
            PluginLogger.warn("Item material is not specified in the configuration.");
            return;
        }

        ItemStack itemStack = createExternal(plugin, model);
        if (itemStack == null) itemStack = createInternal(model);

        this.stack = applyMeta(itemStack, section);
    }

    /**
     * Load the durability map from the configuration.
     *
     * @param configuration configuration to load from.
     */
    private void loadDurabilityMap(FileConfiguration configuration) {
        final ConfigurationSection section = configuration.getConfigurationSection("durability");

        if (section == null) {
            PluginLogger.warn("No durability section could be found, is the plugin setup correctly?");
            return;
        }

        // Getting keys from the section
        final Map<String, Integer> configMap = new HashMap<>();
        for (String key : section.getKeys(false)) {
            final int durability = section.getInt(key);
            configMap.put(key, durability);
        }

        // Ordering the map by custom comparator
        final Map<String, Integer> sortedMap = configMap.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    int value1 = entry1.getValue();
                    int value2 = entry2.getValue();

                    // Prioritize -1 values
                    if (value1 == -1 && value2 != -1) {
                        return -1;
                    } else if (value1 != -1 && value2 == -1) {
                        return 1;
                    } else {
                        return Integer.compare(value2, value1); // Descending order for other values
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Clearing the current map and adding the sorted map
        this.durabilityMap.clear();
        this.durabilityMap.putAll(sortedMap);
    }

    /**
     * Load the freeze item from an external provider.
     *
     * @param plugin the plugin instance
     * @param model  the model to load
     * @return the item stack
     */
    private ItemStack createExternal(SimpleBlockFreeze plugin, String model) {

        for (ExternalItemProviderType type : ExternalItemProviderType.values()) {

            if (!plugin.getServer().getPluginManager().isPluginEnabled(type.getPluginName())) {
                continue;
            }

            if (!type.getProvider().isValidNamespace(model)) {
                continue;
            }

            final ItemStack result = type.getProvider().getItem(model);

            if (result == null) {
                PluginLogger.warn("Failed to create item from external provider '" + type.getPluginName() + "'.");
                return null;
            }

            return result;
        }

        return null;
    }

    /**
     * Create an item stack from the provided material.
     *
     * @param model the model to create the item stack from
     * @return the item stack
     */
    private ItemStack createInternal(String model) {
        Material material = Material.matchMaterial(model);

        if (material == null) {
            PluginLogger.warn("Invalid material '" + model + "', defaulting to ARROW.");
            material = Material.ARROW;
        }

        return new ItemStack(material);
    }

    /**
     * Apply meta to the item stack from the configuration.
     *
     * @param itemStack the item stack to apply the meta to
     * @param section   the configuration section to apply the meta from
     * @return the item stack with the applied meta
     */
    private ItemStack applyMeta(ItemStack itemStack, ConfigurationSection section) {

        if (itemStack.getType().isAir()) {
            return itemStack;
        }

        final String name = section.getString("name");
        final List<String> lore = section.getStringList("lore");
        final int customModelData = section.getInt("custom_model_data", -1);

        final ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());

        if (name != null) {
            meta.setDisplayName(ColorConvert.convertColorCode(name));
        }

        if (!lore.isEmpty()) {
            meta.setLore(lore.stream().map(ColorConvert::convertColorCode).collect(Collectors.toList()));
        }

        if (customModelData != -1) {
            meta.setCustomModelData(customModelData);
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Check if the used item is the freeze item.
     *
     * @param stack the item stack to check
     * @return true if the item is the freeze item, false otherwise
     */
    public boolean isFreezeItem(ItemStack stack) {
        if (stack == null) return false;
        return this.stack.isSimilar(stack);
    }

    /**
     * Get the freeze item.
     *
     * @return ItemStack
     */
    public ItemStack getItemStack() {
        return this.stack;
    }

    public ItemStack getItemStackWithDurability(Player player) {
        final ItemStack itemStack = this.stack.clone();
        final ItemMeta meta = itemStack.getItemMeta();

        if (meta == null) {
            return itemStack;
        }

        final int durability = getDurability(player);
        meta.setCustomModelData(durability);
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    /**
     * Get the appropriate durability for the player.
     * Iterates through the durability map and returns the first match.
     * If no match is found, -1 is returned.
     *
     * @param player the player to get the durability for
     * @return the durability
     */
    private int getDurability(Player player) {
        for (Map.Entry<String, Integer> entry : durabilityMap.entrySet()) {
            final String permission = "simpleblockfreeze.durability." + entry.getKey();
            final int durability = entry.getValue();

            if (player.hasPermission(permission)) {
                return durability;
            }
        }

        return -1;
    }
}
