package com.ordwen.simpleblockfreeze.item.impl;

import com.ordwen.simpleblockfreeze.item.ExternalItemProvider;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.inventory.ItemStack;

public class OraxenItemProvider implements ExternalItemProvider {

    @Override
    public boolean isValidNamespace(String namespace) {
        return OraxenItems.exists(namespace);
    }

    @Override
    public ItemStack getItem(String namespace) {
        return OraxenItems.getItemById(namespace).build();
    }
}
