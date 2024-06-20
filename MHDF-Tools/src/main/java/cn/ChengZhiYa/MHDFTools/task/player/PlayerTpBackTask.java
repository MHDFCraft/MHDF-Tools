package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCord.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.BungeeCord.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;

public final class PlayerTpBackTask extends BukkitRunnable {
    @Override
    public void run() {
        for (Object key : MapUtil.getIntHasMap().keySet()) {
            String keyString = key.toString();
            if (keyString.contains("_TpBackDelay")) {
                String playerName = keyString.replaceAll("_TpBackDelay", "");
                Player player = Bukkit.getPlayer(playerName);
                if (player != null) {
                    int delay = MapUtil.getIntHasMap().get(key);
                    sendTitle(player, i18n("TeleportDelay." + delay));
                    if (delay > 0) {
                        playSound(player, sound("TeleportDelay." + delay));
                        MapUtil.getIntHasMap().put(key, delay - 1);
                    } else {
                        playSound(player, sound("TeleportSound"));
                        TpPlayerTo(playerName, ServerName, MapUtil.getLocationHasMap().get(playerName + "_TpBackLocation"));
                        MapUtil.getLocationHasMap().put(playerName + "_UnBackLocation", player.getLocation());
                        player.sendMessage(i18n("TpBack.Done"));
                        MapUtil.getIntHasMap().remove(key);
                    }
                } else {
                    MapUtil.getIntHasMap().remove(key);
                }
            }
        }
    }
}