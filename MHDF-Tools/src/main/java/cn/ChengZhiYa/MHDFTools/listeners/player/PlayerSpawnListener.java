package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PlayerSpawnListener implements Listener {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!isEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        Location spawnLocation = getSpawnLocation();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            BungeeCordUtil.TpPlayerTo(player.getName(), getServerName(), spawnLocation);
        }, 5);
    }

    private boolean isEnabled() {
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean("SpawnSettings.Enable") &&
                config.getBoolean("SpawnSettings.JoinTeleport");
    }

    private Location getSpawnLocation() {
        FileConfiguration config = plugin.getConfig();
        World world = Bukkit.getWorld(Objects.requireNonNull(config.getString("SpawnSettings.World")));

        double x = config.getDouble("SpawnSettings.X");
        double y = config.getDouble("SpawnSettings.Y");
        double z = config.getDouble("SpawnSettings.Z");
        float yaw = (float) config.getDouble("SpawnSettings.Yaw");
        float pitch = (float) config.getDouble("SpawnSettings.Pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    private String getServerName() {
        FileConfiguration config = plugin.getConfig();
        return config.getString("SpawnSettings.Server");
    }
}