package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.MHDFTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCord.TpPlayerTo;

public final class PlayerReSpawnListener implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!isEnabled()) return;

        Player player = event.getPlayer();
        Location spawnLocation = getSpawnLocation();

        Bukkit.getScheduler().runTaskLater(MHDFTools.instance, () ->
                TpPlayerTo(player.getName(), getServerName(), spawnLocation), 5);
    }

    private boolean isEnabled() {
        return getConfig().getBoolean("SpawnSettings.Enable", false)
                && getConfig().getBoolean("SpawnSettings.ReSpawnTeleport", false);
    }

    private Location getSpawnLocation() {
        World world = Bukkit.getWorld(Objects.requireNonNull(getConfig().getString("SpawnSettings.World")));

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