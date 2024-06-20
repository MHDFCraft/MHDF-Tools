package cn.ChengZhiYa.MHDFTools.listener;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.util.BCUtil.TpPlayerTo;

public final class ReSpawnTeleportSpawn implements Listener {
    @EventHandler
    public void onEvent(PlayerRespawnEvent event) {
        if (MHDFTools.instance.getConfig().getBoolean("SpawnSettings.Enable") && MHDFTools.instance.getConfig().getBoolean("SpawnSettings.ReSpawnTeleport")) {
            Player player = event.getPlayer();
            World world = Bukkit.getWorld(Objects.requireNonNull(MHDFTools.instance.getConfig().getString("SpawnSettings.World")));
            double X = MHDFTools.instance.getConfig().getDouble("SpawnSettings.X");
            double Y = MHDFTools.instance.getConfig().getDouble("SpawnSettings.Y");
            double Z = MHDFTools.instance.getConfig().getDouble("SpawnSettings.Z");
            float Yaw = (float) MHDFTools.instance.getConfig().getDouble("SpawnSettings.Yaw");
            float Pitch = (float) MHDFTools.instance.getConfig().getDouble("SpawnSettings.Pitch");
            Location SpawnLcation = new Location(world, X, Y, Z, Yaw, Pitch);
            Bukkit.getScheduler().runTaskLater(MHDFTools.instance, () -> TpPlayerTo(player.getName(), MHDFTools.instance.getConfig().getString("SpawnSettings.Server"), SpawnLcation), 5);
        }
    }
}
