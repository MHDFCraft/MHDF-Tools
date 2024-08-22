package cn.ChengZhiYa.MHDFTools.utils.command;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;

import java.util.Objects;

public final class SpawnUtil {
    public static SuperLocation getSpawnLocation() {
        String worldName = Objects.requireNonNull(PluginLoader.INSTANCE.getPlugin().getConfig().getString("SpawnSettings.World"));
        double x = PluginLoader.INSTANCE.getPlugin().getConfig().getDouble("SpawnSettings.X");
        double y = PluginLoader.INSTANCE.getPlugin().getConfig().getDouble("SpawnSettings.Y");
        double z = PluginLoader.INSTANCE.getPlugin().getConfig().getDouble("SpawnSettings.Z");
        float yaw = (float) PluginLoader.INSTANCE.getPlugin().getConfig().getDouble("SpawnSettings.Yaw");
        float pitch = (float) PluginLoader.INSTANCE.getPlugin().getConfig().getDouble("SpawnSettings.Pitch");

        return new SuperLocation(worldName, x, y, z, yaw, pitch);
    }

    public static String getServerName() {
        return PluginLoader.INSTANCE.getPlugin().getConfig().getString("SpawnSettings.Server");
    }
}
