package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Objects;

public final class PlayerBackListener implements Listener {

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        MapUtil.getLocationHashMap().put(player.getName() + "_DeathLocation", player.getLocation());
    }

    @EventHandler
    public void PlayerRespawnEvent(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (MapUtil.getLocationHashMap().get(player.getName() + "_DeathLocation") != null) {
            Location DiedLocation = MapUtil.getLocationHashMap().get(player.getName() + "_DeathLocation");
            int X = DiedLocation.getBlockX();
            int Y = DiedLocation.getBlockY();
            int Z = DiedLocation.getBlockZ();
            player.sendMessage(SpigotUtil.i18n("Back.ReSpawn", String.valueOf(X), String.valueOf(Y), String.valueOf(Z)));
        }
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getX() != event.getTo().getX() || player.getLocation().getZ() != event.getTo().getZ() || player.getLocation().getY() != event.getTo().getY()) {
            if (MapUtil.getIntHashMap().get(player.getName() + "_BackDelay") != null) {
                MapUtil.getIntHashMap().remove(player.getName() + "_BackDelay");
                SpigotUtil.sendTitle(Objects.requireNonNull(player), SpigotUtil.i18n("TeleportMove"));
            }
        }
    }
}
