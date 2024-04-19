package com.ordwen.simpleblockfreeze.storage;

import com.google.common.base.Enums;
import com.google.common.base.Function;
import com.ordwen.simpleblockfreeze.SimpleBlockFreeze;
import com.ordwen.simpleblockfreeze.storage.sql.h2.H2Manager;
import com.ordwen.simpleblockfreeze.storage.sql.mysql.MySQLManager;
import org.jetbrains.annotations.Contract;

import java.util.Objects;

public enum StorageMode {

    H2((plugin) -> new H2Manager()),
    MYSQL(MySQLManager::new),
    INTERNAL(InternalBlockManager::new),
    ;

    private final Function<SimpleBlockFreeze, IBlockManager> function;

    StorageMode(Function<SimpleBlockFreeze, IBlockManager> function) {
        this.function = function;
    }

    @Contract("_ -> new")
    public IBlockManager createBlockManager(SimpleBlockFreeze plugin) {
        return function.apply(plugin);
     }

    public static StorageMode find(String literal) {
        Objects.requireNonNull(literal);
        return Enums.getIfPresent(StorageMode.class, literal.toUpperCase()).orNull();
    }

    public static StorageMode find(String literal, StorageMode defaultValue) {
        if(literal == null) return defaultValue;
        return Enums.getIfPresent(StorageMode.class, literal.toUpperCase()).or(defaultValue);
    }
}
