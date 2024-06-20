package cn.ChengZhiYa.MHDFTools.task;

import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.BCUtil.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.utils.Util.*;

public final class Back extends BukkitRunnable {

    @Override
    public void run() {
        for (Object key : MapUtil.getIntHasMap().keySet()) {
            if (key.toString().contains("_BackDelay")) {
                String playerName = key.toString().replace("_BackDelay", "");
                Player player = Bukkit.getPlayer(playerName);

                if (player != null) {
                    handlePlayerBack(player, key.toString(), playerName);
                } else {
                    MapUtil.getIntHasMap().remove(key);
                }
            }
        }
    }

    private void handlePlayerBack(Player player, String key, String playerName) {
        int teleportDelay = MapUtil.getIntHasMap().get(key);

        sendTitle(player, i18n("TeleportDelay." + teleportDelay));

        if (teleportDelay > 0) {
            playSound(player, sound("TeleportDelay." + teleportDelay));
            MapUtil.getIntHasMap().put(key, teleportDelay - 1);
        } else {
            playSound(player, sound("TeleportSound"));
            TpPlayerTo(playerName, ServerName, MapUtil.getLocationHasMap().get(playerName + "_DeathLocation"));
            MapUtil.getLocationHasMap().put(playerName + "_UnBackLocation", player.getLocation());
            player.sendMessage(i18n("Back.Done"));
            MapUtil.getIntHasMap().remove(key);
        }
    }
}