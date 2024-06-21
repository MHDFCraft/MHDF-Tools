package cn.ChengZhiYa.MHDFTools.task.player;

import cn.ChengZhiYa.MHDFTools.utils.map.MapUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static cn.ChengZhiYa.MHDFTools.utils.BungeeCord.ServerName;
import static cn.ChengZhiYa.MHDFTools.utils.BungeeCord.TpPlayerTo;
import static cn.ChengZhiYa.MHDFTools.utils.SpigotUtil.*;

public final class PlayerBackTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Object key : MapUtil.getIntHashMap().keySet()) {
            if (key.toString().contains("_BackDelay")) {
                String playerName = key.toString().replace("_BackDelay", "");
                Player player = Bukkit.getPlayer(playerName);

                if (player != null) {
                    handlePlayerBack(player, key.toString(), playerName);
                } else {
                    MapUtil.getIntHashMap().remove(key);
                }
            }
        }
    }

    private void handlePlayerBack(Player player, String key, String playerName) {
        int teleportDelay = MapUtil.getIntHashMap().get(key);

        sendTitle(player, i18n("TeleportDelay." + teleportDelay));

        if (teleportDelay > 0) {
            playSound(player, sound("TeleportDelay." + teleportDelay));
            MapUtil.getIntHashMap().put(key, teleportDelay - 1);
        } else {
            playSound(player, sound("TeleportSound"));
            TpPlayerTo(playerName, ServerName, MapUtil.getLocationHashMap().get(playerName + "_DeathLocation"));
            MapUtil.getLocationHashMap().put(playerName + "_UnBackLocation", player.getLocation());
            player.sendMessage(i18n("Back.Done"));
            MapUtil.getIntHashMap().remove(key);
        }
    }
}