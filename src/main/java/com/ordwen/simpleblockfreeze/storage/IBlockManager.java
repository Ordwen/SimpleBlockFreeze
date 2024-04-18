package com.ordwen.simpleblockfreeze.storage;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface IBlockManager {

    void init();
    void close();

    CompletableFuture<Void> saveLocation(World world, int x, int y, int z);
    CompletableFuture<Void> deleteLocation(World world, int x, int y, int z);
    CompletableFuture<Boolean> searchLocation(World world, int x, int y, int z);

    default CompletableFuture<Void> saveLocation(String worldName, int x, int y, int z) {
        return saveLocation(Objects.requireNonNull(Bukkit.getWorld(worldName)), x, y, z);
    }

    default CompletableFuture<Void> deleteLocation(String worldName, int x, int y, int z) {
        return deleteLocation(Objects.requireNonNull(Bukkit.getWorld(worldName)), x, y, z);
    }

    default CompletableFuture<Boolean> searchLocation(String worldName, int x, int y, int z) {
        return searchLocation(Objects.requireNonNull(Bukkit.getWorld(worldName)), x, y, z);
    }
}
