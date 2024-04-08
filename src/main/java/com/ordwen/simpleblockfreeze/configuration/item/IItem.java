package com.ordwen.simpleblockfreeze.configuration.item;

import org.bukkit.inventory.ItemStack;

public interface IItem {
    ItemStack getExternalItem(String material, String parameter);
    ItemStack getOraxenItem(String namespace, String parameter);
    ItemStack getItemsAdderItem(String namespace, String parameter);
    ItemStack getCustomModelDataItem(String customModelData, String parameter);
}
