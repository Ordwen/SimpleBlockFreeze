package com.ordwen.simpleblockfreeze.enums;

public enum Permissions {
    ADMIN("simpleblockfreeze.admin"),
    USE("simpleblockfreeze.use"),
    COMMAND("simpleblockfreeze.command")
    ;

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return permission;
    }
}
