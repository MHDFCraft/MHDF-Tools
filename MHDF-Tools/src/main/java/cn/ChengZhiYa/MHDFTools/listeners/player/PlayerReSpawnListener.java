package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.PluginLoader;
import cn.ChengZhiYa.MHDFTools.entity.SuperLocation;
import cn.ChengZhiYa.MHDFTools.utils.BungeeCordUtil;
import cn.ChengZhiYa.MHDFTools.utils.command.SpawnUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerReSpawnListener implements Listener {
    JavaPlugin plugin = PluginLoader.INSTANCE.getPlugin();

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!isEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        SuperLocation spawnLocation = SpawnUtil.getSpawnLocation();

        Bukkit.getRegionScheduler().runDelayed(plugin, player.getLocation(), task -> {
            BungeeCordUtil.tpPlayerTo(player.getName(), SpawnUtil.getServerName(), spawnLocation);
        }, 5L);
    }

    private boolean isEnabled() {
        return PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("SpawnSettings.Enable", false) && PluginLoader.INSTANCE.getPlugin().getConfig().getBoolean("SpawnSettings.ReSpawnTeleport", false);
    }
}