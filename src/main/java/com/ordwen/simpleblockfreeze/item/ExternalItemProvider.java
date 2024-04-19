package com.ordwen.simpleblockfreeze.item;

import org.bukkit.inventory.ItemStack;

public interface ExternalItemProvider {
    boolean isValidNamespace(String namespace);
    ItemStack getItem(String namespace);
}
