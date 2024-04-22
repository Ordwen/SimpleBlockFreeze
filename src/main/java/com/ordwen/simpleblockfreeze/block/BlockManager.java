package com.ordwen.simpleblockfreeze.block;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ordwen.simpleblockfreeze.LocationKey;
import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BlockManager {

    private final SimpleBlockFreeze plugin;
    private final File file;

    private final Set<LocationKey> keys;

    public BlockManager(SimpleBlockFreeze plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "blocks.json");
        this.keys = new HashSet<>();
    }

    public boolean isFreezeBlock(Block block) {
        return isFreezeBlock(block.getLocation());
    }

    public boolean isFreezeBlock(Location location) {
        return keys.contains(new LocationKey(location));
    }

    public void freezeBlock(Block block) {
        freezeBlock(block.getLocation());
    }

    public void freezeBlock(Location location) {
        keys.add(new LocationKey(location));
    }

    public void unfreezeBlock(Block block) {
        unfreezeBlock(block.getLocation());
    }

    public void unfreezeBlock(Location location) {
        keys.remove(new LocationKey(location));
    }

    public void load() throws IOException {

        keys.clear();

        if(!file.exists()) {
            return;
        }

        final Gson gson = new Gson();
        final BufferedReader fileReader = new BufferedReader(new FileReader(file));

        final JsonArray array = JsonParser.parseReader(fileReader).getAsJsonArray();

        for(JsonElement element : array) {
            keys.add(LocationKey.fromString(element.getAsString()));
        }

        fileReader.close();
    }

    public void save() throws IOException {

        final Gson gson = new Gson();
        final JsonArray array = new JsonArray();

        for(LocationKey key : keys) {
            array.add(key.toString());
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            gson.toJson(array, writer);
        }
    }

    public CompletableFuture<Void> saveAsync() {
        return CompletableFuture.runAsync(() -> {
            try {
                save();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });
    }
}
