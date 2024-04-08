package com.ordwen.simpleblockfreeze.tools.externals;

import com.ordwen.simpleblockfreeze.tools.Pair;
import org.bukkit.inventory.ItemStack;

public interface IExternalItemGetter {

    Pair<String, ItemStack> getOraxenItem(String namespace);
    Pair<String, ItemStack> getItemsAdderItem(String namespace);
    Pair<String, ItemStack> getMMOItemsItem(String namespace);
    Pair<String, ItemStack> getCustomHead(String texture);
}
