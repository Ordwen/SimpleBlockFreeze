package com.ordwen.simpleblockfreeze.item;

import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.tools.ColorConvert;
import com.ordwen.simpleblockfreeze.tools.PluginLogger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemManager {

    private ItemStack stack;

    public void load(SimpleBlockFreeze plugin, FileConfiguration configuration) {

        final ConfigurationSection section = configuration.getConfigurationSection("item");

        if(section == null) {
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

    public boolean similar(ItemStack stack) {
        if(stack == null) return false;
        return getItemStack().isSimilar(stack);
    }

    public ItemStack getItemStack() {
        return this.stack;
    }

    private ItemStack createExternal(@NotNull SimpleBlockFreeze plugin, @NotNull String model) {

        for (ExternalItemProviderType type : ExternalItemProviderType.values()) {

            if(!plugin.getServer().getPluginManager().isPluginEnabled(type.getPluginName())) {
                continue;
            }

            if(!type.getProvider().isValidNamespace(model)) {
                continue;
            }

            return type.getProvider().getItem(model);
        }

        return null;
    }

    private ItemStack createInternal(@NotNull String model) {
        Material material = Material.matchMaterial(model);

        if (material == null) {
            PluginLogger.warn("Invalid material '" + model + "', defaulting to ARROW.");
            material = Material.ARROW;
        }

        return new ItemStack(material);
    }

    private ItemStack applyMeta(@NotNull ItemStack itemStack, @NotNull ConfigurationSection section) {

        if(itemStack.getType().isAir()) {
            return itemStack;
        }

        final String name = section.getString("name");
        final List<String> description = section.getStringList("description");
        final int customModelData = section.getInt("custom_model_data", Integer.MIN_VALUE);

        final ItemMeta meta = Objects.requireNonNull(itemStack.getItemMeta());

        if(name != null) {
            meta.setDisplayName(ColorConvert.convertColorCode(name));
        }

        if(!description.isEmpty()) {
            meta.setLore(description.stream().map(ColorConvert::convertColorCode).collect(Collectors.toList()));
        }

        if(customModelData != Integer.MIN_VALUE) {
            meta.setCustomModelData(customModelData);
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
