package cn.ChengZhiYa.MHDFTools.Listeners;

import cn.ChengZhiYa.MHDFTools.HashMap.LocationHasMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.SaveLocation;
import static cn.ChengZhiYa.MHDFTools.Utils.BCUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.Utils.Util.i18n;

public final class Back implements Listener {
    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        SaveLocation(player.getName() + "_DeathLocation", ServerName, player.getLocation());
    }

    @EventHandler
    public void On_Event(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation") != null) {
            org.bukkit.Location DiedLocation = LocationHasMap.getHasMap().get(player.getName() + "_DeathLocation");
            int X = DiedLocation.getBlockX();
            int Y = DiedLocation.getBlockY();
            int Z = DiedLocation.getBlockZ();
            player.sendMessage(i18n("Back.ReSpawn", String.valueOf(X), String.valueOf(Y), String.valueOf(Z)));
        }
    }
}
