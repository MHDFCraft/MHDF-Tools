package cn.ChengZhiYa.ChengToolsReloaded.Event;

import cn.ChengZhiYa.ChengToolsReloaded.HashMap.LocationHashMap;
import cn.ChengZhiYa.ChengToolsReloaded.HashMap.StringHashMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static cn.ChengZhiYa.ChengToolsReloaded.Ultis.multi.ChatColor;

public class PlayerRespawn_Event implements Listener {
    @EventHandler
    public void On_Event(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (StringHashMap.Get(player.getName() + "_Fly") != null) {
            player.setAllowFlight(true);
        }

        if (LocationHashMap.Get(player.getName() + "_DeathLocation") != null) {
            Location DiedLocation = LocationHashMap.Get(player.getName() + "_DeathLocation");
            int X = DiedLocation.getBlockX();
            int Y = DiedLocation.getBlockY();
            int Z = DiedLocation.getBlockZ();
            player.sendMessage(ChatColor("&a&l您的死亡位置如下，返回死亡地点请输入/back"));
            player.sendMessage(ChatColor("&e&lX:" + X + "&e&lY:" + Y + "&e&lZ:" + Z));
        }
    }
}
