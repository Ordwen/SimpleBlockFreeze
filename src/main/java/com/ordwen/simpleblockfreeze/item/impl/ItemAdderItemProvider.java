package com.ordwen.simpleblockfreeze.item.impl;

import com.ordwen.simpleblockfreeze.item.ExternalItemProvider;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class ItemAdderItemProvider implements ExternalItemProvider {

    @Override
    public boolean isValidNamespace(String namespace) {
        return CustomStack.isInRegistry(namespace);
    }

    @Override
    public ItemStack getItem(String namespace) {
        return CustomStack.getInstance(namespace).getItemStack();
    }
}
