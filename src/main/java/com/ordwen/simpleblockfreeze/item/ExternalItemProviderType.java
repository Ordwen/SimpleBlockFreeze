package com.ordwen.simpleblockfreeze.item;

import com.ordwen.simpleblockfreeze.item.impl.ItemAdderItemProvider;
import com.ordwen.simpleblockfreeze.item.impl.OraxenItemProvider;

import java.util.function.Supplier;

public enum ExternalItemProviderType {

    ORAXEN("Oraxen", OraxenItemProvider::new),
    ITEM_ADDER("ItemAdder", ItemAdderItemProvider::new),
    ;

    private final String pluginName;
    private final Supplier<ExternalItemProvider> supplier;

    ExternalItemProviderType(String pluginName, Supplier<ExternalItemProvider> supplier) {
        this.pluginName = pluginName;
        this.supplier = supplier;
    }

    public String getPluginName() {
        return pluginName;
    }

     public ExternalItemProvider getProvider() {
        return supplier.get();
     }
}
