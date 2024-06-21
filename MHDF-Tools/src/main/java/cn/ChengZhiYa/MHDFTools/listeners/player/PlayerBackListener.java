package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.i18n;

public final class PlayerBackListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        MapUtil.getLocationHashMap().put(player.getName() + "_DeathLocation", player.getLocation());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (MapUtil.getLocationHashMap().containsKey(player.getName() + "_DeathLocation")) {
            org.bukkit.Location diedLocation = MapUtil.getLocationHashMap().get(player.getName() + "_DeathLocation");
            int x = diedLocation.getBlockX();
            int y = diedLocation.getBlockY();
            int z = diedLocation.getBlockZ();
            player.sendMessage(i18n("Back.ReSpawn", String.valueOf(x), String.valueOf(y), String.valueOf(z)));
        }
    }
}