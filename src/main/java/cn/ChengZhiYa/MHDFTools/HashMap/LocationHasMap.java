package cn.ChengZhiYa.MHDFTools.HashMap;

import org.bukkit.Location;

import java.util.HashMap;

public final class LocationHasMap {
    static final HashMap<Object, Location> Temp = new HashMap<>();

    public static HashMap<Object, Location> getHasMap() {
        return Temp;
    }
}
