package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCord;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
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

        Bukkit.getScheduler().runTaskLater(MHDFTools.instance, () -> {
            BungeeCord.TpPlayerTo(player.getName(), getServerName(), spawnLocation);
        }, 5);
    }

    private boolean isEnabled() {
        return MHDFTools.instance.getConfig().getBoolean("SpawnSettings.Enable") &&
                MHDFTools.instance.getConfig().getBoolean("SpawnSettings.JoinTeleport");
    }

    private Location getSpawnLocation() {
        World world = Bukkit.getWorld(Objects.requireNonNull(MHDFTools.instance.getConfig().getString("SpawnSettings.World")));

        double x = getConfig().getDouble("SpawnSettings.X");
        double y = getConfig().getDouble("SpawnSettings.Y");
        double z = getConfig().getDouble("SpawnSettings.Z");
        float yaw = (float) getConfig().getDouble("SpawnSettings.Yaw");
        float pitch = (float) getConfig().getDouble("SpawnSettings.Pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    private String getServerName() {
        return getConfig().getString("SpawnSettings.Server");
    }

    private FileConfiguration getConfig() {
        return MHDFTools.instance.getConfig();
    }
}