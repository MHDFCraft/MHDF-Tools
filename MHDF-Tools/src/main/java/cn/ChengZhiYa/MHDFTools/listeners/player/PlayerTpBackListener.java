package cn.ChengZhiYa.MHDFTools.listeners.player;

import cn.ChengZhiYa.MHDFTools.utils.SpigotUtil;
import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Objects;

public final class PlayerTpBackListener implements Listener {
    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        MapUtil.getLocationHashMap().put(player.getName() + "_TpBackLocation", player.getLocation());
    }

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getX() != event.getTo().getX() || player.getLocation().getZ() != event.getTo().getZ() || player.getLocation().getY() != event.getTo().getY()) {
            if (MapUtil.getIntHashMap().get(player.getName() + "_TpBackDelay") != null) {
                MapUtil.getIntHashMap().remove(player.getName() + "_TpBackDelay");
                SpigotUtil.sendTitle(Objects.requireNonNull(player), SpigotUtil.i18n("TeleportMove"));
            }
        }
    }
}
