package cn.ChengZhiYa.MHDFTools.entity;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;

@Data
public final class SuperLocation {
    String worldName;
    Double X;
    Double Y;
    Double Z;
    Float Yaw;
    Float Pitch;

    public SuperLocation(String worldName, Double X, Double Y, Double Z) {
        this.worldName = worldName;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public SuperLocation(String worldName, Double X, Double Y, Double Z, Float Yaw, Float Pitch) {
        this.worldName = worldName;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.Yaw = Yaw;
        this.Pitch = Pitch;
    }

    public SuperLocation(Location location) {
        this.worldName = location.getWorld().getName();
        this.X = location.getX();
        this.Y = location.getY();
        this.Z = location.getZ();
        this.Yaw = location.getYaw();
        this.Pitch = location.getPitch();
    }

    public int getBlockX() {
        return NumberConversions.floor(this.X);
    }

    public int getBlockY() {
        return NumberConversions.floor(this.Y);
    }

    public int getBlockZ() {
        return NumberConversions.floor(this.Z);
    }

    public Location getLocation() {
        return new Location(Bukkit.getWorld(this.worldName), this.X, this.Y, this.Z, this.Yaw, this.Pitch);
    }
}
