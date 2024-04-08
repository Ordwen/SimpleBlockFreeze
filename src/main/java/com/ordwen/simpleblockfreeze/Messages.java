package com.ordwen.simpleblockfreeze;

import com.ordwen.simpleblockfreeze.tools.ColorConvert;
import org.bukkit.configuration.file.FileConfiguration;

public enum Messages {

    NO_PERMISSION("no_permission", "&cYou don't have permission."),
    NO_PERMISSION_CATEGORY("no_permission_category", "&cYou don't have permission to see this category."),
    PLAYER_ONLY("player_only", "&cOnly player can execute this command."),

    FREEZE_SUCCESS("freeze_success", "&aBlock successfully frozen."),
    UNFREEZE_SUCCESS("unfreeze_success", "&aBlock successfully unfrozen."),
    ALREADY_FROZEN("already_frozen", "&cThis block is already frozen."),
    FREEZE_NOT_FOUND("freeze_not_found", "&cThis block is not frozen."),
    ERROR_OCCURRED("error_occurred", "&cAn error occurred. Please contact a server administrator."),
    ;

    private final String path;
    private final String defaultMessage;
    private static FileConfiguration LANG;

    /**
     * Message constructor.
     *
     * @param message message (String).
     */
    Messages(String path, String message) {
        this.path = path;
        this.defaultMessage = message;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     *
     * @param messagesFile the config to set.
     */
    public static void setFile(FileConfiguration messagesFile) {
        LANG = messagesFile;
    }

    /**
     * Get message.
     *
     * @return message.
     */
    @Override
    public String toString() {
        String msg = LANG.getString(this.path, defaultMessage);

        if (msg.trim().isEmpty()) return null;
        else return ColorConvert.convertColorCode(msg);
    }

    /**
     * Get the default value of the path.
     *
     * @return the default value of the path.
     */
    public String getDefault() {
        return this.defaultMessage;
    }

    /**
     * Get the path to the string.
     *
     * @return the path to the string.
     */
    public String getPath() {
        return this.path;
    }
}
