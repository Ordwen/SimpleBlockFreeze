package com.ordwen.simpleblockfreeze.storage.sql;

import org.bukkit.configuration.ConfigurationSection;

public class DatabaseCredentials {

    private String host;
    private String dbName;
    private String password;
    private String user;
    private String port;

    public DatabaseCredentials(ConfigurationSection section) {
        this(
                section.getString("host"),
                section.getString("name"),
                section.getString("password"),
                section.getString("user"),
                section.getString("port")
        );
    }

    public DatabaseCredentials(String host, String dbName, String password, String user, String port) {
        this.host = host;
        this.dbName = dbName;
        this.password = password;
        this.user = user;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
