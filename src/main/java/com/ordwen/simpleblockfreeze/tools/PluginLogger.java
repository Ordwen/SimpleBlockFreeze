package com.ordwen.simpleblockfreeze.tools;

import java.util.logging.Logger;

public class PluginLogger {

    private PluginLogger() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger logger = org.bukkit.plugin.PluginLogger.getLogger("SimpleBlockFreeze");

    public static void info(String msg) {
        logger.info(msg);
    }

    public static void warn(String msg) {
        logger.warning(msg);
    }

    public static void error(String msg) {
        logger.severe(msg);
    }

    public static void fine(String msg) {
        logger.fine(msg);
    }

    /**
     * Display an error message in the console when something cannot be loaded because of a configuration error.
     *
     * @param parameter  the parameter that caused the error
     * @param reason     the reason of the error
     */
    public static void configurationError(String parameter, String reason) {
        PluginLogger.error("-----------------------------------");
        PluginLogger.error("Invalid configuration detected.");
        PluginLogger.error("Reason : " + reason);

        if (parameter != null) {
            PluginLogger.error("Parameter : " + parameter);
        }

        PluginLogger.error("-----------------------------------");
    }
}
