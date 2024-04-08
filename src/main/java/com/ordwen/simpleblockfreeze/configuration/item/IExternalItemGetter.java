package com.ordwen.simpleblockfreeze.configuration.item;

import com.ordwen.simpleblockfreeze.tools.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface IExternalItemGetter {

    Pair<String, ItemStack> getOraxenItem(String namespace);
    Pair<String, ItemStack> getItemsAdderItem(String namespace);
    Pair<String, ItemStack> getCustomModelDataItem(Material material, int customModelData);
}
