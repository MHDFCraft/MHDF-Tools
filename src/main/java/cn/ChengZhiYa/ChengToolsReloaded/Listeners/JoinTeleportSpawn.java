package cn.ChengZhiYa.ChengToolsReloaded.Listeners;

import cn.ChengZhiYa.ChengToolsReloaded.ChengToolsReloaded;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public final class JoinTeleportSpawn implements Listener {
    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        if (ChengToolsReloaded.instance.getConfig().getBoolean("SpawnSettings.Enable") && ChengToolsReloaded.instance.getConfig().getBoolean("SpawnSettings.JoinTeleport")) {
            Player player = event.getPlayer();
            World world = Bukkit.getWorld(Objects.requireNonNull(ChengToolsReloaded.instance.getConfig().getString("SpawnSettings.World")));
            double X = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.X");
            double Y = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Y");
            double Z = ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Z");
            float Yaw = (float) ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Yaw");
            float Pitch = (float) ChengToolsReloaded.instance.getConfig().getDouble("SpawnSettings.Pitch");
            Location SpawnLcation = new Location(world, X, Y, Z, Yaw, Pitch);
            player.teleport(SpawnLcation);
        }
    }
}
