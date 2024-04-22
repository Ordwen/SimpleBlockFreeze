package com.ordwen.simpleblockfreeze;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Objects;

public class LocationKey {

    private final static String DELIMITER = ":";

    private final String worldName;
    private final int X;
    private final int Y;
    private final int Z;

    public LocationKey(String worldName, int x, int y, int z) {
        this.worldName = worldName;
        X = x;
        Y = y;
        Z = z;
    }

    public LocationKey(Location location) {
        this(
                location.getWorld().getName(),
                location.getBlockX(),
                location.getBlockX(),
                location.getBlockZ()
        );
    }

    public static LocationKey fromString(String literal) {
        final String[] args = literal.split(DELIMITER);
        final World world = Bukkit.getWorld(args[0]);
        final int X = Integer.parseInt(args[1]);
        final int Y = Integer.parseInt(args[2]);
        final int Z = Integer.parseInt(args[3]);
        return new LocationKey(new Location(world, X, Y, Z));
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(worldName), X, Y, Z);
    }

    @Override
    public String toString() {
        return String.join(
                DELIMITER,
                Arrays.asList(
                        worldName, String.valueOf(X), String.valueOf(Y), String.valueOf(Z)
                )
        );
    }

    @Override
    public boolean equals(Object object) {

        if(object == this) {
            return true;
        }

        if(!(object instanceof LocationKey otherKey)) {
            return false;
        }

        return Objects.equals(otherKey.worldName, worldName) &&
                Objects.equals(otherKey.X, X) &&
                Objects.equals(otherKey.Y, Y) &&
                Objects.equals(otherKey.Z, Z);
    }
}
