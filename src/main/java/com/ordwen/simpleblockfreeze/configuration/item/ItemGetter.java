package com.ordwen.simpleblockfreeze.configuration.item;

import com.ordwen.simpleblockfreeze.tools.Pair;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemGetter extends ExternalItemGetter implements IItem {

    /**
     * Get an item from a string.
     *
     * @param material   the material of the item
     * @param parameter  the parameter involved
     * @return the ItemStack or null if the item cannot be loaded
     */
    @Override
    public ItemStack getExternalItem(String material, String parameter) {
        final String[] split = material.split(":", 2);
        return switch (split[0]) {
            case "oraxen" -> this.getOraxenItem(split[1], parameter);
            case "itemsadder" -> this.getItemsAdderItem(split[1], parameter);
            case "custommodeldata" -> this.getCustomModelDataItem(split[1], parameter);
            default -> null;
        };
    }

    /**
     * Get an item from Oraxen.
     *
     * @param namespace  the namespace of the item
     * @param parameter  the parameter involved
     * @return the ItemStack or null if the item cannot be loaded
     */
    @Override
    public ItemStack getOraxenItem(String namespace, String parameter) {
        final Pair<String, ItemStack> result = super.getOraxenItem(namespace);
        if (!result.first().isEmpty()) {
            PluginLogger.configurationError(parameter, result.first());
            return null;
        }

        return result.second();
    }

    /**
     * Get an item from ItemsAdder.
     *
     * @param namespace  the namespace of the item
     * @param parameter  the parameter involved
     * @return the ItemStack or null if the item cannot be loaded
     */
    @Override
    public ItemStack getItemsAdderItem(String namespace, String parameter) {
        final Pair<String, ItemStack> result = super.getItemsAdderItem(namespace);
        if (!result.first().isEmpty()) {
            PluginLogger.configurationError(parameter, result.first());
            return null;
        }

        return result.second();
    }

    /**
     * Get an item with custom model data.
     *
     * @param customModelData the custom model data of the item
     * @param parameter       the parameter involved
     * @return the ItemStack or null if the item cannot be loaded
     */
    @Override
    public ItemStack getCustomModelDataItem(String customModelData, String parameter) {
        final String[] split = customModelData.split(":");
        if (split.length != 2) {
            PluginLogger.configurationError(parameter, "You need to provide the item and the custom model data.");
            return null;
        }

        final Material material = Material.getMaterial(split[0].toUpperCase());
        if (material == null) {
            PluginLogger.configurationError(parameter, "The material " + split[0] + " does not exist.");
            return null;
        }

        int cmd;
        try {
            cmd = Integer.parseInt(split[1]);
        } catch (Exception e) {
            PluginLogger.configurationError(parameter, split[1] + " is not a number!");
            return null;
        }

        final Pair<String, ItemStack> result = super.getCustomModelDataItem(material, cmd);

        if (!result.first().isEmpty()) {
            PluginLogger.configurationError(parameter, result.first());
            return null;
        }

        return result.second();
    }

    /**
     * Get an item stack from a material.
     *
     * @param material   the material to get
     * @return the item stack
     */
    public ItemStack getItemStackFromMaterial(String material, String parameter) {
        final ItemStack requiredItem;

        if (material.contains(":")) {
            requiredItem = getExternalItem(material, parameter);
            if (requiredItem == null) {
                PluginLogger.configurationError(parameter, "Invalid material type detected.");
                return null;
            }
        } else {
            try {
                requiredItem = new ItemStack(Material.valueOf(material));
            } catch (Exception e) {
                PluginLogger.configurationError(parameter, "Invalid material type detected.");
                return null;
            }
        }

        return requiredItem;
    }
}
