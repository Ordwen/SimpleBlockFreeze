package com.ordwen.simpleblockfreeze.tools.externals;

import com.ordwen.simpleblockfreeze.tools.Pair;
import dev.lone.itemsadder.api.CustomStack;
import io.th0rgal.oraxen.api.OraxenItems;

import org.bukkit.inventory.ItemStack;

public abstract class ExternalItemGetter implements IExternalItemGetter {

    /**
     * Get an Oraxen item by its namespace.
     * @param namespace the namespace of the item
     * @return the ItemStack or null if it does not exist
     */
    @Override
    public Pair<String, ItemStack> getOraxenItem(String namespace) {

        if (!OraxenItems.exists(namespace)) {
            return new Pair<>("The item " + namespace + " does not exist in Oraxen.", null);
        }

        return new Pair<>("", OraxenItems.getItemById(namespace).build());
    }

    /**
     * Get an ItemsAdder item by its namespace.
     * @param namespace the namespace of the item
     * @return the ItemStack or null if it does not exist
     */
    @Override
    public Pair<String, ItemStack> getItemsAdderItem(String namespace) {

        if (!namespace.contains(":")) {
            return new Pair<>("You need to provide the namespace and the id of the item.", null);
        }

        if (!CustomStack.isInRegistry(namespace)) {
            return new Pair<>("The item " + namespace + " does not exist in ItemsAdder.", null);
        }

        return new Pair<>("", CustomStack.getInstance(namespace).getItemStack());
    }
}
