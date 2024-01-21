package cn.ChengZhiYa.MHDFTools.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.SaveLocation;
import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.ServerName;

public final class TpBack implements Listener {
    @EventHandler
    public void PlayerTeleportEvent(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        SaveLocation(player.getName() + "_TpBackLocation", ServerName, player.getLocation());
    }
}
