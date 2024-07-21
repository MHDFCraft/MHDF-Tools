package cn.ChengZhiYa.MHDFTools.utils.command;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;

import java.util.Objects;

public final class SpawnUtil {
    public static SuperLocation getSpawnLocation() {
        String worldName = Objects.requireNonNull(MHDFTools.instance.getConfig().getString("SpawnSettings.World"));
        double x = MHDFTools.instance.getConfig().getDouble("SpawnSettings.X");
        double y = MHDFTools.instance.getConfig().getDouble("SpawnSettings.Y");
        double z = MHDFTools.instance.getConfig().getDouble("SpawnSettings.Z");
        float yaw = (float) MHDFTools.instance.getConfig().getDouble("SpawnSettings.Yaw");
        float pitch = (float) MHDFTools.instance.getConfig().getDouble("SpawnSettings.Pitch");

        return new SuperLocation(worldName, x, y, z, yaw, pitch);
    }

    public static String getServerName() {
        return MHDFTools.instance.getConfig().getString("SpawnSettings.Server");
    }
}
