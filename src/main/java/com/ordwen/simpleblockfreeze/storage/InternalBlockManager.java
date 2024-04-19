package com.ordwen.simpleblockfreeze.storage;

import com.jeff_media.customblockdata.CustomBlockData;
import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;

import java.util.concurrent.CompletableFuture;

public class InternalBlockManager implements IBlockManager {

    private final NamespacedKey freezeKey;

    public InternalBlockManager(SimpleBlockFreeze plugin) {
        this.freezeKey = new NamespacedKey(plugin, "SIMPLE_BLOCK_FREEZE");
    }

    @Override
    public void init() {
        // nothing to do
    }

    @Override
    public void close() {
        // nothing to do
    }

    @Override
    public CompletableFuture<Void> saveLocation(World world, int x, int y, int z) {
        cbd(world, x, y, z).set(freezeKey, PersistentDataType.BYTE, (byte) 1);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> deleteLocation(World world, int x, int y, int z) {
        cbd(world, x, y, z).remove(freezeKey);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Boolean> searchLocation(World world, int x, int y, int z) {
        return CompletableFuture.completedFuture(cbd(world, x, y, z).has(freezeKey));
    }

    private CustomBlockData cbd(World world, int x, int y, int z) {
        return new CustomBlockData(world.getBlockAt(x, y, z), SimpleBlockFreeze.getInstance());
    }
}
