package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.BCUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public final class PlayerSpawnListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!isEnabled()) return;

        Player player = event.getPlayer();
        Location spawnLocation = getSpawnLocation();

        if (spawnLocation != null) {
            Bukkit.getScheduler().runTaskLater(MHDFTools.instance, () -> {
                String serverName = MHDFTools.instance.getConfig().getString("SpawnSettings.Server");
                BCUtil.TpPlayerTo(player.getName(), serverName, spawnLocation);
            }, 5);
        }
    }

    private boolean isEnabled() {
        return MHDFTools.instance.getConfig().getBoolean("SpawnSettings.Enable") &&
                MHDFTools.instance.getConfig().getBoolean("SpawnSettings.JoinTeleport");
    }

    private Location getSpawnLocation() {
        World world = Bukkit.getWorld(Objects.requireNonNull(MHDFTools.instance.getConfig().getString("SpawnSettings.World")));
        double X = MHDFTools.instance.getConfig().getDouble("SpawnSettings.X");
        double Y = MHDFTools.instance.getConfig().getDouble("SpawnSettings.Y");
        double Z = MHDFTools.instance.getConfig().getDouble("SpawnSettings.Z");
        float yaw = (float) MHDFTools.instance.getConfig().getDouble("SpawnSettings.Yaw");
        float pitch = (float) MHDFTools.instance.getConfig().getDouble("SpawnSettings.Pitch");

        if (world == null) {
            return null;
        }

        return new Location(world, X, Y, Z, yaw, pitch);
    }
}