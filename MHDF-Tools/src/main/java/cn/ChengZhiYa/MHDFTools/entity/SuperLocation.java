package cn.ChengZhiYa.MHDFTools.entity;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;

@Data
public final class SuperLocation {
    String worldName;
    Double x;
    Double y;
    Double z;
    Float yaw;
    Float pitch;

    public SuperLocation(String worldName, Double x, Double y, Double z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SuperLocation(String worldName, Double x, Double y, Double z, Float yaw, Float pitch) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SuperLocation(Location location) {
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public int getBlockX() {
        return NumberConversions.floor(this.x);
    }

    public int getBlockY() {
        return NumberConversions.floor(this.y);
    }

    public int getBlockZ() {
        return NumberConversions.floor(this.z);
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(this.worldName), this.x, this.y, this.z, this.yaw, this.pitch);
    }
}
